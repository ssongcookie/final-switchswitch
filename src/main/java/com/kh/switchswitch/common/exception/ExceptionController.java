package com.kh.switchswitch.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ExceptionController {
	
	@RequestMapping("/error/404.html")
	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public String resourceNotFound() {
		log.info("존재하지 않는 페이지");
		return "/error/404";
	}
	@RequestMapping("/error/403")
	public String unauthorizedUser() {
		log.info("접근권한에러 페이지");
		return "/error/403";
	}
}
