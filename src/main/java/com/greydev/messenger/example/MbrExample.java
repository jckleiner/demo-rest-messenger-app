package com.greydev.messenger.example;

import java.util.Calendar;
import java.util.Date;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("messageBodyWriter/")
@Produces(MediaType.TEXT_PLAIN)
public class MbrExample {

	@GET
	public Date getMyDate() {
		return Calendar.getInstance().getTime();
	}

}