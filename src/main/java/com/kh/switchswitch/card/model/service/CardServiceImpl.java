package com.kh.switchswitch.card.model.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.switchswitch.alarm.model.dto.Alarm;
import com.kh.switchswitch.alarm.model.repository.AlarmRepository;
import com.kh.switchswitch.card.model.dto.Card;
import com.kh.switchswitch.card.model.dto.CardRequestCancelList;
import com.kh.switchswitch.card.model.dto.CardRequestList;
import com.kh.switchswitch.card.model.dto.FreeRequestList;
import com.kh.switchswitch.card.model.dto.SearchCard;
import com.kh.switchswitch.card.model.repository.CardRepository;
import com.kh.switchswitch.card.model.repository.CardRequestCancelListRepository;
import com.kh.switchswitch.card.model.repository.CardRequestListRepository;
import com.kh.switchswitch.card.model.repository.FreeRequestListRepository;
import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.common.util.FileUtil;
import com.kh.switchswitch.exchange.model.dto.ExchangeStatus;
import com.kh.switchswitch.exchange.model.repository.ExchangeRepository;
import com.kh.switchswitch.member.model.dto.MemberAccount;
import com.kh.switchswitch.member.model.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
	
	private final CardRepository cardRepository;
	private final CardRequestListRepository cardRequestListRepository;
	private final CardRequestCancelListRepository cardRequestCancelListRepository;
	private final ExchangeRepository exchangeRepository;
	private final MemberRepository memberRepository;
	private final AlarmRepository alarmRepository;
	private final FreeRequestListRepository freeRequestListRepository;
	
	@Override
	public void insertCard(List<MultipartFile> imgList, Card card) {
		FileUtil fileUtil = new FileUtil();
		
		//card instance 생성
		cardRepository.insertCard(card);
		
		//card instance
		for (MultipartFile multipartFile : imgList) {
			if(!multipartFile.isEmpty()) {
				cardRepository.insertFileInfo(fileUtil.fileUpload(multipartFile));
			}
		}
	}

	public List<Card> selectAllCard() {
		return cardRepository.selectAllCard();
	}

	public int selectCardMemberIdxWithCardIdx(int wishCardIdx) {
		return cardRepository.selectCardMemberIdxWithCardIdx(wishCardIdx);
	}

	public CardRequestList selectCardRequestListWithReqIdx(Integer reqIdx) {
		return cardRequestListRepository.selectCardRequestListWithReqIdx(reqIdx);
	}

	public Card selectCardWithCardIdx(Integer requestedCard) {
		return cardRepository.selectCardByCardIdx(requestedCard);
	}

	public List<Card> CardListWithMemberIdx(Integer requestMemIdx) {
		List<Card> cardList = new ArrayList<Card>();
		
		return null;
	}

	public void updateCardWithCardIdx(Card card) {
		cardRepository.modifyCard(card);
	}
	
	public void updateCardStatusWithCardIdxSet(CardRequestList cardRequestList, String status) {
		Set<Integer> cardIdxSet = new LinkedHashSet<Integer>();
		cardIdxSet.add(cardRequestList.getRequestCard1());
		cardIdxSet.add(cardRequestList.getRequestCard2());
		cardIdxSet.add(cardRequestList.getRequestCard3());
		cardIdxSet.add(cardRequestList.getRequestCard4());
		cardIdxSet.add(cardRequestList.getRequestedCard());
		cardIdxSet.remove(null);
		for (Integer cardIdx : cardIdxSet) {
			Card card = new Card();
			card.setCardIdx(cardIdx);
			card.setExchangeStatus(status);
			cardRepository.modifyCard(card);
		}
	}

	public void deleteCardRequestList(Integer reqIdx) {
		CardRequestList cardRequestList = cardRepository.selectCardRequestListWithReqIdx(reqIdx);
		cardRequestCancelListRepository.insertCardRequestCancelList(convertCardRequestListToCardRequestCancelList(cardRequestList));
		cardRepository.deleteCardRequestListWithReqIdx(reqIdx);
		
	}

	private CardRequestCancelList convertCardRequestListToCardRequestCancelList(CardRequestList cardRequestList) {
		CardRequestCancelList cardRequestCancelList = new CardRequestCancelList();
		cardRequestCancelList.setReqIdx(cardRequestList.getReqIdx());
		cardRequestCancelList.setRequestedMemIdx(cardRequestList.getRequestedMemIdx());
		cardRequestCancelList.setRequestMemIdx(cardRequestList.getRequestMemIdx());
		cardRequestCancelList.setRequestedCard(cardRequestList.getRequestedCard());
		cardRequestCancelList.setRequestCard1(cardRequestList.getRequestCard1());
		cardRequestCancelList.setRequestCard2(cardRequestList.getRequestCard2());
		cardRequestCancelList.setRequestCard3(cardRequestList.getRequestCard3());
		cardRequestCancelList.setRequestCard4(cardRequestList.getRequestCard4());
		cardRequestCancelList.setPropBalance(cardRequestList.getPropBalance());
		return cardRequestCancelList;
	}

	public void insertExchangeStatus(CardRequestList cardRequestList) {
		ExchangeStatus exchangeStatus = new ExchangeStatus();
		exchangeStatus.setPropBalance(cardRequestList.getPropBalance());
		exchangeStatus.setReqIdx(cardRequestList.getReqIdx());
		exchangeStatus.setRequestedMemIdx(cardRequestList.getRequestedMemIdx());
		exchangeStatus.setRequestMemIdx(cardRequestList.getRequestMemIdx());
		exchangeRepository.insertExchangeStatus(exchangeStatus);
	}
	


	public Set<Integer> getCardIdxSet(CardRequestList cardRequestList){
		Set<Integer> cardIdxSet = new LinkedHashSet<Integer>();
		cardIdxSet.add(cardRequestList.getRequestCard1());
		cardIdxSet.add(cardRequestList.getRequestCard2());
		cardIdxSet.add(cardRequestList.getRequestCard3());
		cardIdxSet.add(cardRequestList.getRequestCard4());
		cardIdxSet.remove(null);
		return cardIdxSet;
	}

	public String selectExchangeStatusType(Integer reqIdx) {
		return exchangeRepository.selectExchangeStatusType(reqIdx);
	}

	public void deleteExchangeStatus(Integer reqIdx) {
		exchangeRepository.deleteExchangeStatusWithReqIdx(reqIdx);
	}
	
	//교환취소 시 사용
	public void updateExchangeStatusWithReject(Integer reqIdx) {
		ExchangeStatus exchangeStatus = new ExchangeStatus();
		exchangeStatus.setReqIdx(reqIdx);
		exchangeStatus.setType("REJECT");
		exchangeRepository.updateExchangeStatus(exchangeStatus);
	}
	
	public void deleteExchangeStatusWithFreqIdx(Integer freqIdx) {
		exchangeRepository.deleteExchangeStatusWithFreqIdx(freqIdx);
	}


	public void updateExchangeStatus(Integer reqIdx, String type) {
		ExchangeStatus exchangeStatus = new ExchangeStatus();
		exchangeStatus.setReqIdx(reqIdx);
		exchangeStatus.setType(type);
		exchangeRepository.updateExchangeStatus(exchangeStatus);
	}
	
	public void updateExchangeStatusWithFreqIdx(Integer freqIdx, String type) {
		ExchangeStatus exchangeStatus = new ExchangeStatus();
		exchangeStatus.setFreqIdx(freqIdx);
		exchangeStatus.setType(type);
		exchangeRepository.updateFreeExchangeStatus(exchangeStatus);
	}


	public ExchangeStatus selectExchangeStatusWithReqIdx(Integer reqIdx) {
		return exchangeRepository.selectExchangeStatusWithReqIdx(reqIdx);
	}
	
	public ExchangeStatus selectExchangeStatusWithFreqIdx(Integer freqIdx) {
		return exchangeRepository.selectExchangeStatusWithFreqIdx(freqIdx);
	}

	public List<Card> selectCardList(Set<Integer> cardIdxSet) {
		List<Card> cardList = new ArrayList<Card>();
		for (Integer cardIdx : cardIdxSet) {
			cardList.add(cardRepository.selectCardByCardIdx(cardIdx));
		}
		return cardList;
	}

	public void updateCardWithStatus(int previousCardIdx, String status) {
		Card card = new Card();
		card.setCardIdx(previousCardIdx);
		card.setExchangeStatus(status);
		cardRepository.modifyCard(card);
	}

	public List<Card> searchCategoryCard(String category) {
		return cardRepository.searchCategoryCard(category);
	}

	public Card selectCardByReqIdx(Integer reqIdx) {
		return cardRepository.selectCardByReqIdx(reqIdx);
	}
	
	public List<Map<String, Object>> selectRequestedCardList(Integer memberIdx) {
		List<Map<String, Object>> requestedCardList = new ArrayList<>();
		
		//요청받은 카드 리스트 (요청신청, 진행중, 완료)
		List<Integer> reqIdxListOrigin = cardRequestListRepository.selectReqIdxWithMemberIdx(memberIdx);
		List<Integer> reqIdxList = new ArrayList<Integer>();
		for (Integer reqIdx : reqIdxListOrigin) {
			reqIdxList.add(reqIdx);
		}
		
		for (Integer reqIdx : reqIdxListOrigin) {
			System.out.println("requested reqIdx: " + reqIdx);
			//카드 리스트 (진행중, 완료)
			for (Integer exReqIdx : exchangeRepository.selectReqIdxWithMemberIdx(memberIdx)) {
				System.out.println("requested exReqIdx: " + exReqIdx);
				if(reqIdx.equals(exReqIdx)) {
					reqIdxList.remove(reqIdx);
				}
			}
		}
		
		for (Integer integer : reqIdxList) {
			System.out.println("requested reqIdx: " + integer);
		}
		
		List<Card> cardList = new ArrayList();
		//요청 받은 카드 리스트 (요청신청) with info
		for (Integer reqIdx : reqIdxList) {
			cardList.add(cardRepository.selectCardByCardIdx(cardRepository.selectCardRequestListWithReqIdx(reqIdx).getRequestedCard()));
		}
		for (Card card : cardList) {
			List<Integer> reqIdxs= cardRequestListRepository.selectReqIdxByRequestedCardIdx(card.getCardIdx());
			for (Integer reqIdx : reqIdxs) {
				requestedCardList.add(Map.of("requestedCard",card,
						"reqIdx", reqIdx,
						"fileDTO", cardRepository.selectFileInfoByCardIdx(card.getCardIdx()).get(0)));
			}
		}
		return requestedCardList;
	}

	//Ongoing 교환 카드
	public List<Map<String, Object>> selectOngoingCardList(Integer memberIdx) {
		List<Map<String, Object>> ongoingCardList = new ArrayList<>();
		//exchange_status(진행중, 완료, 거절) -> 진행중(ongoing) & request_mem_idx
		List<Integer> reqIdxListForRequest = cardRequestListRepository.selectReqIdxForRequestByOngoingCardIdx(memberIdx);
		//요청한 경우
		if(reqIdxListForRequest!=null) {
			for (Integer reqIdx : reqIdxListForRequest) {
				System.out.println("ongoing ex request reqIdx: " + reqIdx);
				CardRequestList cardRequestList = cardRepository.selectCardRequestListWithReqIdx(reqIdx);
				if(cardRequestList!=null) {
					for (Integer cardIdx : getCardIdxSet(cardRequestList)) {
						System.out.println("ongoing card request cardIdx: " + cardIdx);
						ongoingCardList.add(Map.of("ongoingCard",cardRepository.selectCardByCardIdx(cardIdx),
								"reqIdx", reqIdx,
								"fileDTO", cardRepository.selectFileInfoByCardIdx(cardIdx).get(0)));
					}
				}
				
			}
		}
		
		//exchange_status(진행중, 완료, 거절) -> 진행중(ongoing) & requested_mem_idx
		List<Integer> reqIdxListForRequested = cardRequestListRepository.selectReqIdxForRequestedByOngoingCardIdx(memberIdx);
		//요청받은 경우
		if(reqIdxListForRequested!=null) {
			for (Integer reqIdx : reqIdxListForRequested) {
				System.out.println("ongoing ex requested cardIdx: " + reqIdx);
				CardRequestList cardRequestList = cardRepository.selectCardRequestListWithReqIdx(reqIdx);
				ongoingCardList.add(Map.of("ongoingCard",cardRepository.selectCardByCardIdx(cardRequestList.getRequestedCard()),
						"reqIdx", reqIdx,
						"fileDTO", cardRepository.selectFileInfoByCardIdx(cardRequestList.getRequestedCard()).get(0)));
			}
		}
		
		return ongoingCardList;
	}

	//Request 교환 카드
	public List<Map<String, Object>> selectRequestCardList(Integer memberIdx) {
		List<Map<String, Object>> requestCardList = new ArrayList<>();
		//요청 카드
		List<Card> myRequestCardList = cardRepository.selectCardByMemberIdxWithRequest(memberIdx);
		if(myRequestCardList!=null) {
			for (Card card : myRequestCardList) {
				System.out.println("request card: " + card);
				//이전 요청 
				List<Integer> reqIdxListOrigin = cardRequestListRepository.selectReqIdxByRequestCardIdx(card.getCardIdx());
				List<Integer> reqIdxList = new ArrayList<Integer>();
				if(reqIdxListOrigin!=null) {
					for (Integer reqIdx : reqIdxListOrigin) {
						reqIdxList.add(reqIdx);
					}
					//exchange_status에 존재하는 경우를 제외(교환중)
					for (Integer reqIdx : reqIdxListOrigin) {
						if(exchangeRepository.selectExchangeStatusWithReqIdx(reqIdx) != null) {
							reqIdxList.remove(reqIdx);
						}
					}
					requestCardList.add(Map.of("requestCard",card,
							"reqIdx",reqIdxList.get(0),
							"fileDTO", cardRepository.selectFileInfoByCardIdx(card.getCardIdx()).get(0)));
				}
				
			}
		}
		return requestCardList;
	}
	
	public List<Map<String, Object>> selectDoneCardList(Integer memberIdx) {
		List<Map<String, Object>> doneCardList = new ArrayList<>();
		List<Card> cardList = cardRepository.selectCardByMemberIdxWithDONE(memberIdx);
		for (Card card : cardList) {
			doneCardList.add(Map.of("doneCard",card,"fileDTO", cardRepository.selectFileInfoByCardIdx(card.getCardIdx()).get(0)));
		}
		return doneCardList;
	}	

	@Override
	public List<Card> selectCardTrim(SearchCard searchCard) {
		return cardRepository.selectCardTrim(searchCard);
	}

	public void modifyCard(List<MultipartFile> imgList, Card card) {
		FileUtil fileUtil = new FileUtil();

		// card instance 생성
		cardRepository.modifyCard(card);

		//삭제
		cardRepository.deleteCard(card.getCardIdx());

		
		// card instance
		for (MultipartFile multipartFile : imgList) {
			if (!multipartFile.isEmpty()) {
				cardRepository.modifyFileInfo(fileUtil.fileUpload(multipartFile),card.getCardIdx());
			}
		}
	}

	public List<Map<String, Object>> selectMyExchangeCard(Integer memberIdx) {
		
		List<Map<String, Object>> exchangeCards = new ArrayList();
		List<Card> cardList = cardRepository.selectCardByMemberIdxAndIsFreeExceptDone(memberIdx,"N");
		for (Card card : cardList) {
			exchangeCards.add(Map.of("myExchangeCard",card,"fileDTO", cardRepository.selectFileInfoByCardIdx(card.getCardIdx()).get(0)));
		}
		
		return exchangeCards;
	}

	public List<Map<String, Object>> selectMyFreeCard(Integer memberIdx) {
		List<Map<String, Object>> freeCards = new ArrayList();
		
		List<Card> cardList = cardRepository.selectCardByMemberIdxAndIsFreeExceptDone(memberIdx,"Y");
		for (Card card : cardList) {
			freeCards.add(Map.of("freeCard",card,"fileDTO", cardRepository.selectFileInfoByCardIdx(card.getCardIdx()).get(0)));
		}
		
		return freeCards;
	}

	public Map<String, Object> selectModifyCard(Integer cardIdx) {

		Card card = cardRepository.selectCardByCardIdx(cardIdx);
		List<FileDTO> fileDTOs = cardRepository.selectFileInfoByCardIdx(cardIdx);
		String[] wishCard = card.getHopeKind().split(",");
		
		return Map.of("card",card,"files",fileDTOs ,"wishCard",wishCard);
	}

	
	public void insertExchangeStatusByFreeRequesetList(FreeRequestList freeRequest) {
		ExchangeStatus exchangeStatus = new ExchangeStatus();
		exchangeStatus.setFreqIdx(freeRequest.getFreqIdx());
		exchangeStatus.setRequestedMemIdx(freeRequest.getRequestedMemIdx());
		exchangeStatus.setRequestMemIdx(freeRequest.getRequestMemIdx());
		exchangeRepository.insertExchangeStatus(exchangeStatus);
		
	}

	public List<Map<String, Object>> selectCardsTop5() {
		/*
		 * if(schedule.getCardsTop5().isEmpty()) { 
		 * schedule.setCardsTop5(); } return
		 * schedule.getCardsTop5();
		 */
		List<Map<String, Object>> cardsTop5 = new ArrayList<>();
		List<Card> cards = cardRepository.selectCardsTop5();
		if(cards != null) {
			for (Card card : cards) {
				cardsTop5.add(
						Map.of("card", card, 
								"fileDTO", cardRepository.selectFileInfoByCardIdx(card.getCardIdx()).get(0), 
								"cardOwnerRate", Float.parseFloat(memberRepository.selectMemberScoreByMemberIdx(card.getMemberIdx()).orElse("0"))
								)
						);
			}
		}
		return cardsTop5;
	}

	public List<Map<String, Object>> selectMyCardList(MemberAccount certifiedMember) {
		List<Map<String, Object>> cardlist = new ArrayList<>();
		List<Card> myCardList = cardRepository.selectCardListIsDelAndStatusNone(certifiedMember.getMemberIdx());
		
		if (myCardList != null) {
			
			for (Card card : myCardList) {
				System.out.println(card);
				System.out.println(card.getCardIdx());
				cardlist.add(selectCard(card.getCardIdx()));
			}
		}
		return cardlist;
	}

	public Map<String, Object> selectCard(int cardIdx) {
		System.out.println(cardRepository.selectCardByCardIdx(cardIdx));
		System.out.println(cardRepository.selectFileInfoByCardIdx(cardIdx).get(0));
		return Map.of("cardInfo", cardRepository.selectCardByCardIdx(cardIdx), "fileDTO", cardRepository.selectFileInfoByCardIdx(cardIdx).get(0));
	}

	public List<Map<String, Object>> selectRequestCardListByReqIdx(CardRequestList cardRequestList) {
		List<Map<String,Object>> cardList = new ArrayList<Map<String,Object>>();
		for (Integer cardIdx : getCardIdxSet(cardRequestList)) {
			cardList.add(selectCard(cardIdx));
		}
		return cardList;
	}

	public void rejectRequest(CardRequestList cardRequestList, String status) {
		updateCardStatusWithCardIdxSet(cardRequestList, status);
		deleteCardRequestList(cardRequestList.getReqIdx());
		
	}

	public void acceptRequest(CardRequestList cardRequestList, String status) {
		updateCardStatusWithCardIdxSet(cardRequestList,status);
		insertExchangeStatus(cardRequestList);
		
		//요청 카드 status "REQUEST" -> "NONE", 요청 받은 리스트 card_request_list 삭제 및 card_request_cancel_list 생성
		List<CardRequestList> otherListForRequestedCard = 
				cardRepository.getOtherListForRequestedCard(cardRequestList.getRequestedCard(),cardRequestList.getReqIdx());
		if(otherListForRequestedCard!=null) {
			for (CardRequestList crl : otherListForRequestedCard) {
				//요청 카드 status "REQUEST" -> "NONE"
				updateCardStatusWithCardIdxSet(crl,"NONE");
				//요청 받은 리스트 card_request_list 삭제 및 card_request_cancel_list 생성
				deleteCardRequestList(crl.getReqIdx());
				//취소 알림 보내기
				Alarm alarm = new Alarm();
				alarm.setAlarmType("요청거절");
				alarm.setReceiverIdx(crl.getRequestMemIdx());
				alarm.setSenderIdx(crl.getRequestedMemIdx());
				alarm.setReqIdx(crl.getReqIdx());
				alarmRepository.insertAlarm(alarm);
			}
		}
	}

	public void requestCancelRequest(CardRequestList cardRequestList, String status) {
		updateCardStatusWithCardIdxSet(cardRequestList,status);
		deleteCardRequestList(cardRequestList.getReqIdx());
		
	}

	public void exchangeCancelRequest(CardRequestList cardRequestList, String status) {
		updateCardStatusWithCardIdxSet(cardRequestList,status);
		deleteCardRequestList(cardRequestList.getReqIdx());
		updateExchangeStatusWithReject(cardRequestList.getReqIdx());
		
	}

	public void completeExchange(CardRequestList cardRequestList, String status) {
		updateCardStatusWithCardIdxSet(cardRequestList, status);
		updateExchangeStatus(cardRequestList.getReqIdx(), status);
		
	}

	public List<Map<String, Object>> selectCardListForRevise(Set<Integer> cardIdxSet) {
		List<Map<String, Object>> cardlist = new ArrayList<>();
		List<Card> cardList = selectCardList(cardIdxSet);
		if(cardList != null) {
			for (Card card : cardList) {
				cardlist.add(selectCard(card.getCardIdx()));
			}
		}
		return cardlist;
	}

	public CardRequestCancelList selectCardRequestCancelListWithReqIdx(int reqIdx) {
		return cardRequestCancelListRepository.selectCardRequestCancelList(reqIdx);
	}

	public void requestCancel(Integer reqIdx, String status) {
		updateExchangeStatus(reqIdx, status);
	}

	public void updateCardViews(Card card) {
		card.setViews(card.getViews() + 1);
		updateCardWithCardIdx(card);
	}


	public void cancelRequestReject(Integer reqIdx, String status) {
		updateExchangeStatus(reqIdx, status);
	}

	public List<Map<String, Object>> selectWishCard(Integer memberIdx) {
		List<Map<String, Object>> wishCard = new ArrayList();
		List<Card> cardList = cardRepository.selectWishCardByMemberIdx(memberIdx);
		for (Card card : cardList) {
			wishCard.add(Map.of("wishCard",card,"fileDTO", cardRepository.selectFileInfoByCardIdx(card.getCardIdx()).get(0)));
		}
		
		return wishCard;
	}

	public List<Map<String, Object>> selectMyCardListExceptRequestCardList(MemberAccount certifiedMember,
			Set<Integer> cardIdxSet) {
		List<Map<String, Object>> cardlist = new ArrayList<>();
		List<Card> cardList = cardRepository.selectCardByMemberIdx(certifiedMember.getMemberIdx());
		if(cardList != null) {
			for (Card card : cardList) {
				for (Integer cardIdx : cardIdxSet) {
					if(!card.getCardIdx().equals(cardIdx)) {
						cardlist.add(selectCard(card.getCardIdx()));
					}
				}
			}
		}
		return cardlist;
	}

	public List<Card> selectAllCardExceptDone() {
		return cardRepository.selectAllCardExceptDone();
	}

	//Ongoing 나눔 카드 
	public List<Map<String, Object>> selectOngoingFreeCardList(Integer memberIdx) {
		List<Map<String, Object>> ongoingCardList = new ArrayList<>();
		//exchange_status(진행중, 완료, 거절) -> 진행중(ongoing) & request_mem_idx or requested_mem_idx
		List<Integer> freqIdxListForRequest = cardRequestListRepository.selectReqIdxForRequestByOngoingFreeCardIdx(memberIdx);
		for (Integer freqIdx : freqIdxListForRequest) {
			FreeRequestList cardRequestList = freeRequestListRepository.selectFreeRequestListWithReqIdx(freqIdx);
			ongoingCardList.add(Map.of("ongoingCard",cardRepository.selectCardByCardIdx(cardRequestList.getRequestedCard()),
					"freqIdx", freqIdx,
					"fileDTO", cardRepository.selectFileInfoByCardIdx(cardRequestList.getRequestedCard()).get(0)));
		}
		
		return ongoingCardList;
	}
	
	//Request 나눔 카드
	public List<Map<String, Object>> selectRequestedFreeCardList(Integer memberIdx) {
		List<Map<String, Object>> requestedCardList = new ArrayList<>();
			List<FreeRequestList> freeRequestedList = cardRequestListRepository.selectFreeRequestListByRequestedFreeCardIdx(memberIdx);
			if(freeRequestedList!=null) {
				for (FreeRequestList frl : freeRequestedList) {
					Card card = cardRepository.selectCardByCardIdx(frl.getRequestedCard());
					if(card.getExchangeStatus() == "REQUEST") {
						requestedCardList.add(Map.of("requestedCard",card,
								"freqIdx",frl.getFreqIdx(),
								"fileDTO", cardRepository.selectFileInfoByCardIdx(card.getCardIdx()).get(0)));
					}
				}
			}
			return requestedCardList;
		}
	}
	







