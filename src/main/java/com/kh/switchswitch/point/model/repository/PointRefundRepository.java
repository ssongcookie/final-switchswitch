package com.kh.switchswitch.point.model.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import com.kh.switchswitch.point.model.dto.PointRefund;

@Mapper
public interface PointRefundRepository {

	@Insert("insert into point_refund(PR_IDX, MEMBER_IDX, REFUND_POINT, BANK_NAME, ACCOUNT) "
			+ " values(sc_pr_idx.nextval, #{memberIdx}, #{refundPoint}, #{bankName}, #{account})")
	void insertPointRefund(PointRefund pointRefund);
	
}
