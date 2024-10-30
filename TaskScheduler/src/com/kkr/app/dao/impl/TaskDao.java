package com.kkr.app.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kkr.app.model.TaskDto;
import com.kkr.app.model.TaskLogDto;
import com.kkr.app.task.BaseTask;
import com.kkr.common.util.CommonUtils;
import com.kkr.common.util.Constants;
import com.kkr.web.servlet.InitServlet;

public class TaskDao {

	public List<TaskDto> getRunnableTasks() throws Exception {
		List<TaskDto> runnableTask = new ArrayList<TaskDto>();
		Connection con = CommonUtils.connectDC();
		String sql = "select task_id, name, executer_class, frequency_minutes, last_run_start, last_run_complete, last_run_status, schedule_type, "
        		+ "force_run, run_on_start_up, scheduled_time, scheduled_date, scheduled_day, skip_on_days, notify_via_mail, run_type, check_market_open "
        		+ "from dse_analyzer_loader.scheduled_task where active = 1";
        if(con != null) {
	        Statement stmt=(Statement) con.createStatement();  
	        ResultSet rs=stmt.executeQuery(sql);
	        while(rs.next()) {
        		TaskDto taskDto = new TaskDto(rs.getInt("task_id"), rs.getString("executer_class"), rs.getInt("frequency_minutes"), 
        				rs.getString("schedule_type"), rs.getBoolean("force_run"), rs.getBoolean("run_on_start_up"), 
        				rs.getString("scheduled_day"), rs.getString("scheduled_date"), rs.getString("scheduled_time"), rs.getBoolean("notify_via_mail"), 
        				rs.getString("last_run_complete"), rs.getString("last_run_start"), rs.getString("run_type"),rs.getString("skip_on_days"),
        				rs.getString("last_run_status"), rs.getBoolean("check_market_open"));
        		runnableTask.add(taskDto);
	        }
	        con.close();
        } else {
        	System.out.println("Error while creating database connection");
        }
		return runnableTask;
	}
	
	public void updateTask(int taskId, boolean isRunning, String finalStatus) throws Exception {
		String query = null;
		Connection con = CommonUtils.connectDC();
		String lRStatus = null;
		Date lRStart = null;
		Date lRComplete = null;
		if(con != null) {
			if(isRunning) {
				query = "update dse_analyzer_loader.scheduled_task set last_run_start = now(), last_run_status = '" + Constants.SCHED_STAT_RUNNING +
						"' where task_id = " + taskId;
				lRStart = new Date();
				lRStatus = Constants.SCHED_STAT_RUNNING;
			} else {
				query = "update dse_analyzer_loader.scheduled_task set last_run_complete = now(), last_run_status = '" + finalStatus +
						"' where task_id = " + taskId;
				lRComplete = new Date();
				lRStatus = finalStatus;
			}
			Statement stmt=(Statement) con.createStatement();  
	        stmt.executeUpdate(query);
	        con.close();
	        BaseTask bTask = InitServlet.baseTaskMap.get(taskId);
	        TaskDto tDTO = bTask.getTask();
	        if(lRStatus != null)
	        	tDTO.setLastRunStatus(lRStatus);
	        if(lRStart != null)
	        	tDTO.setLastRunStartTime(CommonUtils.formatDate(lRStart, Constants.DATETIME_MYSQL_FORMAT));
	        if(lRComplete != null)
	        	tDTO.setLastRunCompleteTime(CommonUtils.formatDate(lRComplete, Constants.DATETIME_MYSQL_FORMAT));
	        bTask.setTask(tDTO);
	        InitServlet.baseTaskMap.put(taskId, bTask);
		} else {
			System.out.println("Error while creating database connection");
		}
	}

	public void updateLongRunningJobs() throws Exception {
		Connection con = CommonUtils.connectDC();
		String updateQuery = "update dse_analyzer_loader.scheduled_task set last_run_status = '" + Constants.SCHED_STAT_TERMINATED + "' where schedule_type = 'continuous' and timestampdiff(minute,last_run_start,now()) > " + Constants.JOB_EXPIRE_MINUTES;
		Statement stmt=(Statement) con.createStatement();  
        stmt.executeUpdate(updateQuery);
        con.close();
	}

	public Date getTaskLastRunningTime(int taskId) throws Exception {
		Date lastCompletedTime = null;
		Connection con = CommonUtils.connectDC();
		String sql = "select last_run_complete from dse_analyzer_loader.scheduled_task where active = 1 and task_id = "+taskId;
		Statement stmt=(Statement) con.createStatement();
		ResultSet rs=stmt.executeQuery(sql);
		 while(rs.next()) {
			 lastCompletedTime = rs.getDate(0); 
		 }
		 con.close();
		return lastCompletedTime;
	}
	
	public void resetForceRunForTask(int taskId) {
		try {
			Connection con = CommonUtils.connectDC();
			String updateQuery = "update dse_analyzer_loader.scheduled_task set force_run = 0 where task_id = " + taskId;
			Statement stmt=(Statement) con.createStatement();  
	        stmt.executeUpdate(updateQuery);
	        BaseTask bTask = InitServlet.baseTaskMap.get(taskId);
		    TaskDto tDTO = bTask.getTask();
		    tDTO.setForce_run(false);
	        con.close();
		} catch(Exception e) {
			System.out.println("Error occured while reseting force run for task: " + taskId);
		}
	}
	
	public void updateActive(int taskId, int active) throws Exception {
		Connection con = CommonUtils.connectDC();
		String updateQuery = "update dse_analyzer_loader.scheduled_task set active = "+active+" where task_id = " + taskId;
		Statement stmt=(Statement) con.createStatement();  
        stmt.executeUpdate(updateQuery);
		BaseTask bTask = InitServlet.baseTaskMap.get(taskId);
	    TaskDto tDTO = bTask.getTask();
	    tDTO.setLastRunStatus("0");
	    con.close();
	}
}
