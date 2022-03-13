package com.kh.switchswitch.member.model.dto;

import java.time.LocalDateTime;

import lombok.Data;

//remember-me
@Data
public class PersistentLogins {
	
	private String username;
	private String series;
	private String token;
	private LocalDateTime lastUsed;
	
	
	
	
}
