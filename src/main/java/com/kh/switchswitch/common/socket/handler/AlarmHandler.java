package com.kh.switchswitch.common.socket.handler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.switchswitch.alarm.model.dto.Alarm;
import com.kh.switchswitch.alarm.model.repository.AlarmRepository;
import com.kh.switchswitch.member.model.dto.Member;
import com.kh.switchswitch.member.model.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AlarmHandler extends TextWebSocketHandler {
	
	private final AlarmRepository alarmRepository;
	private final MemberRepository memberRepository;
	
	private static Map<Integer, WebSocketSession> userSessions = new HashMap<>();
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		
		Member loginMember = memberRepository.selectMemberByEmailAndDelN(session.getPrincipal().getName());
		
		System.out.println("#AlarmController, afterConnectionEstablished");
		
		userSessions.put(loginMember.getMemberIdx(), session);
		
	}

	@Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		System.out.println("#ChattingController, handleMessage");
		Member loginMember = memberRepository.selectMemberByEmailAndDelN(session.getPrincipal().getName());
		// JSON --> Map으로 변환 (protocol: 알람타입, 수신자IDX, 요청IDX)
		ObjectMapper objectMapper = new ObjectMapper();
		Alarm newAlarm = objectMapper.readValue(message.getPayload(), Alarm.class);
		newAlarm.setSenderIdx(loginMember.getMemberIdx());
		//알람 테이블 생성 (alarmIdx 생성)
		alarmRepository.insertAlarm(newAlarm);
		newAlarm.setAlarmIdx(alarmRepository.selectCurrScAlarmIdx());
		//알람 수신자 session에 존재하는지 확인
		WebSocketSession receiverSession =  userSessions.get(newAlarm.getReceiverIdx());
		
		if( receiverSession != null) {
			log.info(newAlarm.getReqIdx().toString());
			String reqIdx = newAlarm.getReqIdx().toString();
			String alarmIdx = newAlarm.getAlarmIdx().toString();
			String msg = "";
			switch (newAlarm.getAlarmType()) {
			case "교환요청":
				msg = loginMember.getMemberNick() + "님으로부터 교환 요청이 왔습니다.";
				receiverSession.sendMessage(new TextMessage(createSendMsg(reqIdx, alarmIdx ,msg)));
				break;
			case "요청거절":
				msg = loginMember.getMemberNick() + "님이 교환 요청을 거절하였습니다.";
				receiverSession.sendMessage(new TextMessage(createSendMsg(reqIdx, alarmIdx ,msg)));
				break;
			case "요청수락":
				msg = loginMember.getMemberNick() + "님이 교환 요청을 수락했습니다.";
				receiverSession.sendMessage(new TextMessage(createSendMsg(reqIdx, alarmIdx ,msg)));
				break;
			case "교환취소요청":
				msg = loginMember.getMemberNick() + "이 교환취소를 요청하였습니다.";
				receiverSession.sendMessage(new TextMessage(createSendMsg(reqIdx, alarmIdx ,msg)));
				break;
			case "교환취소요청거절":
				msg = loginMember.getMemberNick() + "이 교환취소요청을 거절하였습니다.";
				receiverSession.sendMessage(new TextMessage(createSendMsg(reqIdx, alarmIdx ,msg)));
				break;
			case "교환취소요청취소":
				msg = loginMember.getMemberNick() + "이 교환취소요청을 취소하였습니다.";
				receiverSession.sendMessage(new TextMessage(createSendMsg(reqIdx, alarmIdx ,msg)));
				break;
			case "교환취소":
				msg = "교환취소가 완료되었습니다.";
				receiverSession.sendMessage(new TextMessage(createSendMsg(reqIdx, alarmIdx ,msg)));
				break;
			case "평점요청":
				msg = loginMember.getMemberNick() + "님과의 교환은 어떠셨나요?<br>"
						+ loginMember.getMemberNick() + "에 대한 평점을 남겨주세요.";
				receiverSession.sendMessage(new TextMessage(createSendMsg(reqIdx, alarmIdx ,msg)));
				break;
			}
		}
    }
	
	private String createSendMsg(String reqIdx, String alarmIdx, String msg) throws JsonProcessingException {
		// Map --> Json string으로 변환
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> mapToSend = new HashMap<String, String>();
		mapToSend.put("reqIdx", reqIdx);
		log.info(reqIdx);
		mapToSend.put("alarmIdx", alarmIdx);
		mapToSend.put("message", msg);
		String jsonStr = objectMapper.writeValueAsString(mapToSend);
		return jsonStr;
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		Member loginMember = memberRepository.selectMemberByEmailAndDelN(session.getPrincipal().getName());
		
		System.out.println("#ChattingController, afterConnectionClosed");
		
		userSessions.remove(loginMember.getMemberIdx());
		
	}
}