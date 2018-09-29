package com.keendo.architecture.exception;

public class BaseException extends RuntimeException {
	private static final long serialVersionUID = 8326849298723586892L;

	/**
     * message key
     */
    private int code;

    /**
     * message params
     */
    private Object[] values;

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return the values
     */
    public Object[] getValues() {
        return values;
    }

    /**
     * @param values the values to set
     */
    public void setValues(Object[] values) {
        this.values = values;
    }

    public BaseException(String message, Throwable cause, int code, Object[] values) {
        super(message, cause);
        this.code = code;
        this.values = values;
    }
    
    public BaseException(String message, Throwable cause) {
    	super(message, cause);
    }
}