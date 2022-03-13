package com.kh.switchswitch.exchange.controller;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.switchswitch.card.model.dto.Card;
import com.kh.switchswitch.card.model.dto.CardRequestCancelList;
import com.kh.switchswitch.card.model.dto.CardRequestList;
import com.kh.switchswitch.card.model.service.CardService;
import com.kh.switchswitch.chat.model.service.ChatService;
import com.kh.switchswitch.common.code.ErrorCode;
import com.kh.switchswitch.common.exception.HandlableException;
import com.kh.switchswitch.exchange.model.dto.ExchangeStatus;
import com.kh.switchswitch.exchange.model.service.ExchangeService;
import com.kh.switchswitch.exchange.model.service.RatingService;
import com.kh.switchswitch.member.model.dto.MemberAccount;
import com.kh.switchswitch.member.model.service.MemberService;
import com.kh.switchswitch.point.model.dto.SavePoint;
import com.kh.switchswitch.point.model.service.PointService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("exchange")
@RequiredArgsConstructor
public class ExchangeController {
	
	Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
	
	private final ExchangeService exchangeService;
	private final PointService pointService;
	private final CardService cardService;
	private final MemberService memberService;
	private final ChatService chatService;
	private final RatingService ratingService;
	
	//카드교환 생성 폼
	@GetMapping("exchangeForm/{wishCardIdx}")
	public String exchagneForm(
			@AuthenticationPrincipal MemberAccount certifiedMember
			,@PathVariable Integer wishCardIdx
			, Model model){
		//내카드 리스트
		model.addAttribute("cardlist", cardService.selectMyCardList(certifiedMember));
		model.addAttribute("myRate", exchangeService.selectMyRate(certifiedMember.getMemberIdx()));
		
		//교환 희망 카드
		Map<String, Object> card = cardService.selectCard(wishCardIdx);
		model.addAttribute("userRate", exchangeService.selectMyRate(((Card)card.get("cardInfo")).getMemberIdx()));
		model.addAttribute("wishCard", card);
		
		//포인트 잔액
		SavePoint savePoint = exchangeService.selectSavePointByMemberIdx(certifiedMember.getMemberIdx());
		if(savePoint != null) {
			model.addAttribute("availableBal", savePoint.getAvailableBal());
			model.addAttribute("balance", savePoint.getBalance());
		}
		return "exchange/exchangeForm";
	}
	
	//카드교환 요청 (알람 : 교환요청 & (다른 요청에 대한 교환취소 알람))
	@PostMapping("exchangeForm")
	public String exchangeForm(
			@AuthenticationPrincipal MemberAccount certifiedMember
			, int wishCardIdx
			, String offerPoint
			, String availableBal
			, @RequestParam(required = false)  String[] cardIdxList
			, Model model) {
		int availableBalInt = availableBal.equals("") ? 0 :  Integer.parseInt(availableBal);
		//int offerPointInt = offerPoint.equals("") ? 0 :  Integer.parseInt(offerPoint);
		//교환요청리스트
		CardRequestList crl = exchangeService.requestExchange(certifiedMember, wishCardIdx, cardIdxList, offerPoint);
		//포인트 holding ?? 후 가용 포인트
		pointService.updateSavePointWithAvailableBal(availableBalInt - Integer.parseInt(offerPoint), certifiedMember.getMemberIdx());
		
		model.addAttribute("alarmType", "교환요청");
		model.addAttribute("reqIdx",crl.getReqIdx());
		model.addAttribute("receiverIdx",crl.getRequestedMemIdx());
		model.addAttribute("url","/market/cardmarket");
		
		return "/common/alarm";
	}
	
