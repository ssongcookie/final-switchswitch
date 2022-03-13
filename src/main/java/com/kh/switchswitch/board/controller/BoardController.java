package com.kh.switchswitch.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kh.switchswitch.board.model.dto.Board;
import com.kh.switchswitch.board.model.dto.Reply;
import com.kh.switchswitch.board.model.service.BoardService;
import com.kh.switchswitch.member.model.dto.MemberAccount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("board")
public class BoardController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private final BoardService boardService;

	@GetMapping("board-form")
	public void boardForm() {
	}

	@GetMapping("board-list2")
	public String boardList2(Model model, @RequestParam(required = true, defaultValue = "1") int page) {
		model.addAllAttributes(boardService.selectBoardList(page));
		return "board/board-list2";
	}

	@PostMapping("upload")
	public String uploadBoard(@RequestParam(required = false) List<MultipartFile> imgList, Board board, @AuthenticationPrincipal MemberAccount member) {
		board.setUserId(member.getMemberNick());
		boardService.insertBoard(imgList, board);

		return "redirect:/board/board-list2";
	}

	@GetMapping("board-list")
	public String boardList(Model model, @RequestParam(required = true, defaultValue = "1") int page) {
		model.addAllAttributes(boardService.selectBoardList(page));
		return "board/board-list2";
	}

	@GetMapping("board-detail")
	public String boardDetail(int bdIdx, Model model) {
		Map<String, Object> commandMap = boardService.selectBoardByIdx(bdIdx);
		model.addAttribute("datas", commandMap);
		model.addAttribute("commentList", boardService.getCommetList(commandMap));
		System.out.println(commandMap);
		return "board/board-detail";
	}

	@GetMapping("board-modify")
	public void boardModify(Model model, int bdIdx) {
		Map<String, Object> commandMap = boardService.selectBoardModifyBdIdx(bdIdx);
		model.addAttribute("datas", commandMap);
	}

	@PostMapping("modify")
	public String modifyBoard(Board board, @RequestParam(required = false) List<MultipartFile> imgList, int bdIdx) {
		System.out.println(board);
		
		board.setBdIdx(bdIdx);
		boardService.modifyBoard(board, imgList);
		return "redirect:/board/board-detail?bdIdx=" + board.getBdIdx();
	}

	@PostMapping("delete")
	public String deleteBoard(int bdIdx) {
		boardService.deleteBoard(bdIdx);
		return "redirect:/board/board-list2";
	}

	// AJAX 호출 (댓글 등록)
	@CrossOrigin("*")
	@ResponseBody
	@PostMapping("upload-reply")
	public String boardReplyUpload(@RequestBody Reply reply) {
		log.info("json={}", reply);
		boardService.boardReplyInsert(reply);
		return "success";
	}
	
	@CrossOrigin("*")
	@ResponseBody
	@PostMapping("modify-reply")
	public String modifyReply(@RequestBody Reply reply) {
		boardService.modifyReply(reply);
		return "success";
	}
	
	@CrossOrigin("*")
	@ResponseBody
	@PostMapping("delete-reply")
	public String deleteReply(@RequestParam int cmIdx) {

		boardService.deleteReply(cmIdx);
		//boardService.deleteReply(Integer.toString(cmIdx));
		return "success";
	}
	
 
 @PostMapping("reply")
    public String viewPostMethod(Model model, @RequestParam Map<String, Object> paramMap) {
        Reply reply = new Reply();
        reply.setUserId(paramMap.get("userId").toString());
        reply.setContent(paramMap.get("content").toString());

        // 수정&삭제 버튼 게시를 위한 유저 정보 전달
        Map<String, Object> userInform = new HashMap<String, Object>();
       userInform.put("userId", paramMap.get("userId"));
        model.addAttribute("userInform", userInform);

        return null;
    }
}
