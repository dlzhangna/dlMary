package com.bjyt.springcloud.retry;

public class CustomRetryableException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private int value;
	private String message;
    public CustomRetryableException() {
        super();
    }
    public CustomRetryableException(String msg) {
        super(msg);
        this.message = msg;
        //this.value=value;
    }
    
    public void setMsg(String msg) {
    	this.message = msg;
    }
    public String getMsg() {
        return this.message;
    }
}
