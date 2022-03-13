package com.kh.switchswitch.exchange.model.service;

import com.kh.switchswitch.exchange.model.dto.ExchangeStatus;
import com.kh.switchswitch.member.model.dto.MemberAccount;

public interface RatingService {

	void createRating(ExchangeStatus exchangeStatus, Integer rate, Integer memberIdx);
	
}
