package com.kh.switchswitch.inquiry.model.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.switchswitch.common.util.pagination.Paging;
import com.kh.switchswitch.inquiry.model.dto.Answer;
import com.kh.switchswitch.inquiry.model.dto.Inquiry;

@Mapper
public interface InquiryRepository {

	//글작성
	@Insert("insert into inquiry(inquiry_idx, user_id, title, content,type)"
			+ " values(sc_inquiry_idx.nextval, #{userId}, #{title}, #{content}, #{type})")
	void insertInquiry(Inquiry inquiry);

	//상세글조회 
	@Select("select * from inquiry where inquiry_idx = #{inquiryIdx}")
	Inquiry selectInquiryByIdx(Integer inquiryIdx);
	
	//게시글목록
	@Select("select * from inquiry where  is_del=0 ORDER BY inquiry_idx DESC") 
	List<Inquiry> selectInquiryList(Paging pageUtil);

	//총 게시글 갯수 출력
	@Select("select count(*) from inquiry where is_del = 0")
	int selectContentCnt();
	
	//나의 게시글 갯수 출력
	@Select("select count(*) from inquiry where user_id=#{userId}")
	int selectMyContentCnt(String userId);
	
	//수정
	void modifyInquiry(Inquiry inquiry);
	
	@Update("update inquiry set is_del = 1 where inquiry_idx = #{inquiryIdx}")	
	void deleteInquiry(Integer inquiryIdx);

	@Select("select * from (select rownum rnum, inquiryr.* from (select inquiry.* from inquiry inquiry"
			+ " where is_del=0 order by REG_DATE DESC) inquiryr"
			+ " ) where rnum between #{startBoard} and #{lastBoard}")	
	List<Inquiry> selectInquiryListWithPageNo(Map<String, Integer> map);
	
	@Select("select * from (select rownum rnum, inquiryr.* from (select inquiry.* from inquiry inquiry"
			+ " where is_del=0 and user_id=#{userId} order by REG_DATE DESC) inquiryr"
			+ " ) where rnum between #{startBoard} and #{lastBoard}")	
	List<Inquiry> selectInquiryListWitchUserId(Map<String, Object> map);

	@Insert("insert into answer(answer_idx,user_id,answer,inquiry_idx)"
			+ " values(sc_answer_idx.nextval, #{userId}, #{answer},#{inquiryIdx})")
	void insertAnswer(Answer answer);
	
	@Select("SELECT * FROM answer WHERE  is_del=0 and inquiry_idx=#{inquiryIdx}")
	List<Answer> getAnswer(Integer inquiryIdx);

	@Update("update answer set is_del = 1 where answer_idx = #{answerIdx}")	
	void deleteAnswer(int answerIdx);
}
