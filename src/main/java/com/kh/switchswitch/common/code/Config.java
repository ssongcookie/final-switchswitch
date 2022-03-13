package com.kh.switchswitch.common.code;

public enum Config {
	
	DOMAIN("http://toy-khj1220.ga"),
	//DOMAIN("http://localhost:9090"),
	COMPANY_EMAIL("projectteamyong@gmail.com"),
	UPLOAD_PATH("/usr/local/upload/"); //운영서버
	//UPLOAD_PATH("C:\\CODE\\upload\\");//개발서버
	
	public final String DESC;
	
	private Config(String desc) {
		this.DESC = desc;
	}
}
