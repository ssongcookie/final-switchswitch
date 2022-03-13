package com.kh.switchswitch.card.model.dto;

import java.sql.Date;
import java.util.List;

import lombok.Data;

@Data
public class Card {
	
	private Integer cardIdx;
	private Integer memberIdx;
	private String category;
	private String name;
	private Integer condition;
	private String deliveryCharge;
	private Date regDate;
	private String isfree;
	private String exchangeStatus;
	private String content;
	private String region;
	private Integer views;
	private String regionDetail;
	private String method;
	private Integer isDel;
	private String hopeKind;
	private String dateParse;
	private float memberRate;
	private String memberNick;
	private List imgUrl;
	private Integer requestedCardIdx;
	private Integer reqIdx;
}
