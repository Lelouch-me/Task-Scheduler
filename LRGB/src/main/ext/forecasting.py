import sys
import time
import pymysql
import numpy as np
import pandas as pd
import datetime as dt
from decimal import Decimal
from arch import arch_model
from sklearn.preprocessing import RobustScaler,StandardScaler

cnx = None

def simulate(model_fit,n_sims,n_steps):
    sig0=model_fit.conditional_volatility[-1]
    r0=scaled_data[-1]
    (omega,alpha,beta)=model_fit.params.values
    (mu_s,sig_s)=(pt.mean_,pt.scale_)
    paths=np.ones(n_sims)
    var=(sig0*sig0)*np.ones(n_sims)
    rets=r0*np.ones(n_sims)

    for step in range(n_steps):
        var[:]=omega+alpha*rets*rets+beta*var
        rets[:]=np.sqrt(var)*np.random.randn(n_sims)
        paths[:]=paths[:]*(1.0+(sig_s*rets+mu_s))
    paths[:]=paths-1.0

    return paths

def get_price_table(tickers,start_date,end_date,fc_days,keep_common_data=True):
    num_tickers=0
    valid_tickers=[]
    query = ("select date, adjstd_close from adjusted_price where code = %s and date >= %s and date <= %s")

    if (cnx is None):
        open_connection()

    ret_dict=dict()

    cursor=cnx.cursor()
    px_table=None
    for ticker in tickers:
        cursor.execute(query,(ticker,start_date,end_date))
        rows = cursor.fetchall()
        df= pd.DataFrame(list(rows),columns=['date',ticker])
        num_prices=df.index.size
        if (num_prices>0):
            lDate = df['date'].iloc[-1]
            use_latest = False
            if ((dt.datetime.today().date() - end_date.date()).days <= 1):
                # Remove this hardcoding to include intraday price once date added in intraday table ##################################
                query1 = ("select '2024-01-01', close from market_stat where code = %s")
                cursor.execute(query1,(ticker,))
                rows = cursor.fetchall()
                if rows is not None and len(rows) > 0:
                    nDate = dt.datetime.strptime(rows[0][0], "%Y-%m-%d")
                    thisDate = dt.date(nDate.year, nDate.month, nDate.day)
                    use_latest = True if (thisDate > lDate) else False
            if use_latest:
                df = pd.concat([df, pd.DataFrame([{'date': thisDate, ticker:Decimal(rows[0][1])}])], ignore_index=True)
                fDate = date_by_adding_business_days(thisDate, fc_days)
                ret_dict['latestPrice'] = float(rows[0][1])
                ret_dict['latestPriceDate'] = dt.datetime.strftime(nDate, "%Y-%m-%d %H:%M:%S")
                ret_dict['forecastDate'] = dt.datetime.strftime(fDate, "%Y-%m-%d")
            else:
                fDate = date_by_adding_business_days(lDate, fc_days)
                ret_dict['latestPrice'] = float(df[ticker].iloc[-1])
                ret_dict['latestPriceDate'] = dt.datetime.strftime(lDate, "%Y-%m-%d")
                ret_dict['forecastDate'] = dt.datetime.strftime(fDate, "%Y-%m-%d")
            df.index=pd.to_datetime(df['date'])
            del df['date']

            num_tickers=num_tickers+1
            valid_tickers.append(ticker)
            if (px_table is None):
                px_table = df
            else:
                if (keep_common_data):
                    px_table=pd.merge(px_table,df,how='inner',left_index=True,right_index=True)
                else:
                    px_table=pd.concat([px_table,df],axis=1,sort=True)
    if (keep_common_data):
        px_table = px_table.dropna()
    else:
        px_table=px_table.ffill()
        px_table=px_table.bfill()
    cursor.close()
    return (num_tickers,valid_tickers,px_table,ret_dict)

def date_by_adding_business_days(from_date, add_days):
    business_days_to_add = int(add_days)
    current_date = from_date
    while business_days_to_add > 0:
        current_date += dt.timedelta(days=1)
        weekday = current_date.weekday()
        if weekday >= 5: # sunday = 6
            continue
        business_days_to_add -= 1
    return current_date

def open_connection():
    global cnx
    cnx = pymysql.connect(
        user='developer', password='developer4',
        host='localhost',
        port=3306,
        database='dse_analyzer')
    return

def close_connection():
    global cnx
    cnx.close(); cnx=None
    return

#%%main
if __name__=="__main__":
    fc_instr = sys.argv[1]
    fc_days = sys.argv[2]
    fc_start = sys.argv[3]
    fc_end = sys.argv[4]

    np.random.seed(0)
    start_date=dt.datetime.strptime(fc_start, "%Y-%m-%d")
    end_date=dt.datetime.strptime(fc_end, "%Y-%m-%d")

    if fc_instr != "watchlist":
        tickers=[fc_instr]
        (num_tickers,valid_tickers,px_tbl,ret_dict) = get_price_table(tickers,start_date,end_date,fc_days,True)
    else:
        # Implement this later if needed to get price & return tables for watchlist similar to ticker, Right now setting it to None ##################################
        (px_tbl,ret_dict) = None

    use_standard_scalar=True
    if (use_standard_scalar):
        pt=StandardScaler()
    else:
        pt = RobustScaler(quantile_range=(2.5, 97.5))

    data = px_tbl.pct_change().values[1:,:]
    scaled_data = pt.fit_transform(data)

    model = arch_model(scaled_data, mean='Zero', vol='GARCH', p=1, q=1)
    model_fit = model.fit(disp='off')
    t_start=time.time()
    n_steps = int(fc_days) #forecast days into the future
    n_sims=500000 #number of simulations for forecast
    paths=simulate(model_fit,n_sims,n_steps)
    t_end=time.time()
    h_data = px_tbl.pct_change(n_steps).dropna().values

    start_idx = None
    idxs = []
    s_paths=np.sort(paths)
    fc_dict=dict()
    for i in np.linspace(-500,500,2001,endpoint=True):
        ret_val=i/100.0
        prob=s_paths[s_paths>ret_val].shape[0]/s_paths.shape[0]
        if prob >= 0.99995:
            start_idx = i
        else:
            if start_idx is not None:
                fc_dict[start_idx] = 100.00
                idxs.append(start_idx)
            fc_dict[i] = float("{:.2f}".format(100*prob))
            idxs.append(i)
            if prob <= 0.00005:
                break
    ret_dict['forecast'] = fc_dict
    ht_dict=dict()
    s_paths=np.sort(h_data)
    for i in idxs:
        ret_val=i/100.0
        prob=s_paths[s_paths>ret_val].shape[0]/s_paths.shape[0]
        ht_dict[i] = float("{:.2f}".format(100*prob))
    ret_dict['historical'] = ht_dict
    print(ret_dict)
