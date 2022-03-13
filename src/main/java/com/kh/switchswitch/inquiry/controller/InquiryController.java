package com.kh.switchswitch.inquiry.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.switchswitch.board.model.dto.Reply;
import com.kh.switchswitch.inquiry.model.dto.Answer;
import com.kh.switchswitch.inquiry.model.dto.Inquiry;
import com.kh.switchswitch.inquiry.model.service.InquiryService;
import com.kh.switchswitch.member.model.dto.MemberAccount;
import com.kh.switchswitch.notice.model.dto.Notice;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("inquiry")
public class InquiryController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private final InquiryService inquiryService;
	 
	@GetMapping("inquiry-form")
	public void inquiryForm() {}

	@PostMapping("upload")
	public String uploadInquiry(Inquiry inquiry, @AuthenticationPrincipal MemberAccount member ) {
		inquiry.setUserId(member.getMemberName());
		inquiryService.insertInquiry(inquiry);
		return "redirect:/inquiry/inquiry-list2";
	}

	  @GetMapping("inquiry-list2") 
	  public String inquiryList(Model model, @RequestParam(required = true, defaultValue = "1") int page) {
		  model.addAllAttributes(inquiryService.selectInquiryList(page));
			return "inquiry/inquiry-list2";
	}

	
	  @GetMapping("inquiry-detail")
		public String inquiryDetail(Integer inquiryIdx, Model model) {
			Map<String,Object> commandMap = inquiryService.selectInquiryByIdx(inquiryIdx);
			model.addAttribute("datas", commandMap);
			model.addAttribute("answer", inquiryService.getAnswer(commandMap));
			return "inquiry/inquiry-detail";
		}
		@GetMapping("inquiry-modify")
		public void inquiryModify(Model model, Integer inquiryIdx) {
			Map<String,Object> commandMap = inquiryService.selectInquiryByIdx(inquiryIdx);
			model.addAttribute("datas", commandMap);
		}
		

		@PostMapping("modify")
		public String modifyinquiry(Inquiry inquiry, Integer inquiryIdx) {
			inquiry.setInquiryIdx(inquiryIdx);
			inquiryService.modifyInquiry(inquiry);
			return "redirect:/inquiry/inquiry-detail?inquiryIdx="+inquiry.getInquiryIdx();
		}

		@PostMapping("delete")
		public String deleteInquiry(Integer inquiryIdx) {
			inquiryService.deleteInquiry(inquiryIdx);
			return "redirect:/";
		}

		//answer
		@GetMapping("inquiry-answer")
		public void inquiryAnswer(Model model, Integer inquiryIdx) {
			model.addAttribute("datas", inquiryService.selectInquiryByIdx(inquiryIdx));
		}
		
		@PostMapping("answer")
		public String answerInsert(Inquiry inquiry,Answer answer,  Integer inquiryIdx, @AuthenticationPrincipal MemberAccount member ) {
			answer.setUserId(member.getMemberName());
			inquiry.setInquiryIdx(inquiryIdx);
			inquiryService.insertAnswer(answer);
			return "redirect:/inquiry/inquiry-detail?inquiryIdx="+inquiry.getInquiryIdx();
		}
		
		@CrossOrigin("*")
		@ResponseBody
		@PostMapping("delete-answer")
		public String deleteAnswer(@RequestParam int answerIdx) {

			inquiryService.deleteAnswer(answerIdx);
			return "success";
		}
}
