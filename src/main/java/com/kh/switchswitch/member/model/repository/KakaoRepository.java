package com.kh.switchswitch.member.model.repository;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.switchswitch.member.model.dto.KakaoLogin;

@Mapper
public interface KakaoRepository {
	
	@Select("select * from kakao_login where kakao_id = #{id}")
	KakaoLogin selectKakaoLoginById(String id);

	@Insert("insert into kakao_login values(sc_kakao_idx.nextval,#{memberIdx},#{id})")
	void insertKakaoLoginWithMemberIdxAndId(Map<String, Object> of);
	
	@Insert("insert into kakao_login values(sc_kakao_idx.nextval,sc_member_idx.currval,#{id})")
	void insertKakaoLoginWithId(String id);
	

}
