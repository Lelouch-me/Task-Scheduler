package com.kkr.app.task;

import com.kkr.app.service.IMarketDataPullService;
import com.kkr.app.service.ITaskService;
import com.kkr.common.util.Constants;

public class MarketDataPullTask extends BaseTask{
	
	public void run(){
		
		ITaskService service = (ITaskService) getCtx().getBean("taskService");
		IMarketDataPullService marketDataPullService =(IMarketDataPullService) getCtx().getBean("marketDataPullService");	
		try{
			if(getTask().getRunType().equals(Constants.RUN_TYPE_AUTO)){
				marketDataPullService.pullMarketData();		
			    service.markTaskComplete(getTask().getTaskId(), "SUCCESS");
			}
		}catch(Exception e){
			try {
				service.markTaskComplete(getTask().getTaskId(), "ERROR");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
}
