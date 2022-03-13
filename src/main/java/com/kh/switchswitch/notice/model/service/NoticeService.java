package com.kh.switchswitch.notice.model.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.kh.switchswitch.notice.model.dto.Notice;

public interface NoticeService { //전자정부 프레임워크에서 interface를 사용하는 것이 다수 관습으로 남아있음

	void insertNotice(Notice notice, List<MultipartFile> mfs);

	Map<String,Object>selectNoticeList(int page);

	Map<String, Object> selectNoticeByIdx(Integer noticeIdx);

	void modifyNotice(List<MultipartFile> mfs, Notice notice);

	void deleteNotice(Integer noticeIdx);





}
