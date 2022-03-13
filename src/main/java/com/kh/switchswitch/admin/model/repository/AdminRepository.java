package com.kh.switchswitch.admin.model.repository;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.lang.Nullable;

import com.kh.switchswitch.admin.model.dto.Code;
import com.kh.switchswitch.admin.model.dto.Menu;
import com.kh.switchswitch.card.model.dto.Card;
import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.member.model.dto.Member;
import com.kh.switchswitch.point.model.dto.PointHistory;
import com.kh.switchswitch.point.model.dto.PointRefund;
import com.kh.switchswitch.point.model.dto.SavePoint;

@Mapper
public interface AdminRepository {
	
	@Select("select * from card where is_del = 0 and rownum <= 32")
	List<Card> selectCardList();
	
	@Select("select * from card where is_del = 0 and isfree = 'N' rownum <= 32")
	List<Card> selectCardListByTrade();
	
	@Select("select * from card where is_del = 0 and isfree = 'Y' rownum <= 32")
	List<Card> selectCardListByFree();
	
	//@Select("select * from card where is_del = 0")
	List<Card> selectCardsDetail(@Param("searchPeriod")String searchPeriod, @Param("searchType")String searchType, @Nullable@Param("searchKeyword")String searchKeyword,
								 @Param("page")Integer page, @Param("cntPerPage")Integer cntPerPage);
	
	@Select("select * from file_info where card_idx is not null and card_idx = #{cardIdx}")
	List<FileDTO> selectCardImgListByCardIdx(Integer cardIdx);
	
	@Select("select card_idx from card where is_del = 0")
	List<Integer> selectCardIdx();
	
	@Update("update card set is_del = 1 where card_idx = #{cardIdx}")
	Integer deleteCard(Integer cardIdx);
	
	@Update("update file_info set is_del = 1 where card_idx = #{cardIdx}")
	void deleteImg(Integer cardIdx);
	
	//@Select("select * from member where member_del_yn=0 and #{searchType}=#{keyword}")
	List<Member> selectMemberAllList(@Param("searchType") String searchType, @Param("keyword") String keyword, 
									 @Param("page")Integer page, @Param("cntPerPage")Integer cntPerPage);
	
	//@Select("select * from member where code = 'D' and member_del_yn=0")
	List<Member> selectMemberBlackList(@Param("searchType") String searchType, @Param("keyword") String keyword,
									   @Param("page")Integer page, @Param("cntPerPage")Integer cntPerPage);
	
	@Insert("insert into menu(url_idx,code,url,url_name,position,depth,parent) values(sc_url_idx.nextval, #{code},#{url},#{urlName},#{position},#{depth},#{parent})")
	void insertMenu(Menu Menu);
	
	@Select("select * from menu where depth = '1' and parent = '없음' and position = '메뉴바' order by seq asc")
	List<Menu> selectMenuList();
	
	@Select("select * from menu where depth = '1' and parent = '없음' and position = '사이드바' order by seq asc" )
	List<Menu> selectSideMenu();
	
	@Select("select * from menu where parent = #{parent} and depth = '2' order by seq asc")
	List<Menu> selectChildMenu(String parent);
	
	@Select("select distinct url_name from menu where depth = '1' and parent = '없음' and position = '메뉴바'")
	List<Menu> selectParentsMenuListByMenu();
	
	@Select("select distinct url_name from menu where depth = '1' and parent = '없음' and position = '사이드바'")
	List<Menu> selectParentsMenuListBySideMenu();
	
	@Select("select * from code")
	List<Code> selectCodeList();
	
	@Select("select * from menu")
	List<Menu> selectMenuAllList();
	
	@Delete("delete from menu where url_idx = #{urlIdx}")
	void deleteMenu(int urlIdx);

	@Update("update member set code=#{code} where member_idx=#{memberIdx}")
	void updateMemberCode(@Param("code")String code,@Param("memberIdx") int memberIdx);
	
	@Select("select * from member where member_idx = #{memberIdx} and member_del_yn=0")
	Member selectMemberByIdx(int memberIdx);
	
	@Select("select * from file_info where fl_idx = #{flIdx} and is_del=0")
	FileDTO selectFileByMemberIdx(int flIdx);
	
	void updateMemberInfo(Member convertToMember);
	
	@Select("select * from member where member_nick = #{memberNick}")
	Member selectMemberByNickName(@Param("memberNick") String memberNick);
	
	@Update("update member set member_del_yn=1,member_del_date=sysdate where member_Idx=#{memberIdx}")
	void deleteMember(Integer memberIdx);
	
	@Update("update file_info set is_del=1 where fl_Idx = #{flIdx}")
	void deleteMemberProfileImg(Integer flIdx);
	
