package com.kh.switchswitch.common.util;

import java.sql.Date;

import lombok.Data;

@Data
public class FileDTO {
	private Integer flIdx;
	private Integer bdIdx;
	private String originFileName;
	private String renameFileName;
	private String savePath;
	private Date regDate;
	private Integer isDel;
	private Integer cardIdx;
	
	public String getDownloadURL() {
		return"/file/"+ savePath + renameFileName;
	}
}
