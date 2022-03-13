package com.kh.switchswitch.point.model.repository;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.kh.switchswitch.point.model.dto.InquiryRealName;

@Mapper
public interface InquiryRealNameRepository {
	
	@Insert("insert into inquiry_real_name values(#{bankTranId}, #{bankCodeStd}, #{accountNum}, #{accountHolderInfo}, #{tranDtime} )")
	void insertInquiryRealName(InquiryRealName inquiryRealName);
	
	@Select("select bank_tran_id from (select bank_tran_id from inquiry_real_name order by bank_tran_id desc) where rownum = 1")
	String selectLastBankTranId();
	
}
