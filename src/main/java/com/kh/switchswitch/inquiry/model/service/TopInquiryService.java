package com.kh.switchswitch.inquiry.model.service;

import java.util.Map;

import com.kh.switchswitch.inquiry.model.dto.TopInquiry;

public interface TopInquiryService {
	//게시글등록
	void insertInquiry(TopInquiry topInquiry);
	//상세글조회
	Map<String, Object> selectInquiryByIdx(Integer supIdx);	
	//게시글 목록
	Map<String,Object> selectInquiryList(int page);
	void modifyInquiry(TopInquiry topInquiry);
	void deleteInquiry(Integer supIdx);

	



}
