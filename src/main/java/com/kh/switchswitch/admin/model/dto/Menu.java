package com.kh.switchswitch.admin.model.dto;

import lombok.Data;

@Data
public class Menu {
	
	private Integer urlIdx;
	private String code;
	private String url;
	private String urlName;
	private String position;
	private Integer depth;
	private String parent;

}
