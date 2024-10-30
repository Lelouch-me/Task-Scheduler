package com.kkr.app.service.impl;

import java.util.Date;
import java.util.List;

import com.kkr.app.dao.impl.TaskDao;
import com.kkr.app.model.ScheduledTaskModel;
import com.kkr.app.model.TaskDto;
import com.kkr.app.model.TaskLogDto;
import com.kkr.app.service.ITaskService;


public class TaskService implements ITaskService {
	
	private TaskDao taskDao;

	@Override
	public List<TaskDto> getRunnableTasks() throws Exception {		
		return taskDao.getRunnableTasks();
	}
	
	public void updateActive(int taskId, int active) throws Exception {		
		taskDao.updateActive(taskId, active);
	}
	
	@Override
	public void markTaskRunning(int taskId) throws Exception {
		taskDao.updateTask(taskId, true, null);
	}

	@Override
	public void markTaskComplete(int taskId, String status) throws Exception {
		taskDao.updateTask(taskId, false, status);
	}
	
	@Override
	public void updateLongRunningJobs() throws Exception {
		taskDao.updateLongRunningJobs();
	}
	
	@Override
	public Date getTaskTime(int taskId) throws Exception {		
		return taskDao.getTaskLastRunningTime(taskId);
	}
	
	public TaskDao getTaskDao() {
		return taskDao;
	}

	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

	public void resetForceRunForTask(int taskId) {
		taskDao.resetForceRunForTask(taskId);
	}
}
