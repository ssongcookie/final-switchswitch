/*package com.kh.switchswitch.common.socket.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.switchswitch.common.schedule.Schedule;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class BatchHandler extends TextWebSocketHandler  {
	
	private static List<WebSocketSession> sessions = new ArrayList<WebSocketSession>();
	
	@Autowired
	private Schedule schedule;
	
	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		sessions.add(session);
	}

	@Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		
    }
	
	@Scheduled(cron = "0 0/10 * * * *")
	public void sendMessage() {
		ObjectMapper objMapper = new ObjectMapper();
		String jsonString;
		try {
			jsonString = objMapper.writeValueAsString(schedule.getCardsTop5());
			System.out.println(jsonString);
			
			for (WebSocketSession session : sessions) {
				synchronized (session) {
					session.sendMessage(new TextMessage(jsonString));
				}
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		sessions.remove(session);
	}
}
*/
