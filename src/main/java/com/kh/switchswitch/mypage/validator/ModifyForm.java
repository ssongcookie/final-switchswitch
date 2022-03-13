package com.kh.switchswitch.mypage.validator;

import com.kh.switchswitch.member.model.dto.Member;

import lombok.Data;

@Data
public class ModifyForm {

	private String memberPass;
	private String memberEmail;
	private String newMemberPass;
	private String checkMemberPss;
	private String memberTell;
	private String memberNick;
	private String memberAddress;
	private String address;
	private String zipNo;	

	public Member convertToMember() {

		String newPw = newMemberPass.equals("") ? memberPass : newMemberPass;

		
		Member member = new Member();
		member.setMemberEmail(memberEmail);
		member.setMemberPass(newPw);
		member.setMemberTell(memberTell);
		member.setMemberNick(memberNick);
		member.setMemberAddress("[" + zipNo + "] " + address );
		
		return member;
	}
}
