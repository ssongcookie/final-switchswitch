package com.kh.switchswitch.inquiry.model.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.switchswitch.common.util.pagination.Paging;
import com.kh.switchswitch.inquiry.model.dto.TopInquiry;

@Mapper
public interface TopInquiryRepository {

	//글작성
	@Insert("insert into top_inquiry(sup_idx, user_id, title, content)"
			+ " values(sc_top_idx.nextval, #{userId}, #{title}, #{content})")
	void insertInquiry(TopInquiry topInquiry);

	//상세글조회 
	@Select("select * from top_inquiry where sup_idx = #{supIdx}")
	TopInquiry selectInquiryByIdx(Integer supIdx);
	
	//게시글목록
	@Select("select * from top_inquiry where  is_del=0 ORDER BY sup_idx DESC") 
	List<TopInquiry> selectInquiryList(Paging pageUtil);

	//총 게시글 갯수 출력
	@Select("select count(*) from top_inquiry where is_del = 0")
	int selectContentCnt();

	
	void modifyInquiry(TopInquiry topInquiry);
	
	@Update("update top_inquiry set is_del = 1 where sup_idx = #{supIdx}")	
	void deleteInquiry(Integer supIdx);

	@Select("select * from (select rownum rnum, topr.* from (select top_inquiry.* from top_inquiry top_inquiry"
			+ " where is_del=0 order by REG_DATE DESC) topr"
			+ " ) where rnum between #{startBoard} and #{lastBoard}")	
	List<TopInquiry> selectInquiryListWithPageNo(Map<String, Integer> map);

}
