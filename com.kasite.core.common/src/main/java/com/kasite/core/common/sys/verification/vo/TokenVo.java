package com.kasite.core.common.sys.verification.vo;

public class TokenVo {
	private String access_token;//token
	private String create_time;//token被创建时间
	private String invalid_time;//token失效时间
	private String now_time;//服务器当前时间，可以用来判断token的有效期。防止不通服务器间时间不一致的问题。
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getCreate_time() {
		return create_time;
	}
	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}
	public String getInvalid_time() {
		return invalid_time;
	}
	public void setInvalid_time(String invalid_time) {
		this.invalid_time = invalid_time;
	}
	public String getNow_time() {
		return now_time;
	}
	public void setNow_time(String now_time) {
		this.now_time = now_time;
	}
	
}
