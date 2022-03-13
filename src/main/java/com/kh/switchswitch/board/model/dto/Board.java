package com.kh.switchswitch.board.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Board {
   private Integer bdIdx;
   private String userId;
   private Date regDate;
   private String title;
   private String content;
   private Integer isDel;
}