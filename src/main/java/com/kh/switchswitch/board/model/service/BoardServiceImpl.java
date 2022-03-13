package com.kh.switchswitch.board.model.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.switchswitch.board.model.dto.Board;
import com.kh.switchswitch.board.model.dto.Reply;
import com.kh.switchswitch.board.model.repository.BoardRepository;
import com.kh.switchswitch.card.model.dto.Card;
import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.common.util.FileUtil;
import com.kh.switchswitch.common.util.pagination.Paging;
import com.kh.switchswitch.common.util.pagination.PagingV2;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	private final BoardRepository boardRepository;
	
	public void insertBoard(List<MultipartFile> imgList, Board board) {
		FileUtil  fileUtil = new FileUtil();
		
		boardRepository.insertBoard(board);
		
		for (MultipartFile multipartFile : imgList) {
			if(!multipartFile.isEmpty()) {
				boardRepository.insertFileInfo(fileUtil.fileUpload(multipartFile));
			}
		}
	}

	public Map<String, Object> selectBoardByIdx(int bdIdx) {
		Board board = boardRepository.selectBoardByIdx(bdIdx);
		List<FileDTO> files = boardRepository.selectFilesByBdIdx(bdIdx);
		return Map.of("board",board,"files",files);
	}

	public Map<String, Object> selectBoardList(int page) {
		int cntPerPage = 10;
		int total = (boardRepository.selectContentCnt());
		int nowPage = page;
		String url = "/board/board-list2";
		
		PagingV2 pagingV2 = new PagingV2(total, nowPage, cntPerPage, url);
				

		Map<String,Object> commandMap = new HashMap<String,Object>();
		commandMap.put("paging", pagingV2);
		commandMap.put("boardList", boardRepository.selectBoardListWithPageNo(Map.of("startBoard",pagingV2.getStartAlarm(),"lastBoard",pagingV2.getEndAlarm())));
		return commandMap;
	}
	
	@Transactional
	public void modifyBoard(Board board, List<MultipartFile> imgList) {
		FileUtil  fileUtil = new FileUtil();
		boardRepository.modifyBoard(board);
		//삭제
		boardRepository.deleteBoardImg(board.getBdIdx());
		
		for (MultipartFile multipartFile : imgList) {
			if(!multipartFile.isEmpty()) {
				boardRepository.modifyFileInfo(fileUtil.fileUpload(multipartFile),board.getBdIdx());
			}
		}
	}
	

	public void deleteBoard(int bdIdx) {
		boardRepository.deleteBoard(bdIdx);
		
	}

	public List<Reply> getCommetList(Map<String, Object> commandMap) {
		 List<Reply> commentList = boardRepository.getCommentList(((Board)commandMap.get("board")).getBdIdx());
		return commentList;
	    }

	public void boardReplyInsert(Reply reply) {
		
		int lastOrder = boardRepository.selectLastOrderOfBoard(reply.getBdIdx());		
		reply.setCmOrder(lastOrder+1);	
		boardRepository.insertReplyDepth1(reply);
		
		
	}

	public void modifyReply(Reply reply) {
		boardRepository.modifyReply(reply);
	}

	public void deleteReply(int cmIdx) {
		boardRepository.deleteReply(cmIdx);
		
	}

	@Override
	public Map<String, Object> selectBoardModifyBdIdx(int bdIdx) {
		Board board = boardRepository.selectBoardModifyBdIdx(bdIdx);
		List<FileDTO> files = boardRepository.selectFileInfoBybdIdx(bdIdx);
		
		return Map.of("board",board,"files",files );
	}












	
	

	
	
	
	
	
	
	
	
	
}
