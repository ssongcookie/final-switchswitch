package com.kh.switchswitch.board.model.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.switchswitch.board.model.dto.Board;
import com.kh.switchswitch.board.model.dto.Reply;
import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.common.util.pagination.Paging;
import com.kh.switchswitch.common.util.pagination.PagingV2;

@Mapper
public interface BoardRepository {

	//글작성
	@Insert("insert into community(bd_idx, user_id, title, content)"
			+ " values(sc_bd_idx.nextval, #{userId}, #{title}, #{content})")
	void insertBoard(Board board);

	//상세글조회 
	@Select("select * from community where bd_idx = #{bdIdx}")
	Board selectBoardByIdx(int bdIdx);
	
	 //게시글목록
	 @Select("select * from community where  is_del=0 ORDER BY BD_IDX DESC")
	List<Board> selectBoardList(PagingV2 pageUtil);

	//총 게시글 갯수 출력
	@Select("select count(*) from community where is_del = 0")
	int selectContentCnt();

	//수정
	void modifyBoard(Board board);

	//파일업로드
	@Insert("insert into file_info(fl_idx,bd_idx,origin_file_name, rename_file_name, save_path)"
			+ " values(sc_file_idx.nextval, sc_bd_idx.currval, #{originFileName}, #{renameFileName}, #{savePath})")
	void insertFileInfo(FileDTO fileUpload);
	
	@Select("select * from file_info  where is_del= 0 and bd_idx = #{bdIdx} ORDER BY REG_DATE DESC")
	List<FileDTO> selectFilesByBdIdx(int bdIdx);

	@Update("update community set is_del = 1 where bd_idx = #{bdIdx}")	
	void deleteBoard(int bdIdx);
	//파일업로드
	@Insert("insert into file_info(fl_idx,origin_file_name, rename_file_name, save_path,bd_idx)"
			+ " values(sc_file_idx.nextval, #{fileUpload.originFileName},#{fileUpload.renameFileName},#{fileUpload.savePath}, #{bdIdx})")
	void modifyFileInfo(@Param("fileUpload")FileDTO fileUpload, @Param("bdIdx")Integer bdIdx);

	
	
	@Select("select * from (select rownum rnum, communityr.* from (select community.* from community community"
			+ " where is_del=0 ORDER BY BD_IDX DESC) communityr"
			+ " ) where rnum between #{startBoard} and #{lastBoard}")
	List<Board> selectBoardListWithPageNo(Map<String, Integer> map);

	@Select("SELECT * FROM reply WHERE  is_del=0 and bd_idx=#{bdIdx} ORDER BY cm_idx DESC")
	List<Reply> getCommentList(Integer bdIdx);

	@Insert("insert into reply(CM_IDX, CM_PARENT,BD_IDX,USER_ID,CONTENT)"
			+ " values (SC_CM_IDX.nextval, SC_CM_PARENT_IDX.nextval, #{bdIdx},#{userId},#{content})")
	void insertReplyDepth1(Reply reply);
	
	@Select("select nvl (max(cm_order),0) from reply WHERE bd_idx=#{bdIdx}")
	int selectLastOrderOfBoard(int bdIdx);

	@Update("update reply set reg_date = SYSDATE, content = #{content} where cm_idx = #{cmIdx}")	
	void modifyReply(Reply reply);

	@Update("update reply set is_del = 1 where cm_idx = #{cmIdx}")	
	void deleteReply(int cmIdx);
	
	@Update("update file_info set is_del = 1 where bd_idx=#{bdIdx}")
	void deleteBoardImg(Integer bdIdx);

	@Select("select * from community where is_del=0 and bd_idx=#{bdIdx} order by reg_date desc")
	Board selectBoardModifyBdIdx(int bdIdx);

	@Select("select * from file_info where is_del= 0 and bd_idx=#{bdIdx}")
	List<FileDTO> selectFileInfoBybdIdx(int bdIdx);





	
}
