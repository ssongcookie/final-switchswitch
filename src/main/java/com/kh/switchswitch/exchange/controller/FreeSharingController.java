package com.kh.switchswitch.exchange.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.switchswitch.card.model.dto.FreeRequestList;
import com.kh.switchswitch.card.model.service.CardService;
import com.kh.switchswitch.chat.model.service.ChatService;
import com.kh.switchswitch.exchange.model.service.ExchangeService;
import com.kh.switchswitch.member.model.dto.MemberAccount;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("freeSharing")
@RequiredArgsConstructor
public class FreeSharingController {

	private final ExchangeService exchangeService;
	private final CardService cardService;
	private final ChatService chatService;
	
	//무료나눔 신청
	@GetMapping("request/{cardIdx}")
	public String freeSharingRequest(@AuthenticationPrincipal MemberAccount member,@PathVariable Integer cardIdx) {
		//신청하면 freeRequestList에 추가
		cardService.updateCardWithStatus(cardIdx, "REQUEST");
		exchangeService.requestFreeSharing(member.getMemberIdx(),cardIdx);
		return "redirect:/";
	}
	
	//무료나눔 신청 거절 및 취소
	@GetMapping("reject/{freqIdx}")
	public String reject(@PathVariable Integer freqIdx) {
		//freeRequestList에서 삭제
		exchangeService.rejectFreeSharing(freqIdx);
		FreeRequestList freeRequest = exchangeService.selectFreeRequestListWithFreqIdx(freqIdx);
		//카드 상태 Ongoing으로 업데이트
		cardService.updateCardWithStatus(freeRequest.getRequestedCard(), "NONE");
		
		return "redirect:/";
	}
	
	//무료나눔 신청 수락
	@GetMapping("accept/{freqIdx}")
	public String accept(@PathVariable Integer freqIdx) {
		FreeRequestList freeRequest = exchangeService.selectFreeRequestListWithFreqIdx(freqIdx);
		//카드 상태 Ongoing으로 업데이트
		cardService.updateCardWithStatus(freeRequest.getRequestedCard(), "ONGOING");
		//exchangeStatus에 추가
		cardService.insertExchangeStatusByFreeRequesetList(freeRequest);
		//수락 알림
		
		//채팅방 생성
		chatService.makeChatRoom(freeRequest.getRequestedMemIdx(),freeRequest.getRequestMemIdx());
		return "redirect:/";
	}
	

	//무료나눔 취소 (ongoing 상태일 때)
	@GetMapping("request-cancel/{freqIdx}")
	public String requestCancle(@PathVariable Integer freqIdx) {
		FreeRequestList freeRequest = exchangeService.selectFreeRequestListWithFreqIdx(freqIdx);
		//카드 상태 NONE으로 변경
		cardService.updateCardWithStatus(freeRequest.getRequestedCard(), "NONE");
		//freeRequestList에서 삭제
		exchangeService.rejectFreeSharing(freqIdx);
		//exchangeStatus에서 삭제
		cardService.deleteExchangeStatusWithFreqIdx(freqIdx);
		
		return "redirect:/";
	}
	
	
	//무료나눔 완료
	@GetMapping("complete/{freqIdx}")
	public String complete(@PathVariable Integer freqIdx) {
		FreeRequestList freeRequest = exchangeService.selectFreeRequestListWithFreqIdx(freqIdx);
		//카드 상태 Done으로 업데이트
		cardService.updateCardWithStatus(freeRequest.getRequestedCard(), "DONE");
		//exchangeStatus type Done
		cardService.updateExchangeStatusWithFreqIdx(freeRequest.getFreqIdx(), "DONE");
		//exchangeHistory에 추가
		exchangeService.insertExchangeHistory(cardService.selectExchangeStatusWithFreqIdx(freqIdx));
		//완료 알림
		
	
		return "redirect:/";
	}
}
