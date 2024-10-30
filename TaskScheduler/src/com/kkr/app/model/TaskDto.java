package com.kkr.app.model;

import java.util.Date;

public class TaskDto {

	private int taskId;
	private String executerClass;
	private int frequencyMinutes;
	private String schedule_type;
	private Boolean force_run;
	private Boolean run_on_start_up;
	private String dayOfWeek;
	private String schdeuledDate;
	private String schdeuledTime;
	private Boolean notifyViaMail;
	private String dataLoadingStatus;
	private String dataMovementStatus;
	private String lastRunCompleteTime;
	private String lastRunStartTime;
	private String runType;
	private String skipDays;
	private String lastRunStatus;
	private Date dataLoadingStart;
	private Date dataMovementStart;
	private Date dataLoadingEnd;
	private Date dataMovementEnd;
	private Boolean checkMarketOpen;

	public TaskDto(int taskId, String executerClass, int frequencyMinutes, String schedule_type, Boolean force_run, 
			Boolean run_on_start_up, String dayOfWeek, String schdeuledDate, String schdeuledTime, Boolean notifyViaMail, 
			String lastRunCompleteTime, String lastRunStartTime, String runType, String skipDays, String lastRunStatus,Boolean checkMarketOpen) {
		super();
		this.taskId = taskId;
		this.executerClass = executerClass;
		this.frequencyMinutes = frequencyMinutes;
		this.schedule_type = schedule_type;
		this.force_run = force_run;
		this.run_on_start_up = run_on_start_up;
		this.dayOfWeek = dayOfWeek;
		this.schdeuledDate = schdeuledDate;
		this.schdeuledTime = schdeuledTime;
		this.notifyViaMail = notifyViaMail;
//		this.dataLoadingStatus = dataLoadingStatus;
//		this.dataMovementStatus = dataMovementStatus;
		this.lastRunCompleteTime = lastRunCompleteTime;
		this.lastRunStartTime = lastRunStartTime;
		this.runType = runType;
		this.skipDays = skipDays;
		this.lastRunStatus = lastRunStatus;
//		this.dataLoadingStart = dataLoadingStart;
//		this.dataMovementStart = dataMovementStart;
//		this.dataLoadingEnd = dataLoadingEnd;
//		this.dataMovementEnd = dataMovementEnd;
		this.checkMarketOpen = checkMarketOpen;
	}

	public Boolean getCheckMarketOpen() {
		return checkMarketOpen;
	}

	public void setCheckMarketOpen(Boolean checkMarketOpen) {
		this.checkMarketOpen = checkMarketOpen;
	}

	public Date getDataMovementEnd() {
		return dataMovementEnd;
	}

	public void setDataMovementEnd(Date dataMovementEnd) {
		this.dataMovementEnd = dataMovementEnd;
	}

	public Date getDataLoadingEnd() {
		return dataLoadingEnd;
	}

	public void setDataLoadingEnd(Date dataLoadingEnd) {
		this.dataLoadingEnd = dataLoadingEnd;
	}

	public Date getDataMovementStart() {
		return dataMovementStart;
	}

	public void setDataMovementStart(Date dataMovementStart) {
		this.dataMovementStart = dataMovementStart;
	}

	public Date getDataLoadingStart() {
		return dataLoadingStart;
	}

	public void setDataLoadingStart(Date dataLoadingStart) {
		this.dataLoadingStart = dataLoadingStart;
	}

	public int getTaskId() {
		return taskId;
	}
	
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	
	public String getExecuterClass() {
		return executerClass;
	}
	
	public void setExecuterClass(String executerClass) {
		this.executerClass = executerClass;
	}
	public int getFrequencyMinutes() {
		return frequencyMinutes;
	}
	
	public void setFrequencyMinutes(int frequencyMinutes) {
		this.frequencyMinutes = frequencyMinutes;
	}

	public String getSchedule_type() {
		return schedule_type;
	}

	public void setSchedule_type(String scheduleType) {
		schedule_type = scheduleType;
	}

	public Boolean getForce_run() {
		return force_run;
	}

	public void setForce_run(Boolean forceRun) {
		force_run = forceRun;
	}

	public Boolean getRun_on_start_up() {
		return run_on_start_up;
	}

	public void setRun_on_start_up(Boolean runOnStartUp) {
		run_on_start_up = runOnStartUp;
	}

	public Boolean getNotifyViaMail() {
		return notifyViaMail;
	}

	public void setNotifyViaMail(Boolean notifyViaMail) {
		this.notifyViaMail = notifyViaMail;
	}

	public String getDataLoadingStatus() {
		return dataLoadingStatus;
	}

	public void setDataLoadingStatus(String dataLoadingStatus) {
		this.dataLoadingStatus = dataLoadingStatus;
	}

	public String getDataMovementStatus() {
		return dataMovementStatus;
	}

	public void setDataMovementStatus(String dataMovementStatus) {
		this.dataMovementStatus = dataMovementStatus;
	}

	public String getLastRunCompleteTime() {
		return lastRunCompleteTime;
	}

	public void setLastRunCompleteTime(String lastRunCompleteTime) {
		this.lastRunCompleteTime = lastRunCompleteTime;
	}

	public String getLastRunStartTime() {
		return lastRunStartTime;
	}

	public void setLastRunStartTime(String lastRunStartTime) {
		this.lastRunStartTime = lastRunStartTime;
	}

	public String getDayOfWeek() {
		return dayOfWeek;
	}

	public void setDayOfWeek(String dayOfWeek) {
		this.dayOfWeek = dayOfWeek;
	}

	public String getSchdeuledDate() {
		return schdeuledDate;
	}

	public void setSchdeuledDate(String schdeuledDate) {
		this.schdeuledDate = schdeuledDate;
	}

	public String getSchdeuledTime() {
		return schdeuledTime;
	}

	public void setSchdeuledTime(String schdeuledTime) {
		this.schdeuledTime = schdeuledTime;
	}

	public String getRunType() {
		return runType;
	}

	public void setRunType(String runType) {
		this.runType = runType;
	}
	
	public String getSkipDays() {
		return skipDays;
	}

	public void setSkipDays(String skipDays) {
		this.skipDays = skipDays;
	}

	public String getLastRunStatus() {
		return lastRunStatus;
	}

	public void setLastRunStatus(String lastRunStatus) {
		this.lastRunStatus = lastRunStatus;
	}
	
	@Override
	public String toString() {
		return "TaskDto [taskId=" + taskId + ", executerClass=" + executerClass	+ ", frequencyMinutes=" + frequencyMinutes + "]";
	}
}