	//카드교환창
	@GetMapping("detail/{reqIdx}")
	public String detail(
			@AuthenticationPrincipal MemberAccount certifiedMember,
			@PathVariable(required = false) Integer reqIdx,
			Model model) {
		
		if(reqIdx == null) {
			throw new HandlableException(ErrorCode.FAILED_TO_LOAD_WITH_BAD_REQUEST);
		}
		
		CardRequestList cardRequestList = cardService.selectCardRequestListWithReqIdx(reqIdx);
		CardRequestCancelList cardRequstCancelList = cardService.selectCardRequestCancelListWithReqIdx(reqIdx);
		if(cardRequestList == null) {
			if(cardRequstCancelList == null) {
				throw new HandlableException(ErrorCode.FAILED_TO_LOAD_INFO);
			}
			cardRequestList = convertToCardRequestList(cardRequstCancelList);
		}
		
		//희망 카드
		Map<String, Object>  requestedCard = cardService.selectCard(cardRequestList.getRequestedCard());
		model.addAttribute("userRate", exchangeService.selectMyRate(((Card)requestedCard.get("cardInfo")).getMemberIdx()));
		model.addAttribute("wishCard", requestedCard);
		
		//요청받은 유저 평점
		model.addAttribute("requestedMemRate",exchangeService.selectMyRate(cardRequestList.getRequestedMemIdx()));
		
		//교환희망카드
		List<Map<String,Object>> requestCardlist = cardService.selectRequestCardListByReqIdx(cardRequestList);
		model.addAttribute("requestCardlist",requestCardlist);
		
		//요청 유저 평점
		model.addAttribute("reqMemRate", exchangeService.selectMyRate(cardRequestList.getRequestMemIdx()));
		
		//상대방 닉네임**
		if(certifiedMember.getMemberIdx().equals(cardRequestList.getRequestMemIdx())) {
			model.addAttribute("counterpartNick",memberService.selectMemberNickWithMemberIdx(cardRequestList.getRequestedMemIdx()));
		} else {
			model.addAttribute("counterpartNick",memberService.selectMemberNickWithMemberIdx(cardRequestList.getRequestMemIdx()));
		}
		
		//cardRequestList
		model.addAttribute("cardRequestList",cardRequestList);
		
		//cardRequestCancelList
		model.addAttribute("cardRequestCancelList", cardRequstCancelList);
		
		if(cardRequstCancelList==null) {
			model.addAttribute("status", cardService.selectExchangeStatusType(cardRequestList.getReqIdx()));
		} else {
			model.addAttribute("status", "CANCELED");
		}
		
		return "exchange/detail";
	}

	//요청거절 (알람 : 요청거절)
	@GetMapping("reject/{reqIdx}")
	public String reject(@PathVariable Integer reqIdx, Model model) {
		//교환요청리스트
		CardRequestList cardRequestList = cardService.selectCardRequestListWithReqIdx(reqIdx);
		if(cardRequestList == null) {
			throw new HandlableException(ErrorCode.FAILED_TO_LOAD_INFO);
		}
		//card status ->'REQUEST->'NONE' 변경 및 교환요청리스트 삭제
		cardService.rejectRequest(cardRequestList,"NONE");
		
		//돈 돌려줘야됨
		pointService.updateSavePoint(cardRequestList);
		
		//거절 알림 보내기
		model.addAttribute("alarmType", "요청거절");
		model.addAttribute("reqIdx",cardRequestList.getReqIdx());
		model.addAttribute("receiverIdx",cardRequestList.getRequestMemIdx());
		model.addAttribute("url","/card/my-card");
		
		return "/common/alarm";
	}
	
	//요청수락 (알람 : 요청수락)
	@GetMapping("accept/{reqIdx}")
	public String accept(@PathVariable Integer reqIdx, Model model) {
		//교환요청리스트
		CardRequestList cardRequestList = cardService.selectCardRequestListWithReqIdx(reqIdx);
		if(cardRequestList == null) {
			throw new HandlableException(ErrorCode.FAILED_TO_LOAD_INFO);
		}
		//card status ->'REQUEST->'ONGOING' 및 교환현황 테이블 
		//요청 카드 status "REQUEST" -> "NONE"( v2 : 요청거절 알림 ), 요청 받은 리스트 거절(card_request_cancel_list), card_request_list의 request_card 상태 
		cardService.acceptRequest(cardRequestList,"ONGOING");
		//요청 수락시 채팅방 생성
		chatService.makeChatRoom(cardRequestList.getRequestedMemIdx(),cardRequestList.getRequestMemIdx());
		//수락 알림 보내기
		model.addAttribute("alarmType", "요청수락");
		model.addAttribute("reqIdx",cardRequestList.getReqIdx());
		model.addAttribute("receiverIdx",cardRequestList.getRequestMemIdx());
		model.addAttribute("url","/exchange/detail/"+reqIdx);
		
		return "/common/alarm";
	}
	
