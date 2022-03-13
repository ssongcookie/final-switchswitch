package com.kh.switchswitch.board.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kh.switchswitch.board.model.dto.Board;
import com.kh.switchswitch.board.model.dto.Reply;

public interface BoardService {
	//게시글등록
	void insertBoard(List<MultipartFile> imgList, Board board);
	//상세글조회
	Map<String, Object> selectBoardByIdx(int bdIdx);	
	//게시글 목록
	Map<String,Object> selectBoardList(int page);
	//게시글 수정
	void modifyBoard(Board board, List<MultipartFile> imgList);
	//게시글 삭제
	void deleteBoard(int bdIdx);
	
	//댓글목록
	List<Reply> getCommetList(Map<String, Object> commandMap);
	//댓글등록
	void boardReplyInsert(Reply reply);
	//댓글수정
	void modifyReply(Reply reply);
	
	void deleteReply(int cmIdx);
	
	Map<String, Object> selectBoardModifyBdIdx(int bdIdx);



}
