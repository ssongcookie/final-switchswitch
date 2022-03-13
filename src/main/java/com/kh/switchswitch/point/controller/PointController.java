package com.kh.switchswitch.point.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.gson.Gson;
import com.kh.switchswitch.member.model.dto.MemberAccount;
import com.kh.switchswitch.point.model.dto.InquiryRealName;
import com.kh.switchswitch.point.model.dto.PointHistory;
import com.kh.switchswitch.point.model.dto.PointRefund;
import com.kh.switchswitch.point.model.dto.SavePoint;
import com.kh.switchswitch.point.model.service.PointService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("point")
public class PointController {
	
	private PointService pointService;
	
	private IamportClient api;
	
	public PointController(PointService pointService) {
		this.pointService = pointService;
		// REST API 키와 REST API secret 를 아래처럼 순서대로 입력한다.
		this.api = new IamportClient("7002192506815642","52530b66901a86e01810d0a822adfc6f641449e0e1614b7c7e87d43dd8aaab73e87dfee87c55bab2");
	}

	@GetMapping("point-charge")
	public void pointCharge(@AuthenticationPrincipal MemberAccount memberAccount, Model model) {
		SavePoint savePoint = pointService.selectSavePointByMemberIdx(memberAccount.getMemberIdx());
		model.addAttribute("savePoint", savePoint);
		
	}
	
	
	
	
	@GetMapping("point-return")
	public void pointReturn(@AuthenticationPrincipal MemberAccount memberAccount, Model model) {
		SavePoint savePoint = pointService.selectSavePointByMemberIdx(memberAccount.getMemberIdx());
		model.addAttribute("savePoint", savePoint);
	}
	
	@GetMapping("point-return2")
	public void pointReturn2() {}
		
	@ResponseBody
	@RequestMapping(value="/verifyIamport/{imp_uid}")
	public IamportResponse<Payment> paymentByImpUid(
			Model model
			,@RequestBody PointHistory pointHistory
			,@AuthenticationPrincipal MemberAccount memberAccount
			, Locale locale
			, HttpSession session
			, @PathVariable(value= "imp_uid") String imp_uid) throws IamportResponseException, IOException
	{	
			
		pointService.chargePoint(memberAccount.getMemberIdx(),pointHistory.getPoints());
		return api.paymentByImpUid(imp_uid);
	}
	
	@ResponseBody
	@GetMapping(value="checkAccount", produces = "text/html; charset=utf-8")
	public String checkAccount(@ModelAttribute InquiryRealName inquiryRealName){
		String userInfo = pointService.checkAccount(inquiryRealName);
 		if(userInfo != null) {
			return userInfo;
		}
		
		return "실패";
	}
	
	@GetMapping("refund")
	public String refundPoint(@AuthenticationPrincipal MemberAccount certifiedMember , PointRefund pointRefund, Model model){
		
		switch(pointRefund.getBankName()){
		   case "004" : pointRefund.setBankName("국민은행"); break;
		   case "020" : pointRefund.setBankName("우리은행"); break;
		   case "081" : pointRefund.setBankName("하나은행"); break;
		   case "088" : pointRefund.setBankName("신한은행"); break;
		   case "090" : pointRefund.setBankName("카카오뱅크"); break;
		}
		
		pointService.refundPoint(certifiedMember, pointRefund);
		
		model.addAttribute("msg", "환급신청이 완료되었습니다.");
		model.addAttribute("url", "/point/point-return");
		
		return "common/result";
	}
	
	@GetMapping("point-history")
	public void pointHistory(Model model, @AuthenticationPrincipal MemberAccount member) {
		SavePoint savePoint = pointService.selectSavePointByMemberIdx(member.getMemberIdx());
		model.addAttribute("savePoint",savePoint );
	}
	

	@ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    @GetMapping("total-point-history")
	public String totalPointHistoryJason(@AuthenticationPrincipal MemberAccount member,HttpServletResponse response) {
		
		response.addHeader("Access-Control-Allow-Origin","*");
		
		String json = new Gson().toJson(pointService.selectPointHistoryByMemIdx(member.getMemberIdx()));
		log.info("json={}" ,json);
		return json;
	}
		
	@ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    @GetMapping("charge-point-history")
	public String chargePoinHistoryJason(@AuthenticationPrincipal MemberAccount member,HttpServletResponse response) {
		
		response.addHeader("Access-Control-Allow-Origin","*");
		
		String json = new Gson().toJson(pointService.selectPointHistoryByMemIdxAndType(member.getMemberIdx(),"충전"));		
		log.info("json={}" ,json);
		return json;
	}
	
	@ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    @GetMapping("use-point-history")
	public String usePoinHistoryJason(@AuthenticationPrincipal MemberAccount member,HttpServletResponse response) {
		
		response.addHeader("Access-Control-Allow-Origin","*");
		System.out.println("사용");
		String json = new Gson().toJson(pointService.selectPointHistoryByMemIdxAndType(member.getMemberIdx(),"사용"));		
		log.info("json={}" ,json);
		return json;
	}
	
	
	@ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    @GetMapping("refund-point-history")
	public String refundPoinHistoryJason(@AuthenticationPrincipal MemberAccount member,HttpServletResponse response) {
		
		response.addHeader("Access-Control-Allow-Origin","*");
		
		String json = new Gson().toJson(pointService.selectPointHistoryByMemIdxAndType(member.getMemberIdx(),"환급"));
		
		log.info("json={}" ,json);
		return json;
	}

}
