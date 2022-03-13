package com.kh.switchswitch.inquiry.model.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.switchswitch.common.util.pagination.PagingV2;
import com.kh.switchswitch.inquiry.model.dto.TopInquiry;
import com.kh.switchswitch.inquiry.model.repository.TopInquiryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TopInquiryServiceImpl implements TopInquiryService{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final TopInquiryRepository inquiryRepository;
	
	public void insertInquiry(TopInquiry topInquiry) {
		inquiryRepository.insertInquiry(topInquiry);
	}
	
	//11/17
	public Map<String, Object> selectInquiryByIdx(Integer supIdx) {
		TopInquiry topInquiry = inquiryRepository.selectInquiryByIdx(supIdx);
		return Map.of("topInquiry",topInquiry);
	}
	
	public Map<String, Object> selectInquiryList(int page) {
		int cntPerPage = 10;
		int total = (inquiryRepository.selectContentCnt());
		int nowPage = page;
		String url = "/top/top-list2";
		
		PagingV2 pagingV2 = new PagingV2(total, nowPage, cntPerPage, url);
				

		Map<String,Object> commandMap = new HashMap<String,Object>();
		commandMap.put("paging", pagingV2);
		commandMap.put("inquiryList", inquiryRepository.selectInquiryListWithPageNo(Map.of("startBoard",pagingV2.getStartAlarm(),"lastBoard",pagingV2.getEndAlarm())));
		return commandMap;
	}
	@Transactional
	public void modifyInquiry(TopInquiry topInquiry) {
		inquiryRepository.modifyInquiry(topInquiry);
		
	}

	public void deleteInquiry(Integer supIdx) {
		inquiryRepository.deleteInquiry(supIdx);
		
	}













	
	

	
	
	
	
	
	
	
	
	
}
