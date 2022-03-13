package com.kh.switchswitch.exchange.model.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kh.switchswitch.exchange.model.dto.Rating;

@Mapper
public interface RatingRepository {

	//Optional<> 이용시 tooManyResult 오류 발생하여 걷어냄
	@Select("select rating from rating where user_idx=#{certifiedMemberIdx}")
	List<Float> selectRatingByMemberIdx(int certifiedMemberIdx);

	@Select("select rating from rating where user_idx = #{memberIdx}")
	List<Integer> selectMyRateCnt(int memberIdx);

	@Select("select count(*) from rating where user_idx = #{memberIdx} and eh_idx=#{ehIdx}")
	Integer selectRatingByMemIdxAndEhIdx(@Param("memberIdx") Integer memberIdx,@Param("ehIdx") Integer ehIdx);

	@Insert("insert into rating values(sc_rating_idx.nextval, #{ehIdx}, #{userIdx}, #{rating})")
	void insertRating(Rating rating);

	@Select("select avg(rating) from rating where user_idx=#{requestedMemIdx}")
	Integer calAvgRate(Integer requestedMemIdx);
	
	
}
