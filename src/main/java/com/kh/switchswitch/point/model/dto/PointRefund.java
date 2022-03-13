package com.kh.switchswitch.point.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class PointRefund {
	
	private Integer prIdx;
	private Integer memberIdx;
	private Integer refundPoint;
	private String statusCode;
	private String adminName;
	private Date regDate;
	private String bankName; 
	private Date confirmDate;
	private String confirmCheck;
	private String account;

}
