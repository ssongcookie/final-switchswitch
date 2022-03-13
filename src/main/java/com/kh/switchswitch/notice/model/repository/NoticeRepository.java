package com.kh.switchswitch.notice.model.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.common.util.pagination.Paging;
import com.kh.switchswitch.common.util.pagination.PagingV2;
import com.kh.switchswitch.notice.model.dto.Notice;

@Mapper
public interface NoticeRepository {

	@Insert("insert into notice(notice_idx, user_id, title, content, type)"
			+ " values(sc_notice_idx.nextval, #{userId}, #{title}, #{content}, #{type})")
	void insertNotice(Notice notice);
	
	@Insert("insert into file_info(fl_idx,bd_idx,origin_file_name, rename_file_name, save_path)"
			+ " values(sc_file_idx.nextval, sc_notice_idx.currval, #{originFileName}, #{renameFileName}, #{savePath})")
	void insertFileInfo(FileDTO fileUpload);
	
	@Select("select * from notice where  is_del=0 ORDER BY notice_idx DESC")
	List<Notice>  selectNoticeList(PagingV2 pageUtil);
	
	@Select("select * from notice where notice_idx = #{noticeIdx}")
	Notice selectNoticeByIdx(Integer noticeIdx);

	@Select("select * from file_info  where is_del= 0 and bd_idx = #{noticeIdx} ORDER BY REG_DATE DESC")
	List<FileDTO> selectFilesByIdx(int bdIdx);
	
	//총 게시글 갯수 출력
	@Select("select count(*) from notice where is_del = 0")
	int selectContentCnt();

	void modifyNotice(Notice notice);
	
	@Update("update file_info set is_del = 1 where bd_idx=#{noticeIdx}")
	void deleteFileInfo(Integer noticeIdx);
	
	@Insert("insert into file_info(FL_IDX,ORIGIN_FILE_NAME,RENAME_FILE_NAME,SAVE_PATH,BD_IDX) "
			+ " values(sc_file_idx.nextval,#{fileUpload.originFileName},#{fileUpload.renameFileName},#{fileUpload.savePath}"
			+ " ,#{noticeIdx})")
	void modifyFileInfo(@Param("fileUpload")FileDTO fileUpload,  @Param("noticeIdx")Integer noticeIdx);
	
	@Update("update notice set is_del = 1 where notice_idx = #{noticeIdx}")	
	void deleteNotice(Integer noticeIdx);

	@Select("select * from (select rownum rnum, noticer.* from (select notice.* from notice notice"
			+ " where is_del=0 order by REG_DATE DESC) noticer"
			+ " ) where rnum between #{startBoard} and #{lastBoard}")
	List<Notice> selectNoticeListWithPageNo(Map<String, Integer> map);





	

	

	




	
}
