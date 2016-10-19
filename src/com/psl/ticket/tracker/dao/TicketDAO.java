package com.psl.ticket.tracker.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.psl.ticket.tracker.util.JdbcHelper;

public class TicketDAO {
	private Connection connection = null;
	private Statement statement = null;
	private ResultSet resultSet = null;
	
	public TicketDAO() { }
	
	public Map<String, Integer> findTicketsByOwner(){
		String sql = "SELECT * FROM TICKET_MASTER;";
		Map<String, Integer> ticketOwnerToTicketsCountMap = new HashMap<String, Integer>();
		try { 
			connection = JdbcHelper.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while(resultSet.next()){
				String name = resultSet.getString("TICKET_OWNER");
				if(ticketOwnerToTicketsCountMap.containsKey(name)){
					Integer currentCount = ticketOwnerToTicketsCountMap.get(name);
					ticketOwnerToTicketsCountMap.put(name, ++currentCount);
				} else {
					ticketOwnerToTicketsCountMap.put(name, 1);
				}
			}
			int maxTicketsCount = Collections.max(ticketOwnerToTicketsCountMap.values());
	        for (Entry<String, Integer> entry : ticketOwnerToTicketsCountMap.entrySet()) {
	            if (entry.getValue() == maxTicketsCount) {
	                System.out.println("Owner Name: " + entry.getKey() + " :: " + "Ticket Count : " + entry.getValue());
	            }
	        }
		} catch (SQLException e) {
			System.out.println("Error: Cannot execute query, closing JDBC objects. " + e.getMessage());
			closeJdbcObjects();
		} finally {
			closeJdbcObjects();
		}
		return ticketOwnerToTicketsCountMap;
	}
	
	public Map<String, Long> findOpenTicketsForSL(){
		String sql = "SELECT * FROM TICKET_MASTER WHERE ASSIGNED_TO_TEAM = 'SL';";
		Map<String, Long> openSinceMap = new HashMap<String, Long>();
		try { 
			connection = JdbcHelper.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while(resultSet.next()){
				if(null == resultSet.getDate("RESOLVED_DATE")){
					String createdDate = resultSet.getString("CREATED_DATE");
					String ticketNo = resultSet.getString("TICKET_ID");
					long openSinceDays = getOpenSinceDays(createdDate);
					if(openSinceDays > 2) {
						openSinceMap.put(ticketNo, openSinceDays);						
					}
				}
			}
		} catch (SQLException e) {
			System.out.println("Error: Cannot execute query, closing JDBC objects. " + e.getMessage());
			closeJdbcObjects();
		} finally {
			closeJdbcObjects();
		}
		return openSinceMap;
	}
	
	public String findAssigneeWithHighestDays(){
		String sql_select_all = "SELECT * FROM TICKET_MASTER WHERE TICKET_TYPE = 'Development';";
		String sql_select_one = "SELECT ASSIGNEE FROM TICKET_MASTER WHERE TICKET_ID = '%s'";
		Map<Long, String> openSinceMap = new HashMap<Long, String>();
		String assignee = "";
		long workInProgressDays = 0;
		try { 
			connection = JdbcHelper.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql_select_all);
			while(resultSet.next()){
				String ticketNo = resultSet.getString("TICKET_ID");
				String createdDate = resultSet.getString("CREATED_DATE");
				String resolvedDate = resultSet.getString("RESOLVED_DATE");
				if(null == resolvedDate){
					workInProgressDays = getOpenSinceDays(createdDate);
				} else {
					workInProgressDays = getDifference(createdDate, resolvedDate);
				}
				openSinceMap.put(workInProgressDays, ticketNo);
			}
			Set<Long> noOfDays = openSinceMap.keySet();
			long highestDays = Collections.max(noOfDays);
			String ticketWithHighestDays = openSinceMap.get(highestDays);
			sql_select_one = String.format(sql_select_one, ticketWithHighestDays);
			resultSet = statement.executeQuery(sql_select_one);
			while(resultSet.next()){
				assignee = resultSet.getString("ASSIGNEE");
			}
		} catch (SQLException e) {
			System.out.println("Error: Cannot execute query, closing JDBC objects. " + e.getMessage());
			closeJdbcObjects();
		} finally {
			closeJdbcObjects();
		}
		return assignee;
	}
	
	public String findAssigneeWithMixTicketsResolved(){
		String sql = "SELECT * FROM TICKET_MASTER WHERE TICKET_STATUS = 'RESOLVED';";
		Map<String, Integer> ticketAssigneeToTicketsCountMap = new HashMap<String, Integer>();
		String assignee = "";
		try { 
			connection = JdbcHelper.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);
			while(resultSet.next()){
				String name = resultSet.getString("ASSIGNEE");
				if(ticketAssigneeToTicketsCountMap.containsKey(name)){
					Integer currentCount = ticketAssigneeToTicketsCountMap.get(name);
					ticketAssigneeToTicketsCountMap.put(name, ++currentCount);
				} else {
					ticketAssigneeToTicketsCountMap.put(name, 1);
				}
			}
			int maxTicketsCount = Collections.max(ticketAssigneeToTicketsCountMap.values());
	        for (Entry<String, Integer> entry : ticketAssigneeToTicketsCountMap.entrySet()) {
	            if (entry.getValue() == maxTicketsCount) {
	                System.out.println("Assignee Name: " + entry.getKey() + " :: " + "Resolved Ticket Count : " + entry.getValue());
	                assignee = entry.getKey();
	            }
	        }
		} catch (SQLException e) {
			System.out.println("Error: Cannot execute query, closing JDBC objects. " + e.getMessage());
			closeJdbcObjects();
		} finally {
			closeJdbcObjects();
		}
		return assignee;
	}
	
	private long getOpenSinceDays(String createdDateString){
		long openSinceDays = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date createdDate = df.parse(createdDateString);
			long createdTimestamp = createdDate.getTime();
			long currentTimestamp = new Date().getTime();
			openSinceDays = (currentTimestamp - createdTimestamp) / 86400000;
		} catch (ParseException e) {
			System.out.println("Error: Cannot parse date. " + e.getMessage());
		}
		return Math.abs(openSinceDays);
	}
	
	private long getDifference(String startDate, String endDate){
		long openSinceDays = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date start = df.parse(startDate);
			Date end = df.parse(endDate);
			long startstamp = start.getTime();
			long endTimestamp = end.getTime();
			openSinceDays = (endTimestamp - startstamp) / 86400000;
		} catch (ParseException e) {
			System.out.println("Error: Cannot parse date. " + e.getMessage());
		}
		return Math.abs(openSinceDays);
	}
	
	private void closeJdbcObjects(){
		JdbcHelper.close(resultSet);
		JdbcHelper.close(statement);
		JdbcHelper.close(connection);
	}

}
