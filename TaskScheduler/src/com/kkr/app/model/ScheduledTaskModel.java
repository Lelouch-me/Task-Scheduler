package com.kkr.app.model;

import java.util.Date;

public class ScheduledTaskModel {

	private int task_id;
	private String name;
	private String executer_class;
	private int active;
	private int frequency_minutes;
	private String last_run_start;
	private String last_run_complete;
	private String last_run_status;
	private String schedule_type;
	private int parent_id;
	private boolean force_run;
	private int run_on_start_up;
	private String scheduled_time;
	private String scheduled_date;
	private String scheduled_day;
	private String skip_on_days;
	private int notify_via_mail;
	private Date data_loading_start;
	private Date data_loading_end;
	private String data_loading_status;
	private Date data_movement_start;
	private Date data_movement_end;
	private String data_movement_status;
	private String run_type;
	private int check_market_open;
	
	public int getTask_id() {
		return task_id;
	}
	public String getName() {
		return name;
	}
	public String getExecuter_class() {
		return executer_class;
	}
	public int getActive() {
		return active;
	}
	public int getFrequency_minutes() {
		return frequency_minutes;
	}
	public String getLast_run_start() {
		return last_run_start;
	}
	public String getLast_run_complete() {
		return last_run_complete;
	}
	public String getLast_run_status() {
		return last_run_status;
	}
	public String getSchedule_type() {
		return schedule_type;
	}
	public int getParent_id() {
		return parent_id;
	}
	public boolean getForce_run() {
		return force_run;
	}
	public int getRun_on_start_up() {
		return run_on_start_up;
	}
	public String getScheduled_time() {
		return scheduled_time;
	}
	public String getScheduled_date() {
		return scheduled_date;
	}
	public String getScheduled_day() {
		return scheduled_day;
	}
	public String getSkip_on_days() {
		return skip_on_days;
	}
	public int getNotify_via_mail() {
		return notify_via_mail;
	}
	public Date getData_loading_start() {
		return data_loading_start;
	}
	public Date getData_loading_end() {
		return data_loading_end;
	}
	public String getData_loading_status() {
		return data_loading_status;
	}
	public Date getData_movement_start() {
		return data_movement_start;
	}
	public Date getData_movement_end() {
		return data_movement_end;
	}
	public String getData_movement_status() {
		return data_movement_status;
	}
	public String getRun_type() {
		return run_type;
	}
	public int getCheck_market_open() {
		return check_market_open;
	}
	public void setTask_id(int task_id) {
		this.task_id = task_id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public void setExecuter_class(String executer_class) {
		this.executer_class = executer_class;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public void setFrequency_minutes(int frequency_minutes) {
		this.frequency_minutes = frequency_minutes;
	}
	public void setLast_run_start(String last_run_start) {
		this.last_run_start = last_run_start;
	}
	public void setLast_run_complete(String last_run_complete) {
		this.last_run_complete = last_run_complete;
	}
	public void setLast_run_status(String last_run_status) {
		this.last_run_status = last_run_status;
	}
	public void setSchedule_type(String schedule_type) {
		this.schedule_type = schedule_type;
	}
	public void setParent_id(int parent_id) {
		this.parent_id = parent_id;
	}
	public void setForce_run(boolean force_run) {
		this.force_run = force_run;
	}
	public void setRun_on_start_up(int run_on_start_up) {
		this.run_on_start_up = run_on_start_up;
	}
	public void setScheduled_time(String scheduled_time) {
		this.scheduled_time = scheduled_time;
	}
	public void setScheduled_date(String scheduled_date) {
		this.scheduled_date = scheduled_date;
	}
	public void setScheduled_day(String scheduled_day) {
		this.scheduled_day = scheduled_day;
	}
	public void setSkip_on_days(String skip_on_days) {
		this.skip_on_days = skip_on_days;
	}
	public void setNotify_via_mail(int notify_via_mail) {
		this.notify_via_mail = notify_via_mail;
	}
	public void setData_loading_start(Date data_loading_start) {
		this.data_loading_start = data_loading_start;
	}
	public void setData_loading_end(Date data_loading_end) {
		this.data_loading_end = data_loading_end;
	}
	public void setData_loading_status(String data_loading_status) {
		this.data_loading_status = data_loading_status;
	}
	public void setData_movement_start(Date data_movement_start) {
		this.data_movement_start = data_movement_start;
	}
	public void setData_movement_end(Date data_movement_end) {
		this.data_movement_end = data_movement_end;
	}
	public void setData_movement_status(String data_movement_status) {
		this.data_movement_status = data_movement_status;
	}
	public void setRun_type(String run_type) {
		this.run_type = run_type;
	}
	public void setCheck_market_open(int check_market_open) {
		this.check_market_open = check_market_open;
	}
	
}
