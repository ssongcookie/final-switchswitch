package com.kh.switchswitch.point.model.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kh.switchswitch.card.model.dto.CardRequestList;
import com.kh.switchswitch.member.model.dto.MemberAccount;
import com.kh.switchswitch.point.model.dto.InquiryRealName;
import com.kh.switchswitch.point.model.dto.PointHistory;
import com.kh.switchswitch.point.model.dto.PointRefund;
import com.kh.switchswitch.point.model.dto.SavePoint;
import com.kh.switchswitch.point.model.repository.InquiryRealNameRepository;
import com.kh.switchswitch.point.model.repository.PointHistoryRepository;
import com.kh.switchswitch.point.model.repository.PointRefundRepository;
import com.kh.switchswitch.point.model.repository.SavePointRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService{
	
	private final SavePointRepository savePointRepository;
	private final RestTemplate http;
	private final InquiryRealNameRepository inquiryRealNameRepository;
	private final PointRefundRepository pointRefundRepository;
	private final PointHistoryRepository pointHistoryRepository;

	public void updateSavePointWithAvailableBal(int availableBal, int memberIdx) {
		SavePoint savePoint = new SavePoint();
		savePoint.setAvailableBal(availableBal);
		savePoint.setMemberIdx(memberIdx);
		savePointRepository.updateSavePoint(savePoint);
	}

	public void updateSavePoint(CardRequestList cardRequestList) {
		SavePoint savePoint = savePointRepository.selectSavePointByMemberIdx(cardRequestList.getRequestMemIdx());
		savePoint.setAvailableBal(savePoint.getAvailableBal()+cardRequestList.getPropBalance());
		savePointRepository.updateSavePoint(savePoint);
	}

	public String checkAccount(InquiryRealName inquiryRealName) {
		
		String accessToken = "";
		
		MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
		body.add("client_id", "4bd8e1e3-6c00-4aa1-bb89-46613a436a04");
		body.add("client_secret", "98888e4c-9600-4675-9f1a-f5e3ccbf7bb1");
		body.add("scope", "oob");
		body.add("grant_type", "client_credentials");
		
		//RestTemplate의 기본 ContentType이 application/json이다.
		RequestEntity<MultiValueMap<String, String>> request =
				RequestEntity.post("https://testapi.openbanking.or.kr/oauth/2.0/token")
				.accept(MediaType.APPLICATION_FORM_URLENCODED)
				.body(body);
		
		ResponseEntity<String> responseEntity = http.exchange(request, String.class);
	
		if (responseEntity.getStatusCode() == HttpStatus.OK) {
			try {
				ObjectMapper objMapper = new ObjectMapper();
				Map<String, String> objMap = objMapper.readValue(responseEntity.getBody(), Map.class);
				
				accessToken = objMap.get("access_token");
			} catch (JsonProcessingException e) {
				e.printStackTrace();
				return null;
			}
        }
		
		//은행연결하려면? 처음에 사용자등록 필요
		ObjectMapper obj = new ObjectMapper();
		LocalDateTime now = LocalDateTime.now();
		String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
		String bankTranId = inquiryRealNameRepository.selectLastBankTranId() == null ? 
											"M202113699U111111420" : inquiryRealNameRepository.selectLastBankTranId() ;
		int backNum = Integer.valueOf(bankTranId.substring(11), 10) + 1;
		String newBankTranId = bankTranId.substring(0, 11) + Integer.toString(backNum);
		System.out.println(newBankTranId);
		
		Map<String, String> bodyForRealName = new HashMap<>();
		bodyForRealName.put("bank_tran_id", newBankTranId);
		bodyForRealName.put("bank_code_std", inquiryRealName.getBankCodeStd());
		bodyForRealName.put("account_num", inquiryRealName.getAccountNum());
		bodyForRealName.put("account_holder_info_type", " ");
		bodyForRealName.put("account_holder_info", inquiryRealName.getAccountHolderInfo());
		bodyForRealName.put("tran_dtime", formatedNow);
		
		try {
			String jsonRealName = obj.writerWithDefaultPrettyPrinter().writeValueAsString(bodyForRealName);
			
			System.out.println(accessToken);
			
			RequestEntity<String> requestRealName =
					RequestEntity.post("https://testapi.openbanking.or.kr/v2.0/inquiry/real_name")
					.accept(MediaType.APPLICATION_JSON)
					.header("Authorization", "Bearer " + accessToken)
					.body(jsonRealName);
			
			ResponseEntity<String> responseEntityRealName = http.exchange(requestRealName, String.class);
			
			InquiryRealName irn = new InquiryRealName();
			irn.setBankTranId(newBankTranId);
			irn.setBankCodeStd(inquiryRealName.getBankCodeStd());
			irn.setAccountNum(inquiryRealName.getAccountNum());
			irn.setAccountHolderInfo(inquiryRealName.getAccountHolderInfo());
			irn.setTranDtime(formatedNow);
			inquiryRealNameRepository.insertInquiryRealName(irn);
			
			if (responseEntityRealName.getStatusCode() == HttpStatus.OK) {
				try {
					ObjectMapper objMapper = new ObjectMapper();
					Map<String, String> objMap = objMapper.readValue(responseEntityRealName.getBody(), Map.class);
					
					for (String string : objMap.keySet()) {
						System.out.println(string + ":" + objMap.get(string));
					} 
					
					String accountNum = objMap.get("account_num");
					String accountHolderName = objMap.get("account_holder_name");
					String bankName = objMap.get("bank_name");
					String bankCode = objMap.get("bank_code_std");
					
					return accountNum + "," + accountHolderName + "," + bankName + "," + bankCode;
					
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
	        }
			
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		
		return null;
	}

	public void refundPoint(MemberAccount certifiedMember, PointRefund pointRefund) {
		//환급 객체 생성 후 DB에 insert
		pointRefund.setMemberIdx(certifiedMember.getMemberIdx());
		pointRefundRepository.insertPointRefund(pointRefund);
		//가용 포인트 차감(refundPoint) balance는 관리자 확인  후 차감
		SavePoint savePoint = savePointRepository.selectSavePointByMemberIdx(certifiedMember.getMemberIdx());
		Integer availableBal = savePoint.getAvailableBal() - pointRefund.getRefundPoint();
		savePoint.setAvailableBal(availableBal);
		//가용 포인트 DB 업데이트 후 point_history 객체 생성
		if(savePointRepository.updateSavePoint(savePoint)) {
			PointHistory pointHistory = new PointHistory();
			pointHistory.setContent("심사중");
			pointHistory.setPoints(pointRefund.getRefundPoint());
			pointHistory.setResultPoint(savePoint.getBalance());
			pointHistory.setType("환전");
			pointHistory.setUserIdx(certifiedMember.getMemberIdx());
			savePointRepository.registHistory(pointHistory);
		}
	}

	public SavePoint selectSavePointByMemberIdx(Integer memberIdx) {
		return savePointRepository.selectSavePointByMemberIdx(memberIdx);
	}

	public void chargePoint(Integer memberIdx, Integer points) {
		PointHistory pointHistory = new PointHistory();
		pointHistory.setUserIdx(memberIdx);
		pointHistory.setPoints(points);
		pointHistory.setResultPoint(savePointRepository.selectSavePointByMemberIdx(memberIdx).getBalance());
		pointHistory.setType("충전");
		
		pointHistoryRepository.insertPointHistory(pointHistory);
		
		SavePoint savePoint = savePointRepository.selectSavePointByMemberIdx(memberIdx);
		savePoint.setBalance(savePoint.getBalance()+points);
		savePoint.setAvailableBal(savePoint.getAvailableBal() + points);
		
		savePointRepository.updateSavePoint(savePoint);
	}

	
	public List<PointHistory> selectPointHistoryByMemIdx(Integer memberIdx) {
		return pointHistoryRepository.selectPoinHistoryBymemberIdx(memberIdx);
	}

	
	public List<PointHistory> selectPointHistoryByMemIdxAndType(Integer memberIdx, String type) {
	    return pointHistoryRepository.selectPoinHistoryBymemberIdxAndType(memberIdx,type);
	}
	
	
}
