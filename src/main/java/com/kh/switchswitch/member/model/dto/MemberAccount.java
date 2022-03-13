package com.kh.switchswitch.member.model.dto;

import java.sql.Date;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class MemberAccount extends User{
	
	private static final long serialVersionUID = -771193703920731890L;
	
	private Member member;

	public MemberAccount(Member member) {
		super(member.getMemberEmail(),member.getMemberPass(),List.of(new SimpleGrantedAuthority(member.getCode())));
		this.member = member;
	}

	public Member getMember() {
		return member;
	}
	
	public String getMemberTell() {
		return member.getMemberTell();
	}
	
	public String getMemberEmail() {
		return member.getMemberEmail();
	}


	public Integer getMemberIdx() {
		return member.getMemberIdx();
	}

	public String getCode() {
		return member.getCode();
	}

	public Date getMemberRegDate() {
		return member.getMemberRegDate();
	}

	public String getMemberPass() {
		return member.getMemberPass();
	}

	public Integer getMemberDelYn() {
		return member.getMemberDelYn();
	}

	public String getMemberNick() {
		return member.getMemberNick();
	}

	public String getMemberAddress() {
		return member.getMemberAddress();
	}

	public Date getMemberDelDate() {
		return member.getMemberDelDate();
	}

	public Integer getMemberScore() {
		return member.getMemberScore();
	}

	public String getMemberName() {
		return member.getMemberName();
	}
	
	public Integer getFlIdx() {
		return member.getFlIdx();
	}
	
	

}
