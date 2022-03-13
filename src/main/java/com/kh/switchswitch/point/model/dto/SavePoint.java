package com.kh.switchswitch.point.model.dto;

import lombok.Data;

@Data
public class SavePoint {
	
	private Integer pointIdx;
	private Integer memberIdx;
	private String code;
	private Integer balance;
	private Integer availableBal;

}
