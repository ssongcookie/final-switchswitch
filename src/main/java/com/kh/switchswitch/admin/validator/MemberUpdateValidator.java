package com.kh.switchswitch.admin.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.kh.switchswitch.admin.model.repository.AdminRepository;

@Component
public class MemberUpdateValidator implements Validator {
	
	private final AdminRepository adminRepository;
	
	public MemberUpdateValidator(AdminRepository adminRepository) {
		super();
		this.adminRepository = adminRepository;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return MemberUpdate.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		MemberUpdate form = (MemberUpdate) target;
		boolean valid;
		
		if(form.getMemberPass().equals("")) form.setMemberPass(null);
		if(form.getMemberTell().equals("")) form.setMemberTell(null);
		if(form.getMemberNick().equals("")) form.setMemberNick(null);
		
		if(!StringUtils.isEmpty(form.getMemberPass())) {
			valid = Pattern.matches("(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^a-zA-Zㄱ-힣0-9]).{8,}", form.getMemberPass());
			if(!valid) {
				errors.rejectValue("memberPass", "error-memberPass", "비밀번호는 8글자 이상의 숫자 영문자 특수문자 조합 입니다.");
				System.out.println(errors.getFieldValue("memberPass"));
			}
		}
		/*
		if(!StringUtils.isEmpty(form.getMemberTell())) {
			valid = Pattern.matches("^\\d{9,11}$", form.getMemberTell());
			if(!valid) {
				errors.rejectValue("memberTell", "error-memberTell", "전화번호는 9~11자리의 숫자입니다.");
			}
		}
		*/
		if(!StringUtils.isEmpty(form.getMemberNick())) {
			valid = Pattern.matches(".*[0-9a-zA-Z가-힣]{2,}", form.getMemberNick());
			if(!valid) {
				errors.rejectValue("memberNick", "error-memberNick", "닉네임은 2글자 이상의 숫자 또는 한글 또는 영문 조합 입니다.");
			}
		}
	}
	
}
