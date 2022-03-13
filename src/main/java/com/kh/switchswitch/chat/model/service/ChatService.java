package com.kh.switchswitch.chat.model.service;

import java.util.List;
import java.util.Map;

public interface ChatService {

	List<Map<String, Object>> selectChatMessageByChattingIdx(Integer chattingIdx, Integer memberIdx);

	void makeChatRoom(Integer requestedMemIdx, Integer requestMemIdx);

	List<Map<String, Object>> selectAllChattingList(Integer memberIdx);
	
	 String getSenderNick(Integer chattingIdx,Integer memberIdx);
	 
	 String getNick(Integer memberIdx);

	void leaveChatting(Integer chattingIdx, Integer memberIdx);

}
