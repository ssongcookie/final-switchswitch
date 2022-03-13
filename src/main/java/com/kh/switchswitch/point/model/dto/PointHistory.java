package com.kh.switchswitch.point.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class PointHistory {
	
	private Integer phIdx;
	private Integer userIdx;
	private String type;
	private Integer points;
	private Integer resultPoint;
	private Date regDate;
	private String content;

}
