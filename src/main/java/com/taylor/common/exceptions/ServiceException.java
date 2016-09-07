package com.taylor.common.exceptions;

import com.taylor.common.lang.ErrorCode;

/**
 * @author HaydenWang
 *
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 415436633263046104L;
	private ErrorCode errorCode;

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(ErrorCode errorCode) {
		super(errorCode.getMsg());
		this.errorCode = errorCode;
	}

	public ServiceException(ErrorCode errorCode, String msg) {
		super(msg);
		this.errorCode = errorCode;
	}

	public ServiceException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMsg(), cause);
		this.errorCode = errorCode;
	}

	public ServiceException(ErrorCode errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCode = errorCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

}