	//요청취소 (알람 : 요청취소)
	@GetMapping("request-cancel/{reqIdx}")
	public String requestCancel(@PathVariable Integer reqIdx, Model model) {
		//교환요청리스트
		CardRequestList cardRequestList = cardService.selectCardRequestListWithReqIdx(reqIdx);
		if(cardRequestList == null) {
			throw new HandlableException(ErrorCode.FAILED_TO_LOAD_INFO);
		}
		//card status ->'REQUEST->'NONE' 및 교환요청리스트 삭제 및 교환요청취소리스트 생성
		cardService.requestCancelRequest(cardRequestList,"NONE");
		
		//돈 돌려줘야됨
		pointService.updateSavePoint(cardRequestList);
		
		//취소 알림 보내기
		model.addAttribute("alarmType", "요청취소");
		model.addAttribute("reqIdx",cardRequestList.getReqIdx());
		model.addAttribute("receiverIdx",cardRequestList.getRequestedMemIdx());
		model.addAttribute("url","/market/cardmarket");
		
		return "/common/alarm";
	}
	
	//교환취소요청 (알람 : 교환취소요청)
	@GetMapping("cancel-request/{reqIdx}/{status}")
	public String cancelRequest(@PathVariable Integer reqIdx
								,@PathVariable String status
								, Model model) {
		//교환요청리스트
		CardRequestList cardRequestList = cardService.selectCardRequestListWithReqIdx(reqIdx);
		if(cardRequestList == null) {
			throw new HandlableException(ErrorCode.FAILED_TO_LOAD_INFO);
		}
		
		//exchange status ->'ONGOING'->status("APPLICANTCANCEL"||"OWNERCANCEL")
		cardService.requestCancel(reqIdx, status);
		
		//취소 알림 보내기
		model.addAttribute("alarmType", "교환취소요청");
		model.addAttribute("reqIdx",cardRequestList.getReqIdx());
		if(status.equals("APPLICANTCANCEL")) {
			model.addAttribute("receiverIdx",cardRequestList.getRequestedMemIdx());
		} else {
			model.addAttribute("receiverIdx",cardRequestList.getRequestMemIdx());
		}
		model.addAttribute("url","/market/cardmarket");
		
		return "/common/alarm";
	}
	
	//교환취소요청거절 (알람 : 교환취소요청거절)
	@GetMapping("cancel-request-reject/{reqIdx}/{status}")
	public String cancelRequestReject(@PathVariable Integer reqIdx
								,@PathVariable String status
								, Model model) {
		
		//exchange status ->status("APPLICANTCANCEL"||"OWNERCANCEL")->ONGOING
		cardService.cancelRequestReject(reqIdx, "ONGOING");
		
		//교환요청리스트
		CardRequestList cardRequestList = cardService.selectCardRequestListWithReqIdx(reqIdx);
		if(cardRequestList == null) {
			throw new HandlableException(ErrorCode.FAILED_TO_LOAD_INFO);
		}
		
		//거절 알림 보내기
		model.addAttribute("alarmType", "교환취소요청거절");
		model.addAttribute("reqIdx",cardRequestList.getReqIdx());
		if(status.equals("APPLICANTCANCEL")) {
			model.addAttribute("receiverIdx",cardRequestList.getRequestedMemIdx());
		} else {
			model.addAttribute("receiverIdx",cardRequestList.getRequestMemIdx());
		}
		model.addAttribute("url","/exchange/detail/"+reqIdx);
		
		return "/common/alarm";
	}
	
	//교환취소요청취소 (알람 : 교환취소요청취소)
	@GetMapping("cancel-request-cancel/{reqIdx}/{status}")
	public String cancelRequestCancel(@PathVariable Integer reqIdx
								,@PathVariable String status
								, Model model) {
		
		//exchange status ->status("APPLICANTCANCEL"||"OWNERCANCEL")->ONGOING
		cardService.cancelRequestReject(reqIdx, "ONGOING");
		
		//교환요청리스트
		CardRequestList cardRequestList = cardService.selectCardRequestListWithReqIdx(reqIdx);
		if(cardRequestList == null) {
			throw new HandlableException(ErrorCode.FAILED_TO_LOAD_INFO);
		}
		
		//거절 알림 보내기
		model.addAttribute("alarmType", "교환취소요청취소");
		model.addAttribute("reqIdx",cardRequestList.getReqIdx());
		if(status.equals("APPLICANTCANCEL")) {
			model.addAttribute("receiverIdx",cardRequestList.getRequestedMemIdx());
		} else {
			model.addAttribute("receiverIdx",cardRequestList.getRequestMemIdx());
		}
		model.addAttribute("url","/exchange/detail/"+reqIdx);
		
		return "/common/alarm";
	}
	
