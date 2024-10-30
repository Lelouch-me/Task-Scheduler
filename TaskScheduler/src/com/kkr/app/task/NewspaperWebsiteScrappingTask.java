package com.kkr.app.task;

import com.kkr.app.service.INewspaperWebsiteScrappingService;
import com.kkr.app.service.ITaskService;
import com.kkr.common.util.Constants;

public class NewspaperWebsiteScrappingTask extends BaseTask{
	
	public void run(){
		
		ITaskService service = (ITaskService) getCtx().getBean("taskService");
		INewspaperWebsiteScrappingService newspaperWebsiteScrappingService =(INewspaperWebsiteScrappingService) getCtx()
				.getBean("newspaperWebsiteScrappingService");
		
		try{
			if(getTask().getRunType().equals(Constants.RUN_TYPE_AUTO)){
				newspaperWebsiteScrappingService.scrappAllNewsWebsite();		
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
