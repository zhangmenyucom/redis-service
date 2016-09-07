package com.taylor.common.lang;

/**
 * 
 * 错误码枚举接口
 * @author xiongmiao
 */
public interface ErrorCode extends EnumCode<Integer>{
	
	/**
	 * 获取返回码
	 * @return
	 */
	public int getCode();

	/**
	 * 获取错误信息
	 * @return
	 */
	public String getMsg();

}
