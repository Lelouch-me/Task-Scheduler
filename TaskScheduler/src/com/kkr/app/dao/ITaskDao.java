package com.kkr.app.dao;

import java.util.Date;
import java.util.List;

import com.kkr.app.model.TaskDto;
import com.kkr.app.model.TaskLogDto;

public interface ITaskDao {

	public List<TaskDto> getRunnableTasks() throws Exception;
	public void updateTask(long taskId, boolean isRunning, String finalStatus, Boolean isLoading) throws Exception;
	public void updateLongRunningJobs() throws Exception;
	public Date getTaskLastRunningTime(long taskId) throws Exception;
	public void logTaskStatus(TaskLogDto taskLogDto) throws Exception;
	public void resetForceRunForTask(long taskId);
	void updateActive(long taskId, int active) throws Exception;
	List<Date> getHolidaysList() throws Exception;
}
