package com.kh.switchswitch.member.controller;

import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.switchswitch.common.code.ErrorCode;
import com.kh.switchswitch.common.exception.HandlableException;
import com.kh.switchswitch.common.validator.ValidatorResult;
import com.kh.switchswitch.member.model.dto.KakaoLogin;
import com.kh.switchswitch.member.model.dto.Member;
import com.kh.switchswitch.member.model.service.MemberService;
import com.kh.switchswitch.member.validator.JoinForm;
import com.kh.switchswitch.member.validator.JoinFormValidator;

@Controller
@RequestMapping("member")
public class MemberController {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MemberService memberService;
	private JoinFormValidator joinFormValidator;
	@Autowired
	RestTemplate http;
	@Autowired
	PasswordEncoder passwordEncoder;
	
	public MemberController(MemberService memberService, JoinFormValidator joinFormValidator) {
		super();
		this.memberService = memberService;
		this.joinFormValidator = joinFormValidator;
	}
	
	//model??? ?????? ??? ???????????? joinForm??? ????????? ?????? ?????????
	//WebDataBinder??? ????????? ?????????
	@InitBinder(value="joinForm")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(joinFormValidator);
	}
	
	@GetMapping("login")
	public void login() {}
	
	@GetMapping("kakaoLogin")
	public String kakaoLogin(@RequestParam("code") String code, RedirectAttributes redirectAttr) {
		String id = "";
		String nickname = "";
		String email = "";
		
		try {
			//access_token??? ????????? JSON String??? ????????????.
	        String accessTokenJsonData = memberService.getAccessTokenJsonData(code);
	        if(accessTokenJsonData.equals("error")) {
	        	throw new HandlableException(ErrorCode.FAILED_TO_KAKAO_LOGIN);
	        }

	        //JSON String -> Object(Map)
	        ObjectMapper accessTokenJsonObject= new ObjectMapper();
	        Map<String, Object> map = accessTokenJsonObject.readValue(accessTokenJsonData, Map.class);
			
			//access_token ??????
	        String accessToken = map.get("access_token").toString();
	        if(accessToken.equals("error")) {
	        	throw new HandlableException(ErrorCode.FAILED_TO_KAKAO_LOGIN);
	        }
	        
	        //?????? ????????? ????????? JSON String??? ????????????.
	        String userInfo = memberService.getUserInfo(accessToken);
	        
	        //JSON String -> Object(Map)
	        ObjectMapper userInfoMapper= new ObjectMapper();
	        map = userInfoMapper.readValue(userInfo, new TypeReference<Map<String, Object>>() {});
	    	id = map.get("id").toString();
	    	Map<String, String> propertyKeys = (Map<String, String>) map.get("properties");
	    	nickname = propertyKeys.get("nickname");
	    	Map<String, String> kakaoAccount = (Map<String, String>) map.get("kakao_account");
	    	email = kakaoAccount.get("email");
	        if(map.get("error") != null) {
	        	throw new HandlableException(ErrorCode.FAILED_TO_KAKAO_LOGIN);
	        }
	        
	        //????????? ???????????? ??????
	        memberService.logoutKakao(accessToken);
	        
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//email null??? ??????
		if(email.isEmpty()) {
			throw new HandlableException(ErrorCode.FAILED_TO_JOIN_WITH_KAKAO);
		}
		
		KakaoLogin kakaoLogin = memberService.selectKakaoLoginById(id);
		Member member = memberService.selectMemberByEmailAndDelN(email);
		
		//????????? ?????? ?????? ?????? ?????? X
		if(kakaoLogin == null || member == null) {
			member = new Member();
			member.setMemberEmail(email);
			member.setMemberNick(nickname);
			memberService.insertMemberWithKakao(member,id);
			//???????????? or ?????? ????????? member ??????
			member = memberService.selectMemberByEmailAndDelN(email);
		}
		//??????????????? ????????? ????????? ??????
		if(!passwordEncoder.matches(id, member.getMemberPass())) {
			memberService.updateMemberPass(member.getMemberIdx(), id);
		}
		
		redirectAttr.addFlashAttribute("kakao", "valid");
		redirectAttr.addFlashAttribute("email", email);
		redirectAttr.addFlashAttribute("password", id);
		
		return "redirect:/member/login";
		
	}
	
	@PostMapping("logout")
	public void logout() {}
	
	@GetMapping("email-check")
	@ResponseBody
	public String emailCheck(String memberEmail) {
		if(memberService.selectMemberByEmailAndDelN(memberEmail) == null) {
			return "available";
		}else {
			return "disable";
		}
	}
	
	@GetMapping("nickname-check")
	@ResponseBody
	public String nicknameCheck(String memberNick) {
		if(memberService.selectMemberByNicknameAndDelN(memberNick) == null) {
			return "available";
		}else {
			return "disable";
		}
	}
	
	@GetMapping("addrPopup")
	public void addrPopupG(String inputYn, Model model) {
		
		String confmKey = "U01TX0FVVEgyMDIxMTIyMjE4NTE0NDExMjA2MDM=";
		 
		model.addAttribute("confmKey", confmKey); 
		model.addAttribute("inputYn", inputYn);
	}
	
	@PostMapping("addrPopup")
	public void addrPopupP(String inputYn, String roadFullAddr, String zipNo, Model model) {
		
		model.addAttribute("inputYn", inputYn); 
		model.addAttribute("roadFullAddr", roadFullAddr); 
		model.addAttribute("zipNo", zipNo);
	}
	
	@GetMapping("join")
	public void joinForm(@ModelAttribute("consent") String consent, Model model) {
		//?????? ?????? /member/join ?????? ?????? ??????
		if(!consent.equals("consent")) {
			throw new HandlableException(ErrorCode.UNAUTHORIZED_PAGE);
		}
		
		model.addAttribute(new JoinForm())
		.addAttribute("error",new ValidatorResult().getError());
	}
	
	@PostMapping("join")
	public String join(@Validated JoinForm form
					, Errors errors //Errors ????????? ????????? ????????? ????????? ?????? ?????? ??????
					, Model model
					, HttpSession session) {
		
		ValidatorResult vr = new ValidatorResult();
		model.addAttribute("error", vr.getError());
		
		if(errors.hasErrors()) {
			vr.addErrors(errors);
			return "member/join";
		}
		
		String token = UUID.randomUUID().toString();
		session.setAttribute("persistUser", form);
		session.setAttribute("persistToken", token);
		
		memberService.authenticateByEmail(form,token);
		return "redirect:/";
	}
	
	@GetMapping("join-impl/{token}")
	public String joinImpl(@PathVariable String token
			, @SessionAttribute(value = "persistToken", required = false) String persistToken
			, @SessionAttribute(value = "persistUser", required = false) JoinForm form
			, HttpSession session
			, RedirectAttributes redirectAttrs) {
		
		if(!token.equals(persistToken)) {
			throw new HandlableException(ErrorCode.AUTHENTICATION_FAILED_ERROR);
		}
		//SavePoint ??????
		memberService.insertMember(form);
		
		redirectAttrs.addFlashAttribute("message", "??????????????? ???????????????. ????????? ????????????");
		session.removeAttribute("persistToken");
		session.removeAttribute("persistUser");
		return "redirect:/member/login";
		
	}
	
	@GetMapping("consentForm")
	public void consentForm() {}
	
	@PostMapping("consentForm")
	public String consentForm(@RequestParam String consent
			, RedirectAttributes redirectAttrs) {
		//url(/member/join)??? ???????????? ??? ??????????????? 
		//RedirectAttributes??? FlashAttribute??? ??? ?????? redirect
		redirectAttrs.addFlashAttribute("consent", consent);
		return "redirect:/member/join";
	}
	
	@GetMapping("findingId")
	public void findingId() {}
	
	@PostMapping("findingId")
	public String findingId(String nickname, String tell, Model model) {
		
		String foundEmail = memberService.selectEmailByNicknameAndTell(nickname, tell);
		
		if(foundEmail == null) {
			model.addAttribute("error","???????????? ????????? ???????????? ????????? ???????????? ????????????.");
			return "/member/findingId";
		}
		
		model.addAttribute("email", foundEmail);
		return "/member/findingId";
		
	}
	
	@GetMapping("reissuePw")
	public void reissuePw() {}
	
	@PostMapping("reissuePw")
	public String reissuePw(String email, Model model) {
		
		Member foundMember = memberService.selectMemberByEmailAndDelN(email);
		
		if(foundMember == null) {
			model.addAttribute("error","???????????? ?????? Email?????????.");
			return "/member/reissuePw";
		}
		
		memberService.reissuePwAndSendToEmail(foundMember);
		model.addAttribute("success","???????????? Email??? ????????? ??????????????? ?????????????????????.");
		return "/member/reissuePw";
		
	}
}
