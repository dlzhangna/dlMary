package com.bjyt.springcloud.skiplistener;

public class CustomSkipListenerableException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private int value;
	private String message;
    public CustomSkipListenerableException() {
        super();
    }
    public CustomSkipListenerableException(String msg) {
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
