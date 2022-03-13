package com.kh.switchswitch.chat.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class ChatMessages {

	private Integer cmIdx;
	private Integer chattingIdx;
	private Integer senderId;
	private String message;
	private Date createdAt;
	private Integer IsRead;
}
