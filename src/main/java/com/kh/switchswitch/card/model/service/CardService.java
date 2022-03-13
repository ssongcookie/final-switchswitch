package com.kh.switchswitch.card.model.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.kh.switchswitch.card.model.dto.Card;
import com.kh.switchswitch.card.model.dto.CardRequestCancelList;
import com.kh.switchswitch.card.model.dto.CardRequestList;
import com.kh.switchswitch.card.model.dto.FreeRequestList;
import com.kh.switchswitch.card.model.dto.SearchCard;
import com.kh.switchswitch.exchange.model.dto.ExchangeStatus;
import com.kh.switchswitch.member.model.dto.MemberAccount;

public interface CardService {

	void insertCard(List<MultipartFile> imgList, Card card);

	int selectCardMemberIdxWithCardIdx(int wishCardIdx);
	
	List<Card> selectAllCard();

	CardRequestList selectCardRequestListWithReqIdx(Integer reqIdx);

	Card selectCardWithCardIdx(Integer requestedCard);

	List<Card> CardListWithMemberIdx(Integer requestMemIdx);

	void updateCardWithCardIdx(Card card);

	void insertExchangeStatus(CardRequestList cardRequestList);
	
	Set<Integer> getCardIdxSet(CardRequestList cardRequestList);

	String selectExchangeStatusType(Integer reqIdx);

	void deleteExchangeStatus(Integer reqIdx);

	void updateExchangeStatus(Integer reqIdx, String type);

	ExchangeStatus selectExchangeStatusWithReqIdx(Integer reqIdx);

	List<Card> selectCardList(Set<Integer> cardIdxSet);

	void updateCardWithStatus(int previousCardIdx, String status);
	
	List<Card> searchCategoryCard(String category);

	Card selectCardByReqIdx(Integer reqIdx);

	List<Map<String, Object>> selectRequestedCardList(Integer memberIdx);

	List<Map<String, Object>> selectOngoingCardList(Integer memberIdx);

	List<Map<String, Object>> selectRequestCardList(Integer memberIdx);
	
	List<Map<String,Object>> selectDoneCardList(Integer memberIdx);

	List<Card> selectCardTrim(SearchCard searchCard);

	void modifyCard(List<MultipartFile> imgList, Card card);

	List<Map<String, Object>> selectMyExchangeCard(Integer memberIdx);
	
	List<Map<String, Object>> selectWishCard(Integer memberIdx);

	List<Map<String, Object>> selectMyFreeCard(Integer memberIdx);

	Map<String, Object> selectModifyCard(Integer cardIdx);

	void insertExchangeStatusByFreeRequesetList(FreeRequestList freeRequest);
	
	void deleteExchangeStatusWithFreqIdx(Integer freqIdx);
	
	void updateExchangeStatusWithFreqIdx(Integer freqIdx, String type);
	
	ExchangeStatus selectExchangeStatusWithFreqIdx(Integer freqIdx);
	
	List<Map<String,Object>> selectCardsTop5();

	List<Map<String, Object>> selectMyCardList(MemberAccount certifiedMember);

	Map<String, Object> selectCard(int cardIdx);

	List<Map<String, Object>> selectRequestCardListByReqIdx(CardRequestList cardRequestList);

	void rejectRequest(CardRequestList cardRequestList, String status);

	void updateCardStatusWithCardIdxSet(CardRequestList cardRequestList, String string);

	void deleteCardRequestList(Integer reqIdx);

	void acceptRequest(CardRequestList cardRequestList, String status);

	void requestCancelRequest(CardRequestList cardRequestList, String status);

	void exchangeCancelRequest(CardRequestList cardRequestList, String status);

	void completeExchange(CardRequestList cardRequestList, String status);

	List<Map<String, Object>> selectCardListForRevise(Set<Integer> cardIdxSet);

	CardRequestCancelList selectCardRequestCancelListWithReqIdx(int reqIdx);

	void requestCancel(Integer reqIdx, String status);

	void updateCardViews(Card card);

	void cancelRequestReject(Integer reqIdx, String string);

	List<Map<String, Object>> selectMyCardListExceptRequestCardList(MemberAccount certifiedMember, Set<Integer> cardIdxSet);

	List<Card> selectAllCardExceptDone();

	List<Map<String, Object>> selectOngoingFreeCardList(Integer memberIdx);

	List<Map<String, Object>> selectRequestedFreeCardList(Integer memberIdx);
}
