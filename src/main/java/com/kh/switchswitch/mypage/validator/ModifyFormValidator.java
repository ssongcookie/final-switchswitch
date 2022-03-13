package com.kh.switchswitch.mypage.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.kh.switchswitch.member.model.dto.Member;
import com.kh.switchswitch.member.model.repository.MemberRepository;

@Component
public class ModifyFormValidator implements Validator{

	private final MemberRepository memberRepository;
	
	public ModifyFormValidator(MemberRepository memberRepository) {
		super();
		this.memberRepository = memberRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(ModifyForm.class);
	}

	@Override
	public void validate(Object target, Errors errors) {

		ModifyForm form = (ModifyForm) target;
		Member member = memberRepository.selectMemberByEmailAndDelN(form.getMemberEmail());
		
		
	    if(memberRepository.selectMemberByNickName(form.getMemberNick()) != null && !form.getMemberNick().equals(member.getMemberNick())) {
		    errors.rejectValue("memberNick", "err-memberNick", "이미 존재하는 닉네임 입니다."); 
	    }else if(form.getMemberNick().equals("")) { 
	    	errors.rejectValue("memberNick",
	    	"err-memberNick", "올바르게 닉네임을 작성해주세요."); 
	    }
	    
		if(! Pattern.matches(".*[0-9a-zA-Z가-힣]{2,}", form.getMemberNick())) {
			errors.rejectValue("memberNick", "error-memberNick", "닉네임은 2글자 이상의 숫자 또는 한글 또는 영문 조합 입니다.");
		}
	  
	    if(!Pattern.matches("(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Zㄱ-힣0-9]).{8,}",form.getNewMemberPass()) && !form.getNewMemberPass().equals("")) { 
	    	errors.rejectValue("newMemberPass","err-newMemberPass", "비밀번호는 숫자 영문자 특수문자 조합인 8글자 이상의 문자열입니다."); 
	    }
	  
	    if(!form.getNewMemberPass().equals(form.getCheckMemberPss())) {
	    	errors.rejectValue("checkMemberPss", "err-checkMemberPss","비밀번호가 일치하지 않습니다.");
	    }
	  
	    if(!Pattern.matches("\\d{9,11}", form.getMemberTell())) {
	    	errors.rejectValue("memberTell", "err-memberTell", "전화번호는 9~11자리의 숫자입니다."); 
	    }
	 
		if(!Pattern.matches("^[0-6]\\d{4}$", form.getZipNo())) {
			errors.rejectValue("zipNo", "error-zipNo", "올바른 주소를 입력해 주세요.");
			errors.rejectValue("address", "error-address", "");
		}
	}
	
}
