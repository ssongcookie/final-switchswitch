package com.kh.switchswitch.member.validator;

import java.time.LocalDate;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.kh.switchswitch.common.code.ErrorCode;
import com.kh.switchswitch.common.exception.HandlableException;
import com.kh.switchswitch.member.model.dto.Member;
import com.kh.switchswitch.member.model.repository.MemberRepository;

@Component
public class JoinFormValidator implements Validator {
	
	private final MemberRepository memberRepository;
	
	public JoinFormValidator(MemberRepository memberRepository) {
		super();
		this.memberRepository = memberRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(JoinForm.class);
	}

	@Override
	public void validate(Object target, Errors errors) {
		JoinForm form = (JoinForm) target;
		boolean valid;
		
		//1. 이름
		valid = Pattern.matches(".*[a-zA-Z가-힣]{2,}", form.getMemberName());
		if(!valid) {
			errors.rejectValue("memberName", "error-memberName", "이름은 2글자 이상의 한글 또는 영문 조합 입니다.");
		}
		
		//2. Email
		valid = Pattern.matches("^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$", form.getMemberEmail());
		if(!valid) {
			errors.rejectValue("memberEmail","error-memberEmail","");
		}
		
		//3. 비밀번호가 8글자 이상, 숫자 영문자 특수문자 조합인지 확인
		valid = Pattern.matches("^(?=.*[^ㄱ-ㅣ가-힣])(?=.*?[a-zA-Z0-9!?@$%^&*-]).{8,}$", form.getMemberPass());
		if(!valid) {
			errors.rejectValue("memberPass", "error-memberPass", "비밀번호는 8글자 이상의 숫자 영문자 특수문자 조합 입니다.");
		}
		
		//4. 전화번호가 9~11자리의 숫자
		valid = Pattern.matches("^\\d{9,11}$", form.getMemberTell());
		if(!valid) {
			errors.rejectValue("memberTell", "error-memberTell", "전화번호는 9~11자리의 숫자입니다.");
		}
		
		//5. 닉네임
		valid = Pattern.matches(".*[0-9a-zA-Z가-힣]{2,}", form.getMemberNick());
		if(!valid) {
			errors.rejectValue("memberNick", "error-memberNick", "닉네임은 2글자 이상의 숫자 또는 한글 또는 영문 조합 입니다.");
		}
		
		//6. 주소 
		valid = Pattern.matches("^[0-6]\\d{4}$", form.getZipNo());
		if(!valid) {
			errors.rejectValue("zipNo", "error-zipNo", "올바른 주소를 입력해 주세요.");
			errors.rejectValue("address", "error-address", "");
		}
		
		//7. 재회원가입 (3개월 이후)
		Member member = memberRepository.selectMemberByEmailAndDelY(form.getMemberEmail());
		if(member != null) {
			int flg = member.getMemberDelDate().toLocalDate().plusMonths(3l).compareTo(LocalDate.now());
			if(flg > 0) {
				throw new HandlableException(ErrorCode.FAILED_TO_JOIN_LIMIT_DATE);
			}
			;
		}
	}
	
}
