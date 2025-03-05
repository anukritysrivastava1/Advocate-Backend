package com.advocate.exception;

import org.springframework.http.HttpStatus;

public class ErrorMessage {
	
	private HttpStatus statusDetails;
	private String messageDetails;
	
	
	public ErrorMessage() {
		super();
	}
	public ErrorMessage(HttpStatus statusDetails, String messageDetails) {
		super();
		this.statusDetails = statusDetails;
		this.messageDetails = messageDetails;
	}
	public HttpStatus getStatusDetails() {
		return statusDetails;
	}
	public void setStatusDetails(HttpStatus statusDetails) {
		this.statusDetails = statusDetails;
	}
	public String getMessageDetails() {
		return messageDetails;
	}
	public void setMessageDetails(String messageDetails) {
		this.messageDetails = messageDetails;
	}
	@Override
	public String toString() {
		return "ErrorMessage [statusDetails=" + statusDetails + ", messageDetails=" + messageDetails + "]";
	}
	
	
 
}