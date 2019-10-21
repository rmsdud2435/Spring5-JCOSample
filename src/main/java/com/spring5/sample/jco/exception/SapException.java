package com.spring5.sample.jco.exception;

public class SapException extends Exception{
	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String errorMessage;
	private Throwable throwable;

	public SapException(Throwable throwable)
	{
		this("", "", throwable);
	}

	public SapException(String errorCode, String errorMessage) {
		this(errorCode, errorMessage, new Exception(errorMessage));
	}

	public SapException(String errorCode, String errorMessage, Throwable throwable)
	{
		super(throwable);
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.throwable = throwable;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(String errorCode) { 
		this.errorCode = errorCode; 
	}

	public String getErrorMessage() {
		return this.errorMessage;
	}

	public void setErrorMessage(String errorMessage) { 
		this.errorMessage = errorMessage; 
	}

	public Throwable getThrowable()
	{
		return this.throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
}
