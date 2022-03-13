package com.kh.switchswitch.index;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kh.switchswitch.admin.model.service.AdminService;
import com.kh.switchswitch.card.model.service.CardService;
import com.kh.switchswitch.member.model.service.MemberService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
	
	private final CardService cardService;
	private final MemberService memberService;
	
	@GetMapping("/")
	public String index(Model model) {
		//Controller 메서드의 return 타입
		//void : 해당 메서드가 호출된 url 경로와 같은 위치의 jsp파일로 요청이 재지정
		//		 요청 url : /index/index -> jsp file : WEB-INF/views/index/index.jsp
		//String : jsp 파일의 위치를 지정 -> rerunt "index/index" jsp file : WEB-INF/views/index/index.jsp
		//ModelAndView : Model 객체 + view(jsp 위치)
		
		model.addAttribute("cardsTop5",cardService.selectCardsTop5());
		model.addAttribute("usersTop5",memberService.selectMembersTop5());
		
		return "index";
	}
	
	
	
	
	
	
	
}
