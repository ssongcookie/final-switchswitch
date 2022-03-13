package com.kh.switchswitch.mypage.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.google.gson.Gson;
import com.kh.switchswitch.card.model.service.CardService;
import com.kh.switchswitch.common.code.ErrorCode;
import com.kh.switchswitch.common.exception.HandlableException;
import com.kh.switchswitch.common.validator.ValidatorResult;
import com.kh.switchswitch.exchange.model.service.ExchangeService;
import com.kh.switchswitch.inquiry.model.service.InquiryService;
import com.kh.switchswitch.member.model.dto.MemberAccount;
import com.kh.switchswitch.member.model.service.MemberService;
import com.kh.switchswitch.mypage.validator.ModifyForm;
import com.kh.switchswitch.mypage.validator.ModifyFormValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("mypage")
public class MypageController {
	
	private final MemberService memberService;
	private final ModifyFormValidator modifyFormValidator;
	private final PasswordEncoder passwordEncoder;
	private final ExchangeService exchangeService;
	private final CardService cardService;
	private final InquiryService inquiryService;


	@InitBinder(value = "modifyForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(modifyFormValidator);
	}


	@GetMapping("profile")
	public void profile(@AuthenticationPrincipal MemberAccount member,Model model) {
		int myRateCnt = exchangeService.selectMyRateCnt(member.getMemberIdx()).size();
		List<Integer> totalMyRate = exchangeService.selectMyRateCnt(member.getMemberIdx());
		model.addAttribute("myRate", Map.of("score",exchangeService.selectMyRate(member.getMemberIdx()),"cnt",myRateCnt));
		model.addAttribute("rateList"
							,Map.of("one",Math.round((double)Collections.frequency(totalMyRate, 1)/(double)myRateCnt*100)
									,"two",Math.round((double)Collections.frequency(totalMyRate, 2)/(double)myRateCnt*100)
									,"three",Math.round((double)Collections.frequency(totalMyRate, 3)/(double)myRateCnt*100)
									,"four",Math.round((double)Collections.frequency(totalMyRate, 4)/(double)myRateCnt*100)
									,"five",Math.round((double)Collections.frequency(totalMyRate, 5)/(double)myRateCnt*100)));
		if(member.getFlIdx() != null) {
			model.addAttribute("profileImage", memberService.selectFileInfoByFlIdx(member.getFlIdx()));
		}
		model.addAttribute(new ModifyForm()).addAttribute("error", new ValidatorResult().getError());
	}
	
	@PostMapping("profile")
	public String profileModify(@Validated ModifyForm form
							,Errors errors
							,@RequestParam(required = false) MultipartFile profileImage
							,Model model
							,HttpSession session
							,RedirectAttributes redirectAttr
							,@AuthenticationPrincipal MemberAccount member
			) {
		
		ValidatorResult vr = new ValidatorResult();
		model.addAttribute("error", vr.getError());

		if(errors.hasErrors()) {
			vr.addErrors(errors);
			return "mypage/profile";
		}

		if(profileImage == null) {
			memberService.updateMemberDelYN(form.convertToMember());
		}else {
			memberService.updateMemberWithFile(form.convertToMember(),profileImage);
		}
		
		System.out.println("하이");
		//security에 다시 회원 등록
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDetails newPrincipal = memberService.loadUserByUsername(member.getMemberEmail());
		UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(newPrincipal, authentication.getCredentials(),newPrincipal.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(newAuth);

		return "redirect:/mypage/profile";
	}

	@GetMapping("leave-member")
	public void leaveMember() {}
	
	@PostMapping("leave-member")
	public String leaveMemberImpl(@AuthenticationPrincipal MemberAccount member
									 ,String password 
									,RedirectAttributes redirectAttr
									,Model model
									,HttpSession session
								){
		
		if(!passwordEncoder.matches(password,member.getMemberPass())) {
			model.addAttribute("message","비밀번호가 틀렸습니다"); 
			return "mypage/leave-member"; 
		}
		
		//교환,나눔중인게 있으면 탈퇴 불가
		if(exchangeService.checkExchangeOngoing(member.getMemberIdx())) {
			throw new HandlableException(ErrorCode.FAILED_TO_LEAVE_MEMBER);
 		}
		
		member.getMember().setMemberDelYn(1);
		memberService.updateMemberDelYNForLeave(member.getMember());
		session.invalidate();
		return "redirect:/";
	}
	
	@GetMapping("pw-check")
	@ResponseBody
	public String pwCheck(@AuthenticationPrincipal MemberAccount member,String password) {
		
		if(passwordEncoder.matches(password,member.getMemberPass())) {
			return "available";
		}else {
			return "disable";
		}
	}
	
	@GetMapping("nick-check")
	@ResponseBody
	public String nickCheck(@AuthenticationPrincipal MemberAccount member,String nickName) {
		
		if(nickName.equals(member.getMemberNick()) || memberService.checkNickName(nickName)) {
			return "available";
		}else {
			return "disable";
		}
	}

	@GetMapping("history")
	public void history(@AuthenticationPrincipal MemberAccount member,Model model) {
		//내 별점 구하기
		model.addAttribute("myRate", exchangeService.selectMyRate(member.getMemberIdx()));
		//거래 완료된 카드들
		model.addAttribute("doneCardList",cardService.selectDoneCardList(member.getMemberIdx()));
		//교환내역 찾기
	}
	
	@ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    @GetMapping("exchange-history")
	public String exchangeHistoryJason(@AuthenticationPrincipal MemberAccount member,HttpServletResponse response) {
		
		response.addHeader("Access-Control-Allow-Origin","*");
		
		String json = new Gson().toJson(exchangeService.selectExchangeHistoryByMemIdx(member.getMemberIdx()));
		
		log.info("json={}" ,json);
		return json;
	}
	
	@ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    @GetMapping("free-history")
	public String freeHistoryJason(@AuthenticationPrincipal MemberAccount member,HttpServletResponse response) {
		
		response.addHeader("Access-Control-Allow-Origin","*");
		
		String json = new Gson().toJson(exchangeService.selectFreeRequestHistoryByMemIdx(member.getMemberIdx()));
		
		log.info("json={}" ,json);
		return json;
	}
	
	@GetMapping("personal-inquiry") 
	public String inquiryList(Model model, @RequestParam(required = true, defaultValue = "1") int page,@AuthenticationPrincipal MemberAccount member) {
		model.addAllAttributes(inquiryService.selectMyInquiryList(page,member.getMemberName()));
		System.out.println(inquiryService.selectMyInquiryList(page,member.getMemberName()));
		return "mypage/personal-inquiry";
	}
}
