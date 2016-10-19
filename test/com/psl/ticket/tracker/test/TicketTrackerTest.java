package com.psl.ticket.tracker.test;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.psl.ticket.tracker.dao.TicketDAO;

public class TicketTrackerTest {
	private TicketDAO ticketDao;
	
	@Before
	public void setup(){
		ticketDao = new TicketDAO();
	}
	
	@Test
	public void findTicketsByOwnerTest(){
		Map<String, Integer> ticketOwnerToTicketsCountMap = ticketDao.findTicketsByOwner();
		assertEquals(ticketOwnerToTicketsCountMap.size(), 7);
		assertEquals(ticketOwnerToTicketsCountMap.get("Andy Thomas").intValue(), 3);
	}
	
	@Test
	public void findTicketsForSLTest(){
		Map<String, Long> openSinceMap = ticketDao.findOpenTicketsForSL();
		Assert.assertTrue(openSinceMap.size()  >= 1 );
	}
	
	@Test
	public void findAssigneeWithHighestDaysTest(){
		String assignee = ticketDao.findAssigneeWithHighestDays();
		assertEquals(assignee, "Arun Kulkarni");
	}
	
	@Test
	public void findAssigneeWithMixTicketsResolvedTest(){

		String assignee = ticketDao.findAssigneeWithMixTicketsResolved();
		assertEquals(assignee, "Arun Kulkarni");
	}
}
