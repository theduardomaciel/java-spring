package dev.theduardomaciel.javaspring.handler;

import org.springframework.http.HttpStatus;

import java.util.Date;

public class ResponseError {
	private Date timestamp = new Date();
	private String error;
	private String status = "error";
	private final int statusCode;
	
	public ResponseError(String error, String message, int statusCode) {
		this.error = message;
		this.statusCode = statusCode;
	}
	
	public Date getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public String getError() {
		return error;
	}
	
	public void setError(String error) {
		this.error = error;
	}
}
