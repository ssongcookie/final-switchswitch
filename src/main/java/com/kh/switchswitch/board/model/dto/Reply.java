package com.kh.switchswitch.board.model.dto;

import java.sql.Date;

import lombok.Data;

@Data
public class Reply {

	private int bdIdx; //게시물 번호
	private int cmIdx; //댓글번호
	private String userId; //작성자 아이디
	private int isDel; //삭제여부
	private String content; //댓글내용
	private Date regDate; //작성일자
	private int cmParent; //부모댓글
	private Integer cmDepth; //깊이(cmDepth)는 트리 구조로 보여주기 위해 필요한 필드 = 현재 댓글이 게시물을 기준으로 어느 정도 떨어져 있는지,
	private Integer cmOrder; //게시물을 기준으로 몇 번째 글인지를 나타내는 순서 =  댓글들의 순서를 의미

}