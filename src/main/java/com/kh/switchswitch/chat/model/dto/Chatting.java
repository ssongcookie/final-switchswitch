package com.kh.switchswitch.chat.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Chatting {

	private Integer chattingIdx;
	private Integer attendee1;
	private Integer attendee2;
	private Date createAt;
}
