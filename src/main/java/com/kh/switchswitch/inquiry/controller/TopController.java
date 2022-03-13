package com.kh.switchswitch.inquiry.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.switchswitch.inquiry.model.dto.TopInquiry;
import com.kh.switchswitch.inquiry.model.service.TopInquiryService;
import com.kh.switchswitch.member.model.dto.MemberAccount;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("top")
public class TopController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final TopInquiryService inquiryService;
	 
	@GetMapping("top-form")
	public void inquiryForm() {}

	@PostMapping("upload")
	public String uploadInquiry(TopInquiry topInquiry, @AuthenticationPrincipal MemberAccount member ) {
		topInquiry.setUserId(member.getMemberName());
		inquiryService.insertInquiry(topInquiry);
		return "redirect:/top/top-list2";
	}

	  @GetMapping("top-list2") 
	  public String inquiryList(Model model, @RequestParam(required = true, defaultValue = "1") int page) {
		  model.addAllAttributes(inquiryService.selectInquiryList(page));
			return "top/top-list2";
	}

	
	  @GetMapping("top-detail")
		public String inquiryDetail(Integer supIdx, Model model) {
			Map<String,Object> commandMap = inquiryService.selectInquiryByIdx(supIdx);
			model.addAttribute("datas", commandMap);
			return "top/top-detail";
		}
		@GetMapping("top-modify")
		public void inquiryModify(Model model, Integer supIdx) {
			Map<String,Object> commandMap = inquiryService.selectInquiryByIdx(supIdx);
			model.addAttribute("datas", commandMap);
		}
		

		@PostMapping("modify")
		public String modifyinquiry(TopInquiry topInquiry, Integer supIdx) {
			topInquiry.setSupIdx(supIdx);
			inquiryService.modifyInquiry(topInquiry);
			return "redirect:/top/top-detail?supIdx="+topInquiry.getSupIdx();
		}

		@PostMapping("delete")
		public String deleteInquiry(Integer supIdx) {
			inquiryService.deleteInquiry(supIdx);
			return "redirect:/";
		}

}
