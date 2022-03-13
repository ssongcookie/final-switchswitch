package com.kh.switchswitch.common.util;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.kh.switchswitch.common.code.Config;

@Controller
public class FileHandler {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@GetMapping("download")
	public ResponseEntity<UrlResource> downloadFile(FileDTO file) throws MalformedURLException, UnsupportedEncodingException{
		
		UrlResource resource = new UrlResource(Config.DOMAIN.DESC+file.getDownloadURL());
		//UrlResource resource = new UrlResource("https://www.google.com/search?q=%ED%98%B8%EC%8B%9C&sxsrf=AOaemvIubbRKhVTfHLmyvJeMj3ZkOorqUQ:1635238724508&source=lnms&tbm=isch&sa=X&ved=2ahUKEwjR96_62ufzAhULL6YKHcXuBOYQ_AUoAXoECAEQAw&biw=1536&bih=818&dpr=1.25");
		logger.debug(file.getDownloadURL());
		
		String originFileName = URLEncoder.encode(file.getOriginFileName(), "UTF-8");
		
		ResponseEntity<UrlResource> response = 
				ResponseEntity.ok().header("Content-Disposition", "attachment; filename="+originFileName)
				.body(resource);
		
		return response;
	}
}