	//교환취소 (알람 : 교환취소)
	@GetMapping("exchange-cancel/{reqIdx}")
	public String exchangeCancel(@PathVariable Integer reqIdx
								,@AuthenticationPrincipal MemberAccount certifiedMember
								, Model model) {
		//교환요청리스트
		CardRequestList cardRequestList = cardService.selectCardRequestListWithReqIdx(reqIdx);
		if(cardRequestList == null) {
			throw new HandlableException(ErrorCode.FAILED_TO_LOAD_INFO);
		}
		
		//card status ->'REQUEST->'NONE' 및 교환요청리스트 삭제 및 교환현황 삭제 및 교환요청거절리스트 생성
		cardService.exchangeCancelRequest(cardRequestList,"NONE");
		
		//돈 돌려줘야됨
		pointService.updateSavePoint(cardRequestList);
		
		//취소 알림 보내기
		model.addAttribute("alarmType", "교환취소");
		model.addAttribute("reqIdx",cardRequestList.getReqIdx());
		if(certifiedMember.getMemberIdx().equals(cardRequestList.getRequestMemIdx())) {
			model.addAttribute("receiverIdx",cardRequestList.getRequestedMemIdx());
			logger.info("receiverIdx : " + cardRequestList.getReqIdx());
		} else {
			model.addAttribute("receiverIdx",cardRequestList.getRequestMemIdx());
			logger.info("receiverIdx : " + cardRequestList.getRequestMemIdx());
		}
		model.addAttribute("url","/market/cardmarket");
		logger.info("reqIdx : " + cardRequestList.getReqIdx());
		logger.info("receiverIdx : " + cardRequestList.getReqIdx());
		return "/common/alarm";
	}
	
	//교환완료 (알람 : 평점요청)
	@GetMapping("complete/{reqIdx}")
	public String complete(
						@AuthenticationPrincipal MemberAccount certifiedMember
						, @PathVariable Integer reqIdx
						,@RequestParam(required = false) Integer rate
						, Model model) {
		//교환완료 알림 확인 시 상대방 평가 받아오기
		ExchangeStatus exchangeStatus = exchangeService.selectExchangeStatus(reqIdx);
		System.out.println(exchangeStatus);
		System.out.println(rate);
		System.out.println(certifiedMember);
		if(exchangeStatus.getType().equals("DONE")) {
			ratingService.createRating(exchangeStatus,rate, certifiedMember.getMemberIdx());
			return "redirect:/exchange/detail/"+reqIdx;
		}
		
		//확정요청리스트
		CardRequestList cardRequestList = cardService.selectCardRequestListWithReqIdx(reqIdx);
		if(cardRequestList == null) {
			throw new HandlableException(ErrorCode.FAILED_TO_LOAD_INFO);
		}
		//card status ->'REQUEST->'DONE' 및 교환현황 -> 'ONGOING'->'DONE'
		cardService.completeExchange(cardRequestList,"DONE");
		
		//교환 내역 생성
		exchangeService.insertExchangeHistory(cardService.selectExchangeStatusWithReqIdx(reqIdx));
		
		//교환완료 버튼 클릭 시 front에서 상대방 평가폼 생성해서 함께 정보 받아오기?
		if(rate != null) {
			ratingService.createRating(exchangeStatus,rate, certifiedMember.getMemberIdx());
		}
		
		//교환완료 알림 보내기
		model.addAttribute("alarmType", "평점요청");
		model.addAttribute("reqIdx",cardRequestList.getReqIdx());
		model.addAttribute("receiverIdx",cardRequestList.getRequestMemIdx());
		model.addAttribute("url","/market/cardmarket");
		
		return "/common/alarm";
	}
	
