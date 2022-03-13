package com.kh.switchswitch.point.model.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.switchswitch.point.model.dto.PointHistory;
import com.kh.switchswitch.point.model.dto.SavePoint;

@Mapper
public interface SavePointRepository {

	@Select("select * from save_point where member_idx=#{memberIdx}")
	SavePoint selectSavePointByMemberIdx(int memberIdx);

	boolean updateSavePoint(SavePoint savePoint);
	
	@Insert("insert into save_point(POINT_IDX,MEMBER_IDX) values(sc_point_idx.nextval,sc_member_idx.currval)")
	void insertSavePoint();
	
	@Insert("insert into save_point(POINT_IDX,MEMBER_IDX) values(sc_point_idx.nextval,memberIdx)")
	void insertSavePointWithMemberIdx(Integer memberIdx);
	
	@Insert("insert into point_history(ph_idx,user_idx,type,points,result_point,reg_date,content) values(sc_ph_idx.nextval,#{userIdx},#{type},#{points},#{resultPoint},sysdate,#{content})")
	void registHistory(PointHistory pointHistory);

	
}
