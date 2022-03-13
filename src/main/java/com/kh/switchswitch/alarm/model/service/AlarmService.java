package com.kh.switchswitch.alarm.model.service;

import java.util.List;

import com.kh.switchswitch.alarm.model.dto.Alarm;
import com.kh.switchswitch.member.model.dto.MemberAccount;

public interface AlarmService {
	
	List<Object> selectAlarmListWithReceiverIdx(Integer receiverIdx, int page);
	
	void insertAndUpdateAlarmList(List<Alarm> alarmList);

	void updateAlarm(Alarm alarm);

	boolean checkRating(Alarm alarm);
}
