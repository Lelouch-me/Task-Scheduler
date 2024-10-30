package com.kkr.app.task;

import com.kkr.app.service.IMarketMoverDataCalculateService;
import com.kkr.app.service.ITaskService;
import com.kkr.common.util.Constants;

public class MarketMoverDataCalculateTask extends BaseTask{
	
	public void run(){
		
		ITaskService service = (ITaskService) getCtx().getBean("taskService");
		IMarketMoverDataCalculateService moversDataCalculateService =(IMarketMoverDataCalculateService) getCtx().getBean("marketMoverDataCalculateService");
		
		try{
			if(getTask().getRunType().equals(Constants.RUN_TYPE_AUTO)){
				moversDataCalculateService.calculateMoversData();		
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
