package com.kkr.web.servlet;


import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.Map.Entry;
import java.util.Timer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.kkr.app.model.TaskDto;
import com.kkr.app.service.ITaskService;
import com.kkr.app.task.BaseTask;
import com.kkr.common.util.CommonUtils;
import com.kkr.common.util.Constants;

@SuppressWarnings("serial")
public class InitServlet extends HttpServlet {
	public static Map<Integer, BaseTask> baseTaskMap = new HashMap<Integer, BaseTask>();
   
    public InitServlet() {
        // TODO Auto-generated constructor stub
    	super();
    	System.out.println("Init Servlet Constructor..");
    }

	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		Timer timer=new Timer("Init Task", true);
		timer.scheduleAtFixedRate(new TaskThread(), 0, 5*60*1000);
		(new Thread(new RunnableTaskThread())).start();				
	}
	
	public void destroy() {
		// TODO Auto-generated method stub
		super.destroy();
	}

	public ServletConfig getServletConfig() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getServletInfo() {
		// TODO Auto-generated method stub
		return null; 
	}
	
	public void service(ServletRequest request, ServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("Service Initialized OK");
	}
	
	private class TaskThread extends TimerTask {

		@Override
		public void run() {
			ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml"); 
			ITaskService service = (ITaskService) context.getBean("taskService");
			try {
				for(TaskDto task : service.getRunnableTasks()) {
					BaseTask thisTask = (BaseTask) Class.forName(task.getExecuterClass()).newInstance();
					thisTask.setCtx(context);
					thisTask.setTask(task);
					baseTaskMap.put(task.getTaskId(), thisTask);
				}
			} catch (Exception ex) {
				System.out.println("Error while starting runnable tasks: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}
		
	private class RunnableTaskThread implements Runnable{

		@Override
		public void run() {
			ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml"); 
			ITaskService service = (ITaskService) context.getBean("taskService");
			while(true) {
				for(Entry<Integer, BaseTask> bTMEntry : baseTaskMap.entrySet()) {
					try {
						int tId = bTMEntry.getKey();
						BaseTask bTask = bTMEntry.getValue();
						TaskDto bTDTO = bTask.getTask();
						if(bTDTO != null && !bTDTO.getLastRunStatus().equals(Constants.SCHED_STAT_RUNNING)) {
							if(bTDTO.getForce_run()) {
								new Thread(bTask).start();
								markTaskRun(tId, service, bTDTO.getRunType());
								service.resetForceRunForTask(tId);
							}
							String[] skipDays = bTDTO.getSkipDays() != null ? bTDTO.getSkipDays().split(",") : new String[0];
							Calendar c = Calendar.getInstance();
							c.setTime(new Date());
							Boolean flagToRun = true;
							for (String day : skipDays) {
								if(c.get(Calendar.DAY_OF_WEEK) == Long.parseLong(day)) {
									flagToRun = false;
									break;
								}
							}
							
							if(CommonUtils.isHoliday() && tId!=4 && tId!=7 && tId!=9) flagToRun = false;
							
							if(bTDTO.getCheckMarketOpen()) {
								flagToRun = CommonUtils.isMarketOpen();
							}
							
							if(bTDTO.getSchedule_type().equalsIgnoreCase(Constants.Scheduled_continuous) && flagToRun) {
								if(((new Date().getTime() - (CommonUtils.formatDate(bTDTO.getLastRunCompleteTime(), 
										Constants.DATETIME_MYSQL_FORMAT).getTime()))/(1000*60)) 
										>= bTDTO.getFrequencyMinutes()) {
									new Thread(bTask).start();
									markTaskRun(tId, service, bTDTO.getRunType());
								}
							}
							if(bTDTO.getSchedule_type().equalsIgnoreCase(Constants.Scheduled_Daily)) {
								if(flagToRun) {
									Calendar cScheduled = Calendar.getInstance();
									cScheduled.setTime(new Date());
									cScheduled.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[0]));
									cScheduled.set(Calendar.MINUTE, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[1]));
									cScheduled.set(Calendar.SECOND, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[2]));

									Calendar cal = Calendar.getInstance();
									cal.setTime(CommonUtils.formatDate(bTDTO.getLastRunStartTime(), Constants.DATETIME_MYSQL_FORMAT));
									cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[0]));
									cal.set(Calendar.MINUTE, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[1]));
									cal.set(Calendar.SECOND, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[2]));
									
									if(CommonUtils.getDaysDifference(new Date(), cal.getTime()) > 1 
											&& (new Date().getTime() > cScheduled.getTimeInMillis())){
										new Thread(bTask).start();
										markTaskRun(tId, service, bTDTO.getRunType());
									}
								}
							}
							if(bTDTO.getSchedule_type().equalsIgnoreCase(Constants.Scheduled_Weekly)) {
								String dayOfWeek = bTDTO.getDayOfWeek();
								Calendar cal = Calendar.getInstance();
								cal.setTime(new Date());
								Calendar cScheduled = Calendar.getInstance();
								cScheduled.setTime(new Date());
								cScheduled.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[0]));
								cScheduled.set(Calendar.MINUTE, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[1]));
								cScheduled.set(Calendar.SECOND, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[2]));
								if((cal.get(Calendar.DAY_OF_WEEK) == Long.parseLong(dayOfWeek)) 
										&& CommonUtils.getDaysDifference(new Date(), (CommonUtils.formatDate(bTDTO.getLastRunStartTime(), 
												Constants.DATETIME_MYSQL_FORMAT))) > 1 
										&& (new Date().getTime() > cScheduled.getTimeInMillis())){
									new Thread(bTask).start();
									markTaskRun(tId, service, bTDTO.getRunType());
								}
							}
							if(bTDTO.getSchedule_type().equalsIgnoreCase(Constants.Scheduled_Monthly)) {
								String scheduleDay = bTDTO.getSchdeuledDate();
								Calendar cal = Calendar.getInstance();
								cal.setTime(new Date());
							    cal.set(Calendar.DAY_OF_WEEK, Integer.parseInt(scheduleDay));
							    cal.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
							    Calendar cToday = Calendar.getInstance();
							    cToday.setTime(new Date());
							    Calendar cScheduled = Calendar.getInstance();
								cScheduled.setTime(new Date());
								cScheduled.set(Calendar.HOUR_OF_DAY, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[0]));
								cScheduled.set(Calendar.MINUTE, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[1]));
								cScheduled.set(Calendar.SECOND, Integer.parseInt(bTDTO.getSchdeuledTime().split(":")[2]));
								if((cToday.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DATE)) 
										&& CommonUtils.getDaysDifference(new Date(), (CommonUtils.formatDate(bTDTO.getLastRunStartTime(), 
												Constants.DATETIME_MYSQL_FORMAT))) > 1 
										&& (new Date().getTime() > cScheduled.getTimeInMillis())){
									new Thread(bTask).start();
									markTaskRun(tId, service, bTDTO.getRunType());
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private void markTaskRun(int tId, ITaskService service, String runType){
		try {
			service.markTaskRunning(tId);
		} catch (Exception ex) {
			try {
				service.markTaskComplete(tId, "ERROR");
				ex.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
