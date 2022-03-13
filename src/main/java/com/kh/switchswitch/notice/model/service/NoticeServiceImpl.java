package com.kh.switchswitch.notice.model.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.common.util.FileUtil;
import com.kh.switchswitch.common.util.pagination.Paging;
import com.kh.switchswitch.common.util.pagination.PagingV2;
import com.kh.switchswitch.notice.model.dto.Notice;
import com.kh.switchswitch.notice.model.repository.NoticeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService{
	
	private final NoticeRepository noticeRepository;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void insertNotice(Notice notice, List<MultipartFile> mfs) {
		FileUtil fileUtil = new FileUtil();
		noticeRepository.insertNotice(notice);
		
		for (MultipartFile multipartFile : mfs) {
			if (!multipartFile.isEmpty()) {
				noticeRepository.insertFileInfo(fileUtil.fileUpload(multipartFile));
			}
		}
		
	}

	public Map<String, Object> selectNoticeList(int page) {
		int cntPerPage = 10;
		int total = (noticeRepository.selectContentCnt());
		int nowPage = page;
		String url = "/notice/notice-list2";
		
		PagingV2 pagingV2 = new PagingV2(total, nowPage, cntPerPage, url);
				
		Map<String,Object> commandMap = new HashMap<String,Object>();
		commandMap.put("paging", pagingV2);
		commandMap.put("noticeList", noticeRepository.selectNoticeListWithPageNo(Map.of("startBoard",pagingV2.getStartAlarm(),"lastBoard",pagingV2.getEndAlarm())));
		return commandMap;
		
	}

	public Map<String, Object> selectNoticeByIdx(Integer noticeIdx) {
		Notice notice = noticeRepository.selectNoticeByIdx(noticeIdx);
		List<FileDTO> files = noticeRepository.selectFilesByIdx(noticeIdx);
		return Map.of("notice",notice,"files",files);
	}

	@Transactional
	public void modifyNotice(List<MultipartFile> mfs,Notice notice) {
		FileUtil fileUtil = new FileUtil();
		
		noticeRepository.modifyNotice(notice);
		//삭제
		noticeRepository.deleteFileInfo(notice.getNoticeIdx());
		
		for (MultipartFile multipartFile : mfs) {
			if (!multipartFile.isEmpty()) {
				noticeRepository.modifyFileInfo(fileUtil.fileUpload(multipartFile),notice.getNoticeIdx());
			}
		}
	}

	public void deleteNotice(Integer noticeIdx) {
		noticeRepository.deleteNotice(noticeIdx);
		
	}














	
	

	
	
	
	
	
	
	
	
	
}
