package com.kh.switchswitch.exchange.model.repository;


import org.apache.ibatis.annotations.Delete;
import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.switchswitch.exchange.model.dto.ExchangeHistory;
import com.kh.switchswitch.exchange.model.dto.ExchangeStatus;

@Mapper
public interface ExchangeRepository {

	void insertExchangeStatus(ExchangeStatus exchangeStatus);


	@Select("select * from exchange_status where (user_idx1=#{memberIdx} or user_idx2 =#{memberIdx}) and type='ONGOING'")
	List<ExchangeStatus> selectEhByMemberIdxAndTypeOngoing(Integer memberIdx);

	@Select("select type from exchange_status where req_idx=#{reqIdx}")
	String selectExchangeStatusType(Integer reqIdx);

	@Select("select * from exchange_status where (request_mem_idx=#{memberIdx} or requested_mem_idx =#{memberIdx}) and type='ONGOING'")
	List<ExchangeStatus> selectEsByMemberIdxAndTypeOngoing(Integer memberIdx);

	//교환요청 삭제용(X)
	@Delete("delete from exchange_status where req_idx=#{reqIdx}")
	void deleteExchangeStatusWithReqIdx(Integer reqIdx);
	
	//나눔요청 삭제용
	@Delete("delete from exchange_status where freq_idx=#{freqIdx}")
	void deleteExchangeStatusWithFreqIdx(Integer freqIdx);

	//교환요청 업데이트용
	@Update("update exchange_status set type=#{type} where req_idx=#{reqIdx} ")
	void updateExchangeStatus(ExchangeStatus exchangeStatus);
	
	@Update("update exchange_status set type=#{type} where freq_idx=#{freqIdx} ")
	void updateFreeExchangeStatus(ExchangeStatus exchangeStatus);

	//나눔요청 상태 업데이트용
	@Update("update exchange_status set type=#{type} where freq_idx=#{freqIdx} ")
	void updateExchangeStatusWithFreqIDx(ExchangeStatus exchangeStatus);

	//교환요청 리스트 검색
	@Select("select * from exchange_status where req_idx=#{reqIdx}")
	ExchangeStatus selectExchangeStatusWithReqIdx(Integer reqIdx);
	
	//나눔요청 리스트 검색
	@Select("select * from exchange_status where freq_idx=#{freqIdx}")
	ExchangeStatus selectExchangeStatusWithFreqIdx(Integer freqIdx);
	
	@Insert("insert into exchange_history values(sc_eh_idx.nextval, #{eIdx}, sysdate, #{requestedMemIdx}, #{requestMemIdx})")
	void insertExchangeHistory(ExchangeHistory exchangeHistory);

	@Select("select req_idx from exchange_status where requested_mem_idx=#{memberIdx}")
	List<Integer> selectReqIdxWithMemberIdx(Integer memberIdx);

	@Select("select h.EH_IDX,h.E_IDX,h.EXCHANGE_DATE,h.REQUESTED_MEM_IDX,h.REQUEST_MEM_IDX,s.req_idx "
			+ "from exchange_history h join exchange_status s on h.e_idx = s.e_idx "
			+ "where (s.request_mem_idx = #{memberIdx} or s.requested_mem_idx =#{memberIdx})and  not s.req_idx is null")
	List<ExchangeHistory> selectExchangeHistoryByMemIdxAndReqNotNull(Integer memberIdx);

	
	@Select("select h.EH_IDX,h.E_IDX,h.EXCHANGE_DATE,h.REQUESTED_MEM_IDX,h.REQUEST_MEM_IDX,s.req_idx "
			+ "from exchange_history h join exchange_status s on h.e_idx = s.e_idx "
			+ "where (s.request_mem_idx = #{memberIdx} or s.requested_mem_idx =#{memberIdx})and  not s.freq_idx is null")
	List<ExchangeHistory> selectExchangeHistoryByMemIdxAndFreqNotNull(Integer memberIdx);

	@Select("select eh_idx from exchange_history "
			+ "where e_idx = (select e_idx from exchange_status where req_idx=#{reqIdx})")
	Integer selectEhIdxByReqIdx(Integer reqIdx);

	@Select("select * from exchange_status where req_idx=#{reqIdx}")
	ExchangeStatus selectExchangeStatus(Integer reqIdx);
	
}
