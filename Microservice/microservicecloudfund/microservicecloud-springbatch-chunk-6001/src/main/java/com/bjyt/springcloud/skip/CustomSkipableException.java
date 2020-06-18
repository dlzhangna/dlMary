package com.bjyt.springcloud.skip;

public class CustomSkipableException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private int value;
	private String message;
    public CustomSkipableException() {
        super();
    }
    public CustomSkipableException(String msg) {
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
