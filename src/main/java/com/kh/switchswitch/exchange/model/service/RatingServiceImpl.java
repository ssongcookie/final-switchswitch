package com.kh.switchswitch.exchange.model.service;

import org.springframework.stereotype.Service;

import com.kh.switchswitch.exchange.model.dto.ExchangeStatus;
import com.kh.switchswitch.exchange.model.dto.Rating;
import com.kh.switchswitch.exchange.model.repository.ExchangeRepository;
import com.kh.switchswitch.exchange.model.repository.RatingRepository;
import com.kh.switchswitch.member.model.dto.Member;
import com.kh.switchswitch.member.model.dto.MemberAccount;
import com.kh.switchswitch.member.model.repository.MemberRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RatingServiceImpl implements RatingService {

	private final RatingRepository ratingRepository;
	private final ExchangeRepository exchangeRepository;
	private final MemberRepository memberRepository;
	
	public void createRating(ExchangeStatus exchangeStatus, Integer rate, Integer memberIdx) {
		
		Integer ehIdx = exchangeRepository.selectEhIdxByReqIdx(exchangeStatus.getReqIdx());
		Rating rating = new Rating();
		rating.setEhIdx(ehIdx);
		if(memberIdx == exchangeStatus.getRequestedMemIdx()) {
			rating.setUserIdx(exchangeStatus.getRequestMemIdx());
		} else {
			rating.setUserIdx(exchangeStatus.getRequestedMemIdx());
		}
		
		
		rating.setRating(rate);
		ratingRepository.insertRating(rating);
		
		//member table 업데이트 평가 반영
		Integer memberScore = ratingRepository.calAvgRate(exchangeStatus.getRequestedMemIdx());
		if(memberScore == null) memberScore = 0;
		Member member = new Member();
		member.setMemberIdx(exchangeStatus.getRequestedMemIdx());
		member.setMemberScore(memberScore);
		memberRepository.updateMember(member);
	}
	
}
