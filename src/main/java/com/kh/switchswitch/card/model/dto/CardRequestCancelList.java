package com.kh.switchswitch.card.model.dto;

import lombok.Data;

@Data
public class CardRequestCancelList {
	private Integer reqIdx;
	private Integer requestedMemIdx;
	private Integer requestMemIdx;
	private Integer requestedCard;
	private Integer requestCard1;
	private Integer requestCard2;
	private Integer requestCard3;
	private Integer requestCard4;
	private Integer propBalance;
}
