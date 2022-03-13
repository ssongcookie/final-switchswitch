package com.kh.switchswitch.alarm.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.kh.switchswitch.alarm.model.dto.Alarm;
import com.kh.switchswitch.alarm.model.repository.AlarmRepository;
import com.kh.switchswitch.common.util.pagination.PagingV2;
import com.kh.switchswitch.exchange.model.repository.ExchangeRepository;
import com.kh.switchswitch.exchange.model.repository.RatingRepository;
import com.kh.switchswitch.exchange.model.service.ExchangeService;
import com.kh.switchswitch.member.model.dto.MemberAccount;
import com.kh.switchswitch.member.model.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {
	
	private final AlarmRepository alarmRepository;
	private final MemberRepository memberRepository;
	private final ExchangeRepository exchangeRepository;
	private final RatingRepository ratingRepository;
	
	public List<Object> selectAlarmListWithReceiverIdx(Integer receiverIdx, int page) {
		List<Object> pageAndAlarm = new ArrayList<Object>();
		int total = alarmRepository.selectAlarmCnt(receiverIdx);
		int nowPage = page;
		int cntPerPage = 10;
		String url = "/mypage/alarm";
		PagingV2 pageUtil = new PagingV2(total, nowPage, cntPerPage, url);
		pageAndAlarm.add(pageUtil);
		List<Map<String, Object>> alarmList = new ArrayList<Map<String,Object>>();
		List<Alarm> alarms = alarmRepository.selectAlarmListWithReceiverIdx(
				(Map<String, Integer>) Map.of("receiverIdx", receiverIdx,"startAlarm",pageUtil.getStartAlarm(),"lastAlarm",pageUtil.getEndAlarm()));
		String msg = "";
		for (Alarm alarm : alarms) {
			switch(alarm.getAlarmType()) {
			case "교환요청":
				msg = memberRepository.selectMemberNickByMemberIdx(alarm.getSenderIdx()) + "님으로부터 교환 요청이 왔습니다.";
				break;
			case "요청거절":
				msg = memberRepository.selectMemberNickByMemberIdx(alarm.getSenderIdx()) + "님이 교환 요청을 거절하였습니다.";
				break;
			case "요청수락":
				msg = memberRepository.selectMemberNickByMemberIdx(alarm.getSenderIdx()) + "님이 교환 요청을 수락했습니다.";
				break;
			case "교환취소요청":
				msg = memberRepository.selectMemberNickByMemberIdx(alarm.getSenderIdx()) + "님이 교환취소를 요청하였습니다.";
				break;
			case "교환취소":
				msg = "교환취소가 완료되었습니다.";
				break;
			case "평점요청":
				msg = memberRepository.selectMemberNickByMemberIdx(alarm.getSenderIdx())  + "님과의 교환은 어떠셨나요?"
						+ memberRepository.selectMemberNickByMemberIdx(alarm.getSenderIdx()) + "님에 대한 평점을 남겨주세요.";
				break;
			}
			alarmList.add(Map.of("alarm", alarm, "message", msg));
		}
		pageAndAlarm.add(alarmList);
		return pageAndAlarm;
	}

	public void insertAndUpdateAlarmList(List<Alarm> alarmList) {
		if(alarmList.size() != 0) {
			for (Alarm alarm : alarmList) {
				//int null 비교하려면 wrapper class 사용
				if(Integer.valueOf(alarm.getAlarmIdx()) == null) {
					alarmRepository.insertAlarm(alarm);
				} else {
					alarmRepository.updateAlarmIsRead(alarm);
				}
			}
		}
		
	}

	public void updateAlarm(Alarm alarm) {
		alarm.setIsRead(1);
		alarmRepository.updateAlarmIsRead(alarm);
	}

	public boolean checkRating(Alarm alarm) {
		Integer ehIdx = exchangeRepository.selectEhIdxByReqIdx(alarm.getReqIdx());
		System.out.println(ratingRepository.selectRatingByMemIdxAndEhIdx(alarm.getSenderIdx(),ehIdx));
		System.out.println(ratingRepository.selectRatingByMemIdxAndEhIdx(alarm.getSenderIdx(),ehIdx) == 1);
		
		if(ratingRepository.selectRatingByMemIdxAndEhIdx(alarm.getSenderIdx(),ehIdx) == 1) {
			return true;
		}
		return false;
		
	}

}
