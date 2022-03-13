package com.kh.switchswitch.point.model.repository;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kh.switchswitch.point.model.dto.PointHistory;

@Mapper
public interface PointHistoryRepository {
	
	@Insert("insert into point_history values(sc_ph_idx.nextval,#{userIdx},#{type},#{points},#{resultPoint},sysdate,null)")
	void insertPointHistory(PointHistory pointHistory);

	@Select("select * from point_history where user_idx=#{memberIdx}")
	List<PointHistory> selectPoinHistoryBymemberIdx(Integer memberIdx);

	@Select("select * from point_history where user_idx=#{memberIdx} and type=#{type}")
	List<PointHistory> selectPoinHistoryBymemberIdxAndType(@Param("memberIdx") Integer memberIdx, @Param("type")String type);
}
