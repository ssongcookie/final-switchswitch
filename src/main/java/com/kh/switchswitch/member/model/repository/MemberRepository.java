package com.kh.switchswitch.member.model.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.member.model.dto.Member;

@Mapper
public interface MemberRepository {
	
	void insertMember(Member member);

	@Select("select * from member where member_nick = #{memberNick}")
	Member selectMemberByNickName(String memberNick);
	
	@Select("select * from member where member_email = #{memberEmail} and member_del_yn = 0")
	Member selectMemberByEmailAndDelN(String memberEmail);
	
	@Select("select * from member where member_idx = #{memberIdx}")
	Member selectMemberByMemberIdx(Integer memberIdx);

	@Select("select * from member where member_nick = #{memberNick} and member_del_yn = 0")
	Member selectMemberByNicknameAndDelN(String memberNick);

	void updateMember(Member member);
	
	void updateMemberForFile(Member member);
	
	@Insert("insert into kakao_login values(sc_kakao_idx.nextval,sc_member_idx.currval,#{id})")
	void insertKakaoLoginWithId(String id);

	@Select("select * from member where member_email = #{memberEmail} and member_del_yn = 1")
	Member selectMemberByEmailAndDelY(String memberEmail);
	
	@Insert("insert into file_info(FL_IDX,ORIGIN_FILE_NAME,RENAME_FILE_NAME,SAVE_PATH) "
			+ " values(sc_file_idx.nextval,#{originFileName},#{renameFileName},#{savePath})")
	void insertFileInfo(FileDTO fileUpload);

	@Select("select * from file_info where fl_idx = #{flIdx}")
	FileDTO selectFileInfoByFlIdx(int flIdx);

	@Select("select * from member where member_email = #{memberEmail}")
	Member selectMemberByEmail(String memberEmail);

	@Select("select member_email from member where member_nick = #{nickname} and member_tell = #{tell} and member_del_yn = 0")
	String selectEmailByNicknameAndTell(Map<String, String> map);
	
	@Select("select * from member where member_idx = #{requestMemIdx}")
	Member selectMemberWithMemberIdx(Integer requestMemIdx);

	@Select("select member_score from member where member_idx = #{memberIdx}")
	Optional<String> selectMemberScoreByMemberIdx(Integer memberIdx);

	@Select("select * from (select m.* from member m where code = 'B' order by member_score) where rownum < 6")
	List<Member> selectMembersTop5();

	@Select("select member_nick from member where member_idx=#{senderIdx}")
	String selectMemberNickByMemberIdx(Integer senderIdx);
	
	@Update("update file_info set is_del = 1 where fl_idx=#{flIdx}")
	void deleteProfileImg(Integer flIdx);
}
