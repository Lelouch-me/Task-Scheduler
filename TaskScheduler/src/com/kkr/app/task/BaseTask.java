package com.kkr.app.task;



import org.springframework.context.ApplicationContext;

import com.kkr.app.model.ScheduledTaskModel;
import com.kkr.app.model.TaskDto;



public abstract class BaseTask implements Runnable{

	private ApplicationContext ctx;
	private TaskDto task;
	
	public ApplicationContext getCtx() {
		return ctx;
	}
	public void setCtx(ApplicationContext ctx) {
		this.ctx = ctx;
	}
	public TaskDto getTask() {
		return task;
	}
	public void setTask(TaskDto task) {
		this.task = task;
	}
}
