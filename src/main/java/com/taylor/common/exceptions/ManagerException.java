package com.taylor.common.exceptions;

import com.taylor.common.lang.ErrorCode;
import com.taylor.common.lang.ErrorCodeEntity;

/**
 * 
 * 管理服务异常
 * 
 * @author lenovo
 *
 */
public class ManagerException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3607186052717323589L;
	private ErrorCode errorCodeObject;

	public ManagerException(int errorCode) {
		super();
		this.errorCodeObject = new ErrorCodeEntity(errorCode, null);
	}

	public ManagerException(int errorCode, String msg) {
		super(msg);
		this.errorCodeObject = new ErrorCodeEntity(errorCode, msg);
	}

	public ManagerException(ErrorCode errorCode) {
		super(errorCode.getMsg());
		this.errorCodeObject = errorCode;
	}

	public ManagerException(ErrorCode errorCode, Throwable cause) {
		super(errorCode.getMsg(), cause);
		this.errorCodeObject = errorCode;
	}

	public ManagerException(int errorCode, Throwable cause) {
		super(cause.getMessage(), cause);
		this.errorCodeObject = new ErrorCodeEntity(errorCode, cause.getMessage());
	}

	public ManagerException(int errorCode, String message, Throwable cause) {
		super(message, cause);
		this.errorCodeObject = new ErrorCodeEntity(errorCode, message);
	}

	public long getErrorCode() {
		return this.errorCodeObject.getCode();
	}

	public ErrorCode getErrorCodeObject() {
		return this.errorCodeObject;
	}

}
