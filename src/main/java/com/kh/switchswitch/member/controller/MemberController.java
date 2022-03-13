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
	
	//model의 속성 중 속성명이 joinForm인 속성이 있는 경우에
	//WebDataBinder의 속성을 초기화
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
			//access_token이 포함된 JSON String을 받아온다.
	        String accessTokenJsonData = memberService.getAccessTokenJsonData(code);
	        if(accessTokenJsonData.equals("error")) {
	        	throw new HandlableException(ErrorCode.FAILED_TO_KAKAO_LOGIN);
	        }

	        //JSON String -> Object(Map)
	        ObjectMapper accessTokenJsonObject= new ObjectMapper();
	        Map<String, Object> map = accessTokenJsonObject.readValue(accessTokenJsonData, Map.class);
			
			//access_token 추출
	        String accessToken = map.get("access_token").toString();
	        if(accessToken.equals("error")) {
	        	throw new HandlableException(ErrorCode.FAILED_TO_KAKAO_LOGIN);
	        }
	        
	        //유저 정보가 포함된 JSON String을 받아온다.
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
	        
	        //카카오 로그아웃 처리
	        memberService.logoutKakao(accessToken);
	        
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//email null일 경우
		if(email.isEmpty()) {
			throw new HandlableException(ErrorCode.FAILED_TO_JOIN_WITH_KAKAO);
		}
		
		KakaoLogin kakaoLogin = memberService.selectKakaoLoginById(id);
		Member member = memberService.selectMemberByEmailAndDelN(email);
		
		//카카오 또는 회원 정보 존재 X
		if(kakaoLogin == null || member == null) {
			member = new Member();
			member.setMemberEmail(email);
			member.setMemberNick(nickname);
			memberService.insertMemberWithKakao(member,id);
			//업데이트 or 새로 생성된 member 객체
			member = memberService.selectMemberByEmailAndDelN(email);
		}
		//비밀번호가 임의로 변경된 경우
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
		//동의 없이 /member/join 으로 접근 제한
		if(!consent.equals("consent")) {
			throw new HandlableException(ErrorCode.UNAUTHORIZED_PAGE);
		}
		
		model.addAttribute(new JoinForm())
		.addAttribute("error",new ValidatorResult().getError());
	}
	
	@PostMapping("join")
	public String join(@Validated JoinForm form
					, Errors errors //Errors 객체는 반드시 검증할 객체의 바로 뒤에 작성
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
		//SavePoint 생성
		memberService.insertMember(form);
		
		redirectAttrs.addFlashAttribute("message", "회원가입을 환영합니다. 로그인 해주세요");
		session.removeAttribute("persistToken");
		session.removeAttribute("persistUser");
		return "redirect:/member/login";
		
	}
	
	@GetMapping("consentForm")
	public void consentForm() {}
	
	@PostMapping("consentForm")
	public String consentForm(@RequestParam String consent
			, RedirectAttributes redirectAttrs) {
		//url(/member/join)에 파라미터 값 안보이도록 
		//RedirectAttributes의 FlashAttribute에 값 담고 redirect
		redirectAttrs.addFlashAttribute("consent", consent);
		return "redirect:/member/join";
	}
	
	@GetMapping("findingId")
	public void findingId() {}
	
	@PostMapping("findingId")
	public String findingId(String nickname, String tell, Model model) {
		
		String foundEmail = memberService.selectEmailByNicknameAndTell(nickname, tell);
		
		if(foundEmail == null) {
			model.addAttribute("error","입력하신 정보와 일치하는 회원이 존재하지 않습니다.");
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
			model.addAttribute("error","존재하지 않는 Email입니다.");
			return "/member/reissuePw";
		}
		
		memberService.reissuePwAndSendToEmail(foundMember);
		model.addAttribute("success","입력하신 Email로 새로운 비밀번호를 전송하였습니다.");
		return "/member/reissuePw";
		
	}
}
