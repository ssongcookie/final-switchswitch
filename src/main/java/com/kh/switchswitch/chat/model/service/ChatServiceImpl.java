package com.kh.switchswitch.chat.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kh.switchswitch.chat.model.dto.ChatMessages;
import com.kh.switchswitch.chat.model.dto.Chatting;
import com.kh.switchswitch.chat.model.repository.ChatRepository;
import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.member.model.dto.Member;
import com.kh.switchswitch.member.model.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService{
	
	private final ChatRepository chatRepository;
	private final MemberRepository memberRepository;

	public List<Map<String, Object>> selectChatMessageByChattingIdx(Integer chattingIdx,Integer memberIdx) {
		List<Map<String, Object>> chatMessageList = new ArrayList<Map<String,Object>>();
		for (ChatMessages chatMessage : chatRepository.selectChatMessagesList(chattingIdx)) {
			//목록 불러올 때 읽음 처리 되어 있지 않은 메세지들 전부 읽음 처리
			if(chatMessage.getIsRead() == 1 && chatMessage.getSenderId() != memberIdx) {
				chatRepository.updateChatIsRead(chatMessage.getCmIdx());
			}
			String senderNick = "";
			if(chatMessage.getSenderId() == null) senderNick = "(알수없음)";
			else senderNick = getNick(chatMessage.getSenderId());
			chatMessageList.add(
					Map.of("chatMessage",chatRepository.selectChatMessages(chatMessage.getCmIdx())
							,"senderName",senderNick
							,"sendTime",chatRepository.selectSendTimeByCmId(chatMessage.getCmIdx())));
		}
		return chatMessageList;
	}
	
	public String getSenderNick(Integer chattingIdx,Integer memberIdx) {
		Chatting chatting = chatRepository.selectChattingByChattingIdx(chattingIdx);
		Integer senderIdx = 0;
		if(chatting.getAttendee1() != memberIdx && chatting.getAttendee1() != null) senderIdx = chatting.getAttendee1();
		if(chatting.getAttendee2() != memberIdx && chatting.getAttendee1() != null) senderIdx = chatting.getAttendee2();
		
		Member member = memberRepository.selectMemberWithMemberIdx(senderIdx);
		
		if(member == null) {
			return "(알수없음)";
		}
		
		
		if(member.getMemberDelYn() == 1) {
			return "(알수없음)";
		}
		return member.getMemberNick();
	}
	
	public String getNick(Integer memberIdx) {
		Member member = memberRepository.selectMemberWithMemberIdx(memberIdx);
		if(member.getMemberDelYn() == 1) {
			return "(알수없음)";
		}
		return member.getMemberNick();
	}


	//채팅방 생성
	public void makeChatRoom(Integer requestedMemIdx, Integer requestMemIdx) {
		//해당 회원들이 있는 채팅방이 존재하지 않다면 채팅방 생성
		if(chatRepository.selectChattingByAttendeeMemIdxs(requestedMemIdx, requestMemIdx) == null) {
			 Chatting chatting = new Chatting();
			 chatting.setAttendee1(requestMemIdx);
			 chatting.setAttendee2(requestedMemIdx);
			 chatRepository.insertChatting(chatting);
			 //history table에 추가
			Chatting hiChatting = chatRepository.selectChattingByAttendeeMemIdxs(requestedMemIdx, requestMemIdx);
			 chatRepository.insertChattingHistory(hiChatting);
		}
	}


	//추후 코드 수정 필요
	//회원 번호로 채팅방 받기
	public List<Map<String, Object>> selectAllChattingList(Integer memberIdx) {
		 List<Map<String, Object>> chattingInfoList = new ArrayList<Map<String,Object>>();
		//채팅방 리스트 받기
		List<Chatting> chattingList = chatRepository.selectAllChattingByMemberIdx(memberIdx);
		//해당 채팅방들의 마지막 전송 메세지 찾기
		//상대방의 프로필 받기
		List<String> attendeeList = new ArrayList<String>();
		List<String> lastMessageList = new ArrayList<String>();
		List<FileDTO> attendeeFileList = new ArrayList<FileDTO>();
		List<Integer> isReadList = new ArrayList<Integer>();
		for (Chatting chatting : chattingList) {
			Integer attendeeIdx = 0;
			if(chatRepository.selectLastChatMessages(chatting.getChattingIdx()) != null) {
				lastMessageList.add(chatRepository.selectLastChatMessages(chatting.getChattingIdx()));
			}else {
				lastMessageList.add("");
			}
			
			if(chatting.getAttendee1() != memberIdx) {
				if(chatting.getAttendee1() == null) {
					attendeeList.add("(알수없음)");
				}else {
					attendeeIdx=chatting.getAttendee1();
					attendeeList.add(getNick(chatting.getAttendee1()));
				}
			}
			else if(chatting.getAttendee2() != memberIdx) {
				if(chatting.getAttendee2() == null) {
					attendeeList.add("(알수없음)");
				}else {
					attendeeIdx=chatting.getAttendee2();
					attendeeList.add(getNick(chatting.getAttendee2()));
				}
			}
			if(attendeeIdx != 0){
				if(memberRepository.selectMemberByMemberIdx(attendeeIdx).getFlIdx() != null) {
					attendeeFileList.add(memberRepository.selectFileInfoByFlIdx(memberRepository.selectMemberByMemberIdx(attendeeIdx).getFlIdx()));
				}else {
					attendeeFileList.add(new FileDTO());
				}
			}else {
				attendeeFileList.add(new FileDTO());
			}
			isReadList.add(chatRepository.selectCountOfIsReadByChattingIdx(chatting.getChattingIdx(),memberIdx));
		}
		for (int i = 0; i < chattingList.size(); i++) {
			chattingInfoList.add(Map.of("chatting",chattingList.get(i)
					,"lastMessage",lastMessageList.get(i)
					,"memberNick",attendeeList.get(i)
					,"FileInfo",attendeeFileList.get(i)
					,"isRead",isReadList.get(i)));
		}
		
		return chattingInfoList;
	}

	
	public void leaveChatting(Integer chattingIdx, Integer memberIdx) {
		Chatting chatting = chatRepository.selectChattingByChattingIdx(chattingIdx);
		if(chatting.getAttendee1() == memberIdx) chatRepository.updateChattingAttendee1(chattingIdx);
		if(chatting.getAttendee2() == memberIdx) chatRepository.updateChattingAttendee2(chattingIdx);
	}


}
