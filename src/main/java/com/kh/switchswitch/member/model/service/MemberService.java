package com.kh.switchswitch.member.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;

import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.member.model.dto.KakaoLogin;
import com.kh.switchswitch.member.model.dto.Member;
import com.kh.switchswitch.member.validator.JoinForm;

public interface MemberService extends UserDetailsService {
	
	UserDetails loadUserByUsername(String username);
	
	void insertMember(JoinForm form);

	void authenticateByEmail(JoinForm form, String token);
	
	Member selectMemberByEmailAndDelN(String memberEmail);

	Member selectMemberByNicknameAndDelN(String memberNick);

	KakaoLogin selectKakaoLoginById(String id);
	
	void insertMemberWithKakao(Member member, String id);

	void updateMemberDelYN(Member member);
	
	void updateMemberDelYNForLeave(Member member);
	
	void updateMemberWithFile(Member member, MultipartFile profileImage);

	String getAccessTokenJsonData(String code);

	String getUserInfo(String accessToken);
	
	boolean checkNickName(String nickName);

	FileDTO selectFileInfoByFlIdx(int flIdx);

	void logoutKakao(String accessToken);

	String selectEmailByNicknameAndTell(String nickname, String tell);

	void reissuePwAndSendToEmail(Member foundMember);

	void updateMemberPass(int memberIdx, String id);

	String selectMemberNickWithMemberIdx(Integer requestMemIdx);

	List<Map<String, Object>> selectMembersTop5();
}