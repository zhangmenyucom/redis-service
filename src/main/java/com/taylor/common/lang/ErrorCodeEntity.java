package com.taylor.common.lang;

import java.io.Serializable;

/**
 * error code 实体类
 * 默认实体类
 * @author xiongmiao
 *
 */
public class ErrorCodeEntity implements ErrorCode,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6049350835218876025L;
	
	private int errcode;
	private String errmsg;
	
	public ErrorCodeEntity(){
		
	}
	
	public ErrorCodeEntity(int errcode, String errmsg) {
		super();
		this.errcode = errcode;
		this.errmsg = errmsg;
	}

	@Override
	public int getCode() {
		return this.errcode;
	}
	@Override
	public String getMsg() {
		return this.errmsg;
	}

	@Override
	public Integer getKey() {
		return this.getCode();
	}
	@Override
	public String getDescription() {
		return this.getMsg();
	}
}
