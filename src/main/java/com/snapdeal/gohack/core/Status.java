package com.snapdeal.gohack.core;

public class Status {
	
	public boolean Status;
	
	public Status() {
		Status=Boolean.TRUE;
	}
	
	public String message;

	public boolean isStatus() {
		return Status;
	}

	public void setStatus(boolean status) {
		Status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	

}
