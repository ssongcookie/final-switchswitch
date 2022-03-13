package com.kh.switchswitch.common.mail.handler;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MailHandler {
	
	@PostMapping("mail")
	public String writeMailTemplate(@RequestParam Map<String, String> template, Model model) {
		if(template.containsKey("persistToken")) {
			model.addAttribute("persistToken",template.get("persistToken"));
		}
		return "mail-template/"+template.get("mail-template");
	}
	
	
	
	
}
