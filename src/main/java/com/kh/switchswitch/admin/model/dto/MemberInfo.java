package com.kh.switchswitch.admin.model.dto;

import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.member.model.dto.Member;

import lombok.Data;

@Data
public class MemberInfo {
	private Member member;
	private FileDTO fileDTO;
}
