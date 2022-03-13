package com.kh.switchswitch.card.model.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.switchswitch.card.model.dto.CardRequestCancelList;

@Mapper
public interface CardRequestCancelListRepository {

	@Select("select * from card_request_cancel_list where req_idx=#{reqIdx}")
	CardRequestCancelList selectCardRequestCancelList(int reqIdx);
	
	void insertCardRequestCancelList(CardRequestCancelList cardRequestCancelList);

}
