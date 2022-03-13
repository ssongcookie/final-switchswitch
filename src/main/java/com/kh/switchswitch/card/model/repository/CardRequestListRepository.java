package com.kh.switchswitch.card.model.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kh.switchswitch.card.model.dto.CardRequestList;
import com.kh.switchswitch.card.model.dto.FreeRequestList;

@Mapper
public interface CardRequestListRepository {
	
	void insertCardRequestList(CardRequestList cardRequestList);

	@Select("select * from card_request_list where req_idx=#{reqIdx}")
	CardRequestList selectCardRequestListWithReqIdx(Integer reqIdx);

	@Select("select req_idx from card_request_list where requested_mem_idx=#{memberIdx}")
	List<Integer> selectReqIdxWithMemberIdx(Integer memberIdx);

	@Select("select sc_req_idx.currval from dual")
	Integer selectNewReqIdx();

	void updateCardRequestList(CardRequestList cardRequestList);

	@Select("select distinct req_idx from card_request_list where requested_card=#{cardIdx}")
	List<Integer> selectReqIdxByRequestedCardIdx(Integer cardIdx);
	
	//교환카드 요청
	@Select("select req_idx from card_request_list"
			+ "	where #{cardIdx} in (request_card1, request_card2, request_card3, request_card4)")
	List<Integer> selectReqIdxByRequestCardIdx(Integer cardIdx);
	//나눔카드 요청받은
	@Select("select * from free_request_list where requested_mem_idx =#{memberIdx}")
	List<FreeRequestList> selectFreeRequestListByRequestedFreeCardIdx(Integer memberIdx);
	//교환카드 진행중
	@Select("select req_idx from exchange_status where #{memberIdx} = request_mem_idx"
			+ " and type in('ONGOING','APPLICANTCANCEL','OWNERCANCEL') and req_idx is not null")
	List<Integer> selectReqIdxForRequestByOngoingCardIdx(Integer memberIdx);
	//교환카드 진행중
	@Select("select req_idx from exchange_status where #{memberIdx} = requested_mem_idx"
			+ " and type in('ONGOING','APPLICANTCANCEL','OWNERCANCEL')")
	List<Integer> selectReqIdxForRequestedByOngoingCardIdx(Integer memberIdx);
	//나눔카드 진행중
	@Select("select freq_idx from exchange_status where #{memberIdx} in (request_mem_idx, requested_mem_idx)"
			+ " and type in('ONGOING','APPLICANTCANCEL','OWNERCANCEL') and freq_idx is not null")
	List<Integer> selectReqIdxForRequestByOngoingFreeCardIdx(Integer memberIdx);

	
	
	
	
}
