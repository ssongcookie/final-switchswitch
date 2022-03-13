package com.kh.switchswitch.member.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Member {
	
	private Integer memberIdx;
	private String code;
	private Date memberRegDate;
	private String memberPass;
	private String memberEmail;
	private Integer memberDelYn;
	private String memberTell;
	private String memberNick;
	private String memberAddress;
	private Date memberDelDate;
	private Integer memberScore;
	private String memberName;
	private Integer flIdx;
	
}
