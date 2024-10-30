function getResultObject(tickers,date_range,bmTicker){
    var i = 0;
    var modifiedPriceArray = [];
    var priceArray = [];
    var volumeArray = [];
    var indicatorDataArray = [];
    var dateDtls = [];
    var indicatorHeading;
    var indices = [];
            	
    if(!tickers.includes(bmTicker) && tickers.length>0){
    	tickers.unshift(bmTicker);
    }
    
    for(var ticker in tickers){
       $.ajax({
       		url : "/api/chartData/stock/"+tickers[ticker],
            data : {'dateRange': date_range, 'bmTicker': bmTicker},
            type : 'get',
            success : function(result) {
            	var modifiedPriceDtls = JSON.parse(result).modifiedPrice;
                var priceDtls = JSON.parse(result).price;
                var volumeDtls = JSON.parse(result).volume;
                var indicatorDataDtls = JSON.parse(result).indicatorData;
                indicatorHeading = JSON.parse(result).indicatorHeading;
                dateDtls[i] = JSON.parse(result).date;
                modifiedPriceArray[i] = modifiedPriceDtls;
                priceArray[i] = priceDtls;
                volumeArray[i] = volumeDtls;
                indicatorDataArray[i] = indicatorDataDtls;
                indices = JSON.parse(result).indices;
                i++;
            },
            async : false
       });
	}
	
	var result = {priceArray: priceArray,
				  volumeArray : volumeArray,
				  modifiedPriceArray : modifiedPriceArray,
				  indicatorDataArray : indicatorDataArray,
				  dateDtls : dateDtls,
				  indicatorHeading : indicatorHeading,
				  indices : indices};
				  
	return result;
	        	
}

function getModifiedDateRange(tickers,date_range,bmTicker){
	if(!tickers.includes(bmTicker) && tickers.length>0){
    	tickers.unshift(bmTicker);
    }
    var modifiedDateRange;
     $.ajax({
       		url : "/api/chartData/stockStartDate",
            data : {'dateRange': date_range, 'tickers': tickers.toString()},
            type : 'get',
            success : function(result) {
            	modifiedDateRange = JSON.parse(result).modifiedDateRange;
            },
            async : false
       });
       
     return modifiedDateRange;
}

function getTickDifference(date_range){
	const startDate = new Date(date_range.split(" - ")[0]);
    const endDate = new Date(date_range.split(" - ")[1]);
    const diffTime = Math.abs(endDate - startDate);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
    var tickDiff;
            	
    if(diffDays<=31) {					
    	tickDiff= Math.round(diffDays/5);
	}else if(diffDays<=92) {
		tickDiff= Math.round(diffDays/7);
	}else if(diffDays<=185) {
		tickDiff= Math.round(diffDays/8);
	}else if(diffDays<=366) {
		tickDiff= Math.round(diffDays/8);
	}else if(diffDays<=731) {
		tickDiff= Math.round(diffDays/10);
	}else if(diffDays<=1096) {
		tickDiff= Math.round(diffDays/8);
	}else if(diffDays<=1461) {
		tickDiff= Math.round(diffDays/13);
	}else if(diffDays<=1827) {
		tickDiff= Math.round(diffDays/13);
	}else if(diffDays<=2557) {
		tickDiff= Math.round(diffDays/13);
	}else if(diffDays<=3386) {
		tickDiff= Math.round(diffDays/15);
	}
	
	return tickDiff;
}

function createTickerList(tickers){
	tickers.unshift('DS30');
	tickers.unshift('DSEX');
	tickers.push('LR Global Index');
	tickers.push('SND Index');
	tickers.push('FD Index');
	tickers.push('5-yr T-Bond Index');
	tickers.push('10-yr T-Bond Index');
	    	
	tickers.push('1JANATAMF NAV Index');
	tickers.push('1STPRIMFMF NAV Index');
	tickers.push('ABB1STMF NAV Index');
	tickers.push('AIBL1STIMF NAV Index');
	tickers.push('ATCSLGF NAV Index');
	tickers.push('CAPMBDBLMF NAV Index');
	tickers.push('CAPMIBBLMF NAV Index');
	tickers.push('DBH1STMF NAV Index');
	tickers.push('EBL1STMF NAV Index');
	tickers.push('EBLNRBMF NAV Index');
	tickers.push('EXIM1STMF NAV Index');
	tickers.push('FBFIF NAV Index');
	tickers.push('GRAMEENS2 NAV Index');
	tickers.push('GREENDELMF NAV Index');
	tickers.push('ICB3RDNRB NAV Index');
	tickers.push('ICBAGRANI1 NAV Index');
	tickers.push('ICBAMCL2ND NAV Index');
	tickers.push('ICBEPMF1S1 NAV Index');
	tickers.push('ICBSONALI1 NAV Index');
	tickers.push('IFIC1STMF NAV Index');
	tickers.push('IFILISLMF1 NAV Index');
	tickers.push('LRGLOBMF1 NAV Index');
	tickers.push('MBL1STMF NAV Index');
	tickers.push('NCCBLMF1 NAV Index');
	tickers.push('PF1STMF NAV Index');
	tickers.push('PHPMF1 NAV Index');
	tickers.push('POPULAR1MF NAV Index');
	tickers.push('PRIME1ICBA NAV Index');
	tickers.push('RELIANCE1 NAV Index');
	tickers.push('SEMLFBSLGF NAV Index');
	tickers.push('SEMLIBBLSF NAV Index');
	tickers.push('SEMLLECMF NAV Index');
	tickers.push('TRUSTB1MF NAV Index');
	tickers.push('VAMLBDMF1 NAV Index');
	tickers.push('VAMLRBBF NAV Index');
	    	
	tickers.push('SPX Index');
	tickers.push('NDX Index');
	tickers.push('DAX Index');
	tickers.push('ESTX50 Index');
	tickers.push('N225 Index');
	tickers.push('HSI Index');
	tickers.push('SPI Index');
	tickers.push('SENSEX Index');
	    	
	return tickers;
}