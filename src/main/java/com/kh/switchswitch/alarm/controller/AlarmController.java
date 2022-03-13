package com.kh.switchswitch.alarm.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.switchswitch.alarm.model.dto.Alarm;
import com.kh.switchswitch.alarm.model.service.AlarmService;
import com.kh.switchswitch.member.model.dto.MemberAccount;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AlarmController {
	
	private final AlarmService alarmService;
	
	@GetMapping("mypage/alarm")
	public void alarm(@AuthenticationPrincipal MemberAccount memberAccount
			, Model model,
			@RequestParam(required = true, defaultValue = "1") int page) {
		List<Object> pageAndAlarm = alarmService.selectAlarmListWithReceiverIdx(memberAccount.getMemberIdx(),page);

		model.addAttribute("paging", pageAndAlarm.get(0));
		model.addAttribute("alarms", pageAndAlarm.get(1));
	}
	
	@GetMapping("alarm/read")
	@ResponseBody
	public void readAlarm(Alarm alarm) {
		alarmService.updateAlarm(alarm);
	}
	
	@PostMapping("alarm/check-rating")
	@ResponseBody
	public String checkRating(@RequestBody Alarm alarm) {
		System.out.println(alarm);
		if(alarmService.checkRating(alarm)) {
			return "exist";
		} else {
			return "nothing";
		}
	}
	
}
