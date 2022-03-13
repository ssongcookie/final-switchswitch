package com.kh.switchswitch.inquiry.model.service;

import java.util.List;
import java.util.Map;

import com.kh.switchswitch.inquiry.model.dto.Answer;
import com.kh.switchswitch.inquiry.model.dto.Inquiry;

public interface InquiryService {
	//게시글등록
	void insertInquiry(Inquiry inquiry);
	//상세글조회
	Map<String, Object> selectInquiryByIdx(Integer inquiryIdx);	
	//게시글 목록
	Map<String,Object> selectInquiryList(int page);
	void modifyInquiry(Inquiry inquiry);
	void deleteInquiry(Integer inquiryIdx);
	//mypage inquiryList
	Map<String, Object> selectMyInquiryList(int page,String memberName);
	//answer
	void insertAnswer(Answer answer);
	List<Answer> getAnswer(Map<String, Object> commandMap);
	void deleteAnswer(int answerIdx);

	



}
