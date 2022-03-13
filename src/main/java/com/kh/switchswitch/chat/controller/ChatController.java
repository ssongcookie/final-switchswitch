package com.kh.switchswitch.chat.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kh.switchswitch.chat.model.service.ChatService;
import com.kh.switchswitch.member.model.dto.MemberAccount;

import lombok.RequiredArgsConstructor;


@Controller
@RequestMapping("chat")
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;
	
	@GetMapping("chat")
	public void chat(Integer chattingIdx,Model model,@AuthenticationPrincipal MemberAccount member) {
		List<Map<String, Object>> chatMessageList = chatService.selectChatMessageByChattingIdx(chattingIdx,member.getMemberIdx());
		model.addAttribute("chatMessageList", chatMessageList);
		model.addAttribute("senderNick", chatService.getSenderNick(chattingIdx, member.getMemberIdx()));
	}
	
	@GetMapping("chatting-list")
	public void chattingList(@AuthenticationPrincipal MemberAccount member,Model model) {
		//채팅방 목록 불러오기
		model.addAttribute("chattingList", chatService.selectAllChattingList(member.getMemberIdx()));
	}
	
	@GetMapping("delete")
	public String delete(Integer chattingIdx,Model model,@AuthenticationPrincipal MemberAccount member) {
		chatService.leaveChatting(chattingIdx,member.getMemberIdx());
		return "redirect:/chat/chatting-list";
	}
	
}
