package com.kh.switchswitch.admin.validator;


import com.kh.switchswitch.member.model.dto.Member;

import lombok.Data;

@Data
public class MemberUpdate {
	
	private String memberName;
	private String memberEmail;
	private String memberPass;
	private String memberTell;
	private String memberNick;
	private String zipNo;
	private String address;
	
	public Member convertToMember() {
		
		Member member = new Member();
		member.setMemberName(memberName);
		member.setMemberEmail(memberEmail);
		member.setMemberPass(memberPass);
		member.setMemberTell(memberTell);
		member.setMemberNick(memberNick);
		member.setMemberAddress("[" + zipNo + "] " + address );
		
		return member;
	}
	
	
	
}
