package com.kh.switchswitch.admin.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.kh.switchswitch.admin.model.dto.Code;
import com.kh.switchswitch.admin.model.dto.Menu;
import com.kh.switchswitch.admin.model.repository.AdminRepository;
import com.kh.switchswitch.card.model.dto.Card;
import com.kh.switchswitch.common.code.ErrorCode;
import com.kh.switchswitch.common.exception.HandlableException;
import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.common.util.pagination.Paging;
import com.kh.switchswitch.member.model.dto.Member;
import com.kh.switchswitch.member.model.dto.MemberAccount;
import com.kh.switchswitch.point.model.dto.PointHistory;
import com.kh.switchswitch.point.model.dto.PointRefund;
import com.kh.switchswitch.point.model.dto.SavePoint;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

	@Autowired
	private final AdminRepository adminRepository;
	private final PasswordEncoder passwordEncoder;

	public FileDTO selectMainImgFileByCardIdx(Integer cardIdx) {
		List<FileDTO> fileDTOList = adminRepository.selectFileInfoByCardIdx(cardIdx);
		return fileDTOList.get(0);
	}
	
	public List<Card> selectCardList() {
		List<Card> cardList = adminRepository.selectCardList();
		return cardList;
	}
	
	public List<Card> selectCardListByTrade() {
		List<Card> cardList = adminRepository.selectCardListByTrade();
		return cardList;
	}
	
	public List<Card> selectCardListByFree() {
		List<Card> cardList = adminRepository.selectCardListByFree();
		return cardList;
	}
	
	public List<Map<String, Object>> selectCardListDetail(String searchPeriod, String searchType, String searchKeyword,Integer page) {
		//List<Card> cardList = adminRepository.selectCardsDetail(searchPeriod, searchType, searchKeyword);
		List<Map<String, Object>> cardList = new ArrayList<>();
		Integer cntPerPage = 16;
		System.out.println(searchPeriod);
		List<Card> memberCardList = adminRepository.selectCardsDetail(searchPeriod, searchType, searchKeyword, 1+(page-1)*cntPerPage,page*cntPerPage);
		if (memberCardList != null) {
			for (Card card : memberCardList) {
				FileDTO mainImgFile = adminRepository.selectFileInfoByCardIdx(card.getCardIdx()).get(0);
				List<FileDTO> imgFile = adminRepository.selectFileInfoByCardIdx(card.getCardIdx());
				cardList.add(Map.of("card", card, "fileDTO", mainImgFile, "fileDTOAll", imgFile));
			}
		}
		
		return cardList;
	}
	
	public Paging selectCardPaging(String searchPeriod, String searchType, String searchKeyword, int page) {
		
		Integer cntPerPage = 16;
		Paging pageUtil = Paging.builder()
				.url("/admin/all-cards")
				.total(adminRepository.cardCount(searchPeriod, searchType, searchKeyword))
				.cntPerPage(cntPerPage)
				.blockCnt(5)
				.curPage(page)
				.build();
		return pageUtil;
	}
	
	public List<FileDTO> selectImgFileListByCardIdx(Integer cardIdx) {
		List<FileDTO> fileDTOList = adminRepository.selectFileInfoByCardIdx(cardIdx);
		return fileDTOList;
	}

	public void deleteCard(Integer cardIdx) {
		adminRepository.deleteImg(cardIdx);
		adminRepository.deleteCard(cardIdx);
	}
	
	public void insertMenu(Menu menu) {
		
		try {
			adminRepository.insertMenu(menu);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public List<Menu> selectMenuList() {
		return adminRepository.selectMenuList();
	}

	public List<Menu> selectSideMenu() {
		return adminRepository.selectSideMenu();
	}

	public List<Code> selectCodeList() {
		List<Code> codeList = adminRepository.selectCodeList();
		return codeList;
	}

	public List<Menu> selectParentsMenuListByMenu() {
		List<Menu> parentsMenuList = adminRepository.selectParentsMenuListByMenu();
		return parentsMenuList;
	}

	public List<Menu> selectParentsMenuListBySideMenu() {
		List<Menu> parentsMenuList = adminRepository.selectParentsMenuListBySideMenu();
		return parentsMenuList;
	}

	public void deleteMenu(int urlIdx) {
		adminRepository.deleteMenu(urlIdx);
	}

	public List<Menu> selectChildMenu(String parent) {
		return adminRepository.selectChildMenu(parent);
	}

	public List<Menu> selectMenuAllList() {
		return adminRepository.selectMenuAllList();
	}

	public Map<String, Object> selectMemberAllListByPage(String searchType, String keyword, int page) {
		
		int cntPerPage = 5;
		
		List<Member> memberList = adminRepository.selectMemberAllList(searchType, keyword,1+(page-1)*cntPerPage,page*cntPerPage);
		
		Paging pageUtil = Paging.builder()
				.url("/admin/all-members")
				.total(adminRepository.memberCount(searchType, keyword))
				.cntPerPage(cntPerPage)
				.blockCnt(5)
				.curPage(page)
				.build();
		
		return Map.of("memberList", memberList,"paging",pageUtil);
	}

	public void updateMemberCode(String code, int memberIdx) {
		code = code.equals("B") ? "D" : "B";
		System.out.println(code);
		adminRepository.updateMemberCode(code, memberIdx);
	}

	public Map<String, Object> selectMemberBlackListByPage(String searchType, String keyword, int page) {
		
		int cntPerPage = 5;
		
		List<Member> memberList = adminRepository.selectMemberBlackList(searchType, keyword, 1+(page-1)*cntPerPage,page*cntPerPage);
		
		Paging pageUtil = Paging.builder()
				.url("/admin/black-list-members")
				.total(adminRepository.memberBlackListCount(searchType, keyword))
				.cntPerPage(cntPerPage)
				.blockCnt(5)
				.curPage(page)
				.build();
		return Map.of("memberList", memberList,"paging",pageUtil);
	}

	public Map<String, Object> selectMemberByIdx(Integer memberIdx) {
		Member memberInfo = adminRepository.selectMemberByIdx(memberIdx);
		if(memberInfo.getFlIdx() != null) {
			FileDTO memberImg = adminRepository.selectFileByMemberIdx(memberInfo.getFlIdx());
			return Map.of("member", memberInfo, "memberImg",memberImg);
		}
		return Map.of("member", memberInfo);
	}
	
	public Integer selectCardCountByMemberIdx(Integer memberIdx) {
		Integer cardCnt = adminRepository.selectCardCountByMemberIdx(memberIdx);
		return cardCnt;
	}

	public void updateMemberInfo(Member convertToMember, Integer memberIdx) {
		convertToCheckNull(convertToMember);
		if (convertToMember.getMemberPass() != null) {
			convertToMember.setMemberPass(passwordEncoder.encode(convertToMember.getMemberPass()));
		}
		
		if(nullCheckForMember(convertToMember)) {
			throw new HandlableException(ErrorCode.FAILED_TO_UPDATE_INFO);
		}
		convertToMember.setMemberIdx(memberIdx);
		
		adminRepository.updateMemberInfo(convertToMember);
	}
	
	public boolean nullCheckForMember(Member member) {
		boolean pass = StringUtils.isEmpty(member.getMemberPass());
		boolean nick = StringUtils.isEmpty(member.getMemberNick());
		boolean tell = StringUtils.isEmpty(member.getMemberTell());
		boolean address = StringUtils.isEmpty(member.getMemberAddress());
		if(pass&&nick&&tell&&address) return true;
		return false;
	}
	
	public Member convertToCheckNull(Member member) {
		if(member.getMemberAddress().equals("[] ")) member.setMemberAddress(null);
		//StringUtils.isEmpty(member.getMemberAddress());
		return member;
	}

	public String checkNickName(String nickName) {
		if (adminRepository.selectMemberByNickName(nickName) == null) {
			return nickName;
		}
		return null;
	}

	public void deleteMember(Integer memberIdx) {
		adminRepository.deleteMember(memberIdx);
	}

	public void deleteMemberProfileImg(Integer flIdx) {
		adminRepository.deleteMemberProfileImg(flIdx);
	}

	public List<Card> selectCardListByMemberIdx(int memberIdx) {
		List<Card> cardList = adminRepository.selectCardListByMemberIdx(memberIdx);
		return cardList;
	}

	public List<FileDTO> selectCardImgList() {
		List<FileDTO> cardImg = adminRepository.selectCardImgList();
		return cardImg;
	}

	public Integer selectCardIdxByflIdx(Integer flIdx) {
		return adminRepository.selectCardIdxByflIdx(flIdx);
	}

	public List<Map<String, Object>> selectRefundHistoryList(String statusCode, String searchType, String searchKeyword, int page) {
		List<Map<String, Object>> refundList = new ArrayList<>();
		Integer cntPerPage = 10;
		List<PointRefund> pointRefundList = adminRepository.selectRefundHistoryList(statusCode,1+(page-1)*cntPerPage,page*cntPerPage);
		
		Integer confirmCheck = adminRepository.selectConfirmCheck();
		System.out.println(confirmCheck);
		if(confirmCheck != 0) {
			adminRepository.updateConfirmCheck();
			System.out.println(confirmCheck);
		}
		
		if(pointRefundList != null) {
			for (PointRefund pointRefund : pointRefundList) {
				Member member = adminRepository.selectMemberByIdxWithDetail(pointRefund.getMemberIdx(),searchType,searchKeyword);
				if(member != null) {
					refundList.add(Map.of("point",pointRefund,"member",member));
				}
			}
		}
		
		return refundList;
	}

	public void updateStatusCode(MemberAccount member, String statusCode, Integer prIdx, Integer refundPoint) {
		Member adminInfo = adminRepository.selectMemberByEmail(member.getMemberEmail());
		String adminName = adminInfo.getMemberName();
		String selectStatus = adminRepository.selectStatusCodeByPrIdx(prIdx);
		Integer memberIdx = adminRepository.selectMemberIdxByPrIdxFromPointRefund(prIdx);
		Member clientInfo = adminRepository.selectMemberByIdx(memberIdx);
		System.out.println(selectStatus);
		//PointHistory pointHistory = new PointHistory();
		//pointHistory.setUserIdx(clientInfo.getMemberIdx());
		//pointHistory.setType("충전");
		//pointHistory.setPoints(refundPoint);
		//pointHistory.setContent(statusCode);
		if(selectStatus.equals("취소")&&selectStatus.equals("완료")) {
			throw new HandlableException(ErrorCode.FAILED_TO_REFUND_STATUSCODE_ALREADY_COMPLATE);
		}else {
			if(statusCode.equals("심사중")&&statusCode.equals("보류중")) {
				adminRepository.updateStatusCode(statusCode,adminName,prIdx);
				//adminRepository.insertHistory(pointHistory);
			}else if(statusCode.equals("완료")){
				//완료시에 포인트 차감
				adminRepository.updateStatusCode(statusCode,adminName,prIdx);
				SavePoint savePoint = adminRepository.selectGetPoint(member.getMemberIdx());
				adminRepository.updatePointByComplate(member.getMemberIdx(),savePoint.getBalance(),refundPoint);
				//adminRepository.insertHistory(pointHistory);
			}else {
				//취소시에 포인트 복구
				adminRepository.updateStatusCode(statusCode,adminName,prIdx);
				SavePoint savePoint = adminRepository.selectGetPoint(member.getMemberIdx());
				adminRepository.updatePointByCencel(member.getMemberIdx(),savePoint.getAvailableBal(),refundPoint);
				//adminRepository.insertHistory(pointHistory);
			}
		}
	}

	public Paging selectRefundByPaging(String statusCode, String searchType, String searchKeyword, int page) {
		
		Integer cntPerPage = 10;
		List<Object> obj = new ArrayList<Object>();
		List<PointRefund> pointRefundList = adminRepository.selectRefundHistoryListForCount(statusCode);
		Integer totalCnt = pointRefundList.size();
			if(pointRefundList != null) {
				if(searchKeyword != null) {
					for (PointRefund pointRefund : pointRefundList) {
						Member member = adminRepository.selectMemberByIdxWithDetail(pointRefund.getMemberIdx(),searchType,searchKeyword);
						obj.add(member);
						totalCnt = obj.size();
					}
				}
			}
		
		Paging pageUtil = Paging.builder()
				.url("/admin/refunds-history")
				.total(totalCnt)
				.cntPerPage(cntPerPage)
				.blockCnt(5)
				.curPage(page)
				.build();
		
		return pageUtil;
	}

	public List<PointHistory> selectPointHistoriesByMemberIdxFromAll(Integer memberIdx) {
		return adminRepository.selectPointHistoriesByMemberIdxFromAll(memberIdx);
	}

	public List<PointHistory> selectPointHistoriesByMemberIdxFromUse(Integer memberIdx) {
		return adminRepository.selectPointHistoriesByMemberIdxFromUse(memberIdx);
	}

	public List<PointHistory> selectPointHistoriesByMemberIdxFromAllSave(Integer memberIdx) {
		return adminRepository.selectPointHistoriesByMemberIdxFromAllSave(memberIdx);
	}

	public Integer selectPointByMemberIdx(Integer memberIdx) {
		return adminRepository.selectPointByMemberIdx(memberIdx);
	}

	public Integer selectRefundNewCount() {
		return adminRepository.selectRefundNewCount();
	}

	public String selectCheckAdmin(Integer memberIdx) {
		return adminRepository.selectCheckAdmin(memberIdx);
	}

	

	


	

	

	

	

	

}
