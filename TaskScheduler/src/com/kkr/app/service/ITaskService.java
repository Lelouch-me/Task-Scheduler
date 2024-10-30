package com.kkr.app.service;

import java.util.Date;
import java.util.List;

import com.kkr.app.model.ScheduledTaskModel;
import com.kkr.app.model.TaskDto;
import com.kkr.app.model.TaskLogDto;

public interface ITaskService {

	public List<TaskDto> getRunnableTasks() throws Exception;
	public void markTaskRunning(int taskId) throws Exception;
	public void markTaskComplete(int taskId, String status) throws Exception;
	public void updateLongRunningJobs() throws Exception;
	public Date getTaskTime(int taskId) throws Exception;
	public void resetForceRunForTask(int taskId);
	void updateActive(int taskId, int active) throws Exception;
}
