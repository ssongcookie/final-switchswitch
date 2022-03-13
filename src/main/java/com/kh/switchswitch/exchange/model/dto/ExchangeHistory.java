package com.kh.switchswitch.exchange.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class ExchangeHistory {

	private Integer ehIdx;
	private Integer eIdx;
	private Date exchangeDate;
	private Integer requestMemIdx;
	private Integer requestedMemIdx;
}