	@Select("select count(*) from card where member_idx = #{memberIdx} and is_del = 0")
	Integer selectCardCountByMemberIdx(Integer memberIdx);
	
	@Select("select * from file_info where card_idx is not null and card_idx = #{cardIdx}")
	List<FileDTO> selectFileInfoByCardIdx(Integer cardIdx);
	
	@Select("select * from card where member_idx = #{memberIdx} and rownum < 6 and is_del = 0")
	List<Card> selectCardListByMemberIdx(int memberIdx);
	
	@Select("select * from file_info where card_idx is not null and is_del = 0 and rownum <= 32")
	List<FileDTO> selectCardImgList();
	
	@Select("select card_idx from file_info where fl_idx = #{flIdx}")
	Integer selectCardIdxByflIdx(Integer flIdx);
	
	//@Select("select count(*) from member where member_del_yn=0")
	Integer memberCount(@Param("searchType") String searchType, @Param("keyword") String keyword);

	Integer memberBlackListCount(@Param("searchType")String searchType, @Param("keyword")String keyword);

	Integer cardCount(@Param("searchPeriod")String searchPeriod, @Param("searchType")String searchType, @Param("searchKeyword")String searchKeyword);

	List<PointRefund> selectRefundHistoryList(@Param("statusCode")String statusCode, @Param("page")Integer page, @Param("cntPerPage")Integer cntPerPage);
	
	@Select("select * from member where member_email = #{memberEmail}")
	Member selectMemberByEmail(String memberEmail);
	
	@Update("update point_refund set admin_name=#{adminName},status_code=#{statusCode} where pr_idx = #{prIdx}")
	void updateStatusCode(@Param("statusCode") String statusCode, @Param("adminName")String adminName, @Param("prIdx") Integer prIdx);

	Member selectMemberByIdxWithDetail(@Param("memberIdx")Integer memberIdx, @Param("searchType")String searchType, @Param("searchKeyword")String searchKeyword);
	
	List<PointRefund> selectRefundHistoryListForCount(String statusCode);
	
	@Select("select * from point_history where user_idx = #{memberIdx} and rownum < 4")
	List<PointHistory> selectPointHistoriesByMemberIdxFromAll(Integer memberIdx);
	
	@Select("select * from point_history where user_idx = #{memberIdx} and type = '사용' and rownum < 4")
	List<PointHistory> selectPointHistoriesByMemberIdxFromUse(Integer memberIdx);
	
	@Select("select * from point_history where user_idx = #{memberIdx} and type = '충전' and rownum < 4")
	List<PointHistory> selectPointHistoriesByMemberIdxFromAllSave(Integer memberIdx);
	
	@Select("select balance from save_point where member_idx = #{memberIdx}")
	Integer selectPointByMemberIdx(Integer memberIdx);
	
	@Select("select * from save_point where member_idx = #{memberIdx}")
	SavePoint selectGetPoint(Integer memberIdx);
	
	@Update("update save_point set balance = #{availableBal}-#{refundPoint} where member_idx = #{memberIdx}")
	void updatePointByComplate(@Param("memberIdx")Integer memberIdx, @Param("availableBal")Integer balance, @Param("refundPoint")Integer refundPoint);
	
	@Update("update save_point set balance = #{availableBal}+#{refundPoint} where member_idx = #{memberIdx}")
	void updatePointByCencel(@Param("memberIdx")Integer memberIdx,@Param("availableBal")Integer availableBal,@Param("refundPoint")Integer refundPoint);
	
	@Select("select status_code from point_refund where pr_idx = #{prIdx}")
	String selectStatusCodeByPrIdx(Integer prIdx);
	
	@Select("select count(confirm_check) from point_refund where confirm_check = 'N'")
	Integer selectRefundNewCount();
	
	@Select("select count(*) from point_refund where confirm_check = 'N'")
	Integer selectConfirmCheck();
	
	@Update("update point_refund set confirm_check = 'Y' where confirm_check = 'N'")
	void updateConfirmCheck();
	
	@Select("select code from member where member_idx = #{memberIdx}")
	String selectCheckAdmin(Integer memberIdx);
	
	@Select("select member_idx from point_refund where pr_idx = #{prIdx}")
	Integer selectMemberIdxByPrIdxFromPointRefund(Integer prIdx);
	
	@Insert("insert into point_history(ph_idx,user_idx,type,points,reg_date,content) values(sc_ph_idx.nextval,#{userIdx},#{type},#{points},sysdate,#{content})")
	void insertHistory(PointHistory pointHistory);
	
	
	
	
	


	

	

	
	

	

	

	

	

	

	


}