	//요청 내역 수정폼
	@GetMapping("revise/{reqIdx}")
	public String revise(@PathVariable Integer reqIdx
						, @AuthenticationPrincipal MemberAccount certifiedMember
						, Model model) {
		
		//확정요청리스트
		CardRequestList cardRequestList = cardService.selectCardRequestListWithReqIdx(reqIdx);
		if(cardRequestList == null) {
			throw new HandlableException(ErrorCode.FAILED_TO_LOAD_INFO);
		}
		Set<Integer> cardIdxSet = cardService.getCardIdxSet(cardRequestList);
		model.addAttribute("cardIdxSet",cardIdxSet);
		model.addAttribute("cardlist", cardService.selectCardListForRevise(cardIdxSet));
		model.addAttribute("myRate",exchangeService.selectMyRate(certifiedMember.getMemberIdx()));
		
		//내카드 리스트(확정요청리스트 제외)
		model.addAttribute("myCardlist", cardService.selectMyCardListExceptRequestCardList(certifiedMember,cardIdxSet));
	
		//교환 희망 카드
		Map<String, Object> card = cardService.selectCard(cardRequestList.getRequestedCard());
		model.addAttribute("userRate", exchangeService.selectMyRate(cardRequestList.getRequestedCard()));
		model.addAttribute("wishCard", card);
		
		//포인트 잔액
		SavePoint savePoint = exchangeService.selectSavePointByMemberIdx(certifiedMember.getMemberIdx());
		if(savePoint != null) {
			model.addAttribute("availableBal", savePoint.getAvailableBal()+cardRequestList.getPropBalance());
			model.addAttribute("balance", savePoint.getBalance());
		}
		
		model.addAttribute("propBalance", cardRequestList.getPropBalance());
		model.addAttribute("reqIdx", reqIdx);
		
		return "exchange/detailReviseForm";
	}
	
	//요청 내역 수정.do
	@PostMapping("reviseForm/{reqIdx}")
	public String revise(@AuthenticationPrincipal MemberAccount certifiedMember
			, @PathVariable int reqIdx
			, int wishCardIdx
			, String offerPoint
			, int availableBal
			, String[] previousCardIdxArr
			, @RequestParam(required = false)  String[] cardIdxList
			, Model model) {
		
		//교환요청리스트
		CardRequestList cardRequestList = createCardRequestList(certifiedMember, cardIdxList, wishCardIdx, offerPoint, reqIdx);
		exchangeService.reviseRequest(cardRequestList, cardIdxList.length, previousCardIdxArr, cardIdxList);
		
		//포인트 holding ?? 후 가용 포인트
		pointService.updateSavePointWithAvailableBal(availableBal - Integer.parseInt(offerPoint), certifiedMember.getMemberIdx());
		
		return "redirect:/exchange/detail/"+reqIdx;
	}
	
	//CardRequestList 생성
	private CardRequestList createCardRequestList(MemberAccount certifiedMember, String[] cardIdxList, int wishCardIdx, String offerPoint, int reqIdx) {
		CardRequestList cardRequestList = new CardRequestList();
		if(cardIdxList != null) {
			switch(5-cardIdxList.length) {
			case 1 : cardRequestList.setRequestCard4(Integer.valueOf(cardIdxList[3]));
			case 2 : cardRequestList.setRequestCard3(Integer.valueOf(cardIdxList[2])); 
			case 3 : cardRequestList.setRequestCard2(Integer.valueOf(cardIdxList[1]));
			case 4 : cardRequestList.setRequestCard1(Integer.valueOf(cardIdxList[0])); break;
			default : logger.debug("왜 0이 들어오지??");
			}
		}
		cardRequestList.setRequestedMemIdx(cardService.selectCardMemberIdxWithCardIdx(wishCardIdx));
		cardRequestList.setRequestMemIdx(certifiedMember.getMemberIdx());
		cardRequestList.setPropBalance(Integer.parseInt(offerPoint));
		cardRequestList.setReqIdx(reqIdx);
		return cardRequestList;
	}
	
	//CardRequestCancelList -> CardRequestList
	private CardRequestList convertToCardRequestList(CardRequestCancelList cardRequstCancelList) {
		CardRequestList cardRequestList = new CardRequestList();
		cardRequestList.setReqIdx(cardRequstCancelList.getReqIdx());
		cardRequestList.setPropBalance(cardRequstCancelList.getPropBalance());
		cardRequestList.setRequestCard1(cardRequstCancelList.getRequestCard1());
		cardRequestList.setRequestCard2(cardRequstCancelList.getRequestCard2());
		cardRequestList.setRequestCard3(cardRequstCancelList.getRequestCard3());
		cardRequestList.setRequestCard4(cardRequstCancelList.getRequestCard4());
		cardRequestList.setRequestedCard(cardRequstCancelList.getRequestedCard());
		cardRequestList.setRequestedMemIdx(cardRequstCancelList.getRequestedMemIdx());
		cardRequestList.setRequestMemIdx(cardRequstCancelList.getRequestMemIdx());
		return cardRequestList;
	}
	

}
