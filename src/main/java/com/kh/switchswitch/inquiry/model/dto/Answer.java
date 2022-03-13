package com.kh.switchswitch.inquiry.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Answer {
	private int answerIdx;
   private int inquiryIdx;
   private String userId;
   private Date regDate;
   private String answer;
   private int isDel;
}