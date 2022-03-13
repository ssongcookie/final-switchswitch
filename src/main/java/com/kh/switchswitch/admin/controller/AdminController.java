package com.kh.switchswitch.admin.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.switchswitch.admin.model.dto.Menu;
import com.kh.switchswitch.admin.model.service.AdminService;
import com.kh.switchswitch.admin.validator.MemberUpdate;
import com.kh.switchswitch.admin.validator.MemberUpdateValidator;
import com.kh.switchswitch.card.model.dto.Card;
import com.kh.switchswitch.common.code.ErrorCode;
import com.kh.switchswitch.common.exception.HandlableException;
import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.common.util.pagination.Paging;
import com.kh.switchswitch.common.validator.ValidatorResult;
import com.kh.switchswitch.member.model.dto.Member;
import com.kh.switchswitch.member.model.dto.MemberAccount;
import com.kh.switchswitch.point.model.dto.PointHistory;

@Controller
@RequestMapping("admin")
public class AdminController {

	private AdminService adminService;
	private MemberUpdateValidator memberUpdateValidator;

	public AdminController(AdminService adminService, MemberUpdateValidator memberUpdateValidator) {
		super();
		this.adminService = adminService;
		this.memberUpdateValidator = memberUpdateValidator;
	}

	@InitBinder(value = "memberUpdate")
	public void initBinder(WebDataBinder webDataBinder) {
		webDataBinder.addValidators(memberUpdateValidator);
	}
	
	
	
	
	@GetMapping("real-time-cards")
	public void realTimeCards(Model model, @RequestParam(required = false) String type) {
		if (type != null) {
			switch (type) {
			case "allCard":
				allCardList(model);
				break;
			case "tradeCard":
				tradeCardList(model);
				break;
			case "freeCard":
				freeCardList(model);
				break;
			}
		} else {
			allCardList(model);
		}
	}

	@GetMapping("real-time-card-img")
	public void realTimeCardImg(Model model) {
		List<FileDTO> cardImgList = adminService.selectCardImgList();
		model.addAttribute("cardImg", cardImgList);
	}

	@GetMapping("all-cards")
	public void allCards(Model model, @Nullable @RequestParam(name = "searchPeriod") String searchPeriod,
			@Nullable @RequestParam(name = "searchType") String searchType,
			@Nullable @RequestParam(name = "searchKeyword") String searchKeyword,
			@RequestParam(required = false, defaultValue = "1") int page) {
		
		List<Map<String, Object>> cardList = adminService.selectCardListDetail(searchPeriod, searchType, searchKeyword, page);
		Paging pageUtil = adminService.selectCardPaging(searchPeriod, searchType, searchKeyword,page);
		model.addAttribute("cardList", cardList);
		model.addAttribute("paging",pageUtil);
	}

	@GetMapping("card-delete")
	@ResponseBody
	public String cardDelete(@RequestParam Integer cardIdx) {
		if (cardIdx != null) {
			adminService.deleteCard(cardIdx);
			return "success";
		} else {
			throw new HandlableException(ErrorCode.FAILED_TO_DELETE_CARD);
		}
	}
	
	@GetMapping("card-delete-by-Img")
	@ResponseBody
	public String cardDeleteByImg(@RequestParam Integer flIdx) {
		if(flIdx != null) {
			Integer cardIdx = adminService.selectCardIdxByflIdx(flIdx);
			adminService.deleteCard(cardIdx);
		return "success";
		}else {
			throw new HandlableException(ErrorCode.FAILED_TO_DELETE_IMG);
		}
	}

	@GetMapping("all-members")
	public void allMembers(Model model, @Nullable @RequestParam(name = "searchDetail") String searchType,
			@Nullable @RequestParam(name = "searchKeyword") String keyword, @RequestParam(required = false, defaultValue = "1") int page) {
		model.addAllAttributes(adminService.selectMemberAllListByPage(searchType, keyword, page));
	}

	@GetMapping("changeMemberStatus")
	public String changeMemberStatus(@RequestParam String code, @RequestParam int memberIdx) {
		adminService.updateMemberCode(code, memberIdx);
		return "redirect:/admin/all-members";
	}

	@GetMapping("black-list-members")
	public void blackListMembers(Model model, @Nullable @RequestParam(name = "searchDetail") String searchType,
			@Nullable @RequestParam(name = "searchKeyword") String keyword, @RequestParam(required = false, defaultValue = "1") int page) {
		model.addAllAttributes(adminService.selectMemberBlackListByPage(searchType, keyword, page));
	}

	@GetMapping("removeMemberBlack")
	public String removeMemberBlack(@RequestParam String code, @RequestParam int memberIdx) {
		adminService.updateMemberCode(code, memberIdx);
		return "redirect:/admin/black-list-members";
	}

	@GetMapping("memberLeave")
	public String memberLeave(@RequestParam Integer memberIdx) {
		adminService.deleteMember(memberIdx);
		return "redirect:/admin/all-members";
	}

	@GetMapping("refunds-history")
	public void refundsHistory(Model model, @Nullable @RequestParam String statusCode,
			@Nullable @RequestParam String searchType, @Nullable @RequestParam String searchKeyword, @RequestParam(required = false, defaultValue = "1") int page) {
		List<Map<String, Object>> pointRefund = adminService.selectRefundHistoryList(statusCode, searchType, searchKeyword,page);
		Paging pageUtil = adminService.selectRefundByPaging(statusCode, searchType, searchKeyword,page);
		model.addAttribute("pointRefund",pointRefund);
		model.addAttribute("paging",pageUtil);
	}
	
	@GetMapping("change-refund-status")
	public String changeRefundStatus(@AuthenticationPrincipal MemberAccount member,@RequestParam String statusCode, @RequestParam Integer prIdx,@RequestParam Integer refundPoint ) {
			adminService.updateStatusCode(member,statusCode, prIdx,refundPoint);
			return "redirect:/admin/refunds-history";
	}

	@GetMapping("member-profile/{memberIdx}")
	public String memberProfile(@PathVariable(required = false, name = "memberIdx") Integer memberIdx, Model model) {
		
		String adminCheck = adminService.selectCheckAdmin(memberIdx);
		if(adminCheck.equals("C")) {
			throw new HandlableException(ErrorCode.FAILED_TO_ACCESS_ADMIN_PROFILE);
		}
			// 회원정보
			Map<String, Object> memberInfo = adminService.selectMemberByIdx(memberIdx);
			// 회원이 등록한 카드 수량
			Integer cardCountFromMember = adminService.selectCardCountByMemberIdx(memberIdx);
	
			// 회원이 등록한 카드
			List<Map<String, Object>> cardList = new ArrayList<>();
			List<Card> memberCardList = adminService.selectCardListByMemberIdx(memberIdx);
			if (memberCardList != null) {
				for (Card card : memberCardList) {
					FileDTO mainImgFile = adminService.selectMainImgFileByCardIdx(card.getCardIdx());
					List<FileDTO> imgFile = adminService.selectImgFileListByCardIdx(card.getCardIdx());
					cardList.add(Map.of("card", card, "fileDTO", mainImgFile, "fileDTOAll", imgFile));
				}
				model.addAttribute("cardList", cardList);
			}
			Integer refundCount = adminService.selectRefundNewCount();
			model.addAttribute("refundCount",refundCount);
			//회원 포인트 내역
			List<PointHistory> pointHistories = adminService.selectPointHistoriesByMemberIdxFromAll(memberIdx);
			List<PointHistory> pointHistoriesUse = adminService.selectPointHistoriesByMemberIdxFromUse(memberIdx);
			List<PointHistory> pointHistoriesSave = adminService.selectPointHistoriesByMemberIdxFromAllSave(memberIdx);
			Integer userPoint = adminService.selectPointByMemberIdx(memberIdx);
			if(userPoint == null) userPoint = 0;
			
			model.addAttribute("point", pointHistories);
			model.addAttribute("usePoint", pointHistoriesUse);
			model.addAttribute("savePoint", pointHistoriesSave);
			model.addAttribute("memberCardList", memberCardList);
			
			model.addAttribute("userPoint", userPoint + "<span style=\"color: black\">포인트</span>");
			model.addAttribute("memberInfo", memberInfo);
			model.addAttribute("cardCnt", cardCountFromMember + "<span style=\"color: black\">카드</span>");
			return "admin/member-profile";
	}

	@GetMapping("member-edit/{memberIdx}")
	public String memberProfileEdit(@PathVariable(required = false, name = "memberIdx") Integer memberIdx, Model model) {
		Map<String, Object> memberInfo = adminService.selectMemberByIdx(memberIdx);
		
		Integer refundCount = adminService.selectRefundNewCount();
		model.addAttribute("refundCount",refundCount);
		
		model.addAttribute("memberDetail", memberInfo);
		model.addAttribute(new MemberUpdate()).addAttribute("error", new ValidatorResult().getError());
		return "admin/member-edit";
	}

	@PostMapping("member-edit/{memberIdx}")
	public String memberProfileEditSuccess(@Validated@ModelAttribute("memberUpdate") MemberUpdate form, Errors errors, 
			Integer memberIdx,@AuthenticationPrincipal Member member,
			Model model) {
		Map<String, Object> memberInfo = adminService.selectMemberByIdx(memberIdx);
		System.out.println(memberIdx);
		model.addAttribute("memberDetail",memberInfo);
		ValidatorResult vr = new ValidatorResult();
		model.addAttribute("error", vr.getError());
		if (errors.hasErrors()) {
			vr.addErrors(errors);
			return "admin/member-edit";
		}
		adminService.updateMemberInfo(form.convertToMember(), memberIdx);
		return "redirect:/admin/member-profile/{memberIdx}";
	}

	@GetMapping("nick-check")
	@ResponseBody
	public String nickCheck(String nickName) {
		if (nickName.equals(adminService.checkNickName(nickName))) {
			return "available";
		} else {
			return "disable";
		}
	}

	@GetMapping("profile-img-delete")
	@ResponseBody
	public String profileImgDelete(Integer flIdx) {
		System.out.println("비동기통신 성공");
		if (flIdx != null) {
			adminService.deleteMemberProfileImg(flIdx);
			return "success";
		} else {
			return "fail";
		}
	}

	@GetMapping("page-setting")
	public void pageSetting(Model model) {
		// 주메뉴
		Map<String, List<Menu>> childMenuList = new LinkedHashMap<String, List<Menu>>();
		List<Menu> parentMenu = adminService.selectMenuList();
		model.addAttribute("menuList", adminService.selectMenuList());
		for (Menu menu : parentMenu) {
			String parent = menu.getUrlName();
			childMenuList.put(parent, adminService.selectChildMenu(parent));
		}
		model.addAttribute("childMenuList", childMenuList);

		// 사이드메뉴
		Map<String, List<Menu>> childSideMenuList = new LinkedHashMap<String, List<Menu>>();
		List<Menu> sideParentMenu = adminService.selectSideMenu();
		model.addAttribute("sideMenuList", sideParentMenu);
		for (Menu menu : sideParentMenu) {
			String parent = menu.getUrlName();
			childSideMenuList.put(parent, adminService.selectChildMenu(parent));
		}
		model.addAttribute("childSideMenuList", childSideMenuList);

		// 목록 및 페이지 추가 팝업 view
		model.addAttribute("menuAllList", adminService.selectMenuAllList());
		model.addAttribute("parentsMenuList", adminService.selectParentsMenuListByMenu());
		model.addAttribute("parentsSideMenuList", adminService.selectParentsMenuListBySideMenu());
		model.addAttribute("codeList", adminService.selectCodeList());
		
		Integer refundCount = adminService.selectRefundNewCount();
		model.addAttribute("refundCount",refundCount);
	}
	
	//@Async
	@PostMapping("add-menu")
	public String addPage(Menu menu) {
		try {
			if (menu.getParent().equals("없음")) {
				menu.setDepth(1);
				adminService.insertMenu(menu);
			} else {
				menu.setDepth(2);
				adminService.insertMenu(menu);
			}
		} catch (Exception e) {
			throw new HandlableException(ErrorCode.DATABASE_ACCESS_ERROR);
		}
		System.out.println("배치진입");
		return "redirect:/admin/page-setting";
	}

	@GetMapping("delete-menu")
	public String deleteMenu(int urlIdx) {
		adminService.deleteMenu(urlIdx);
		return "redirect:/admin/page-setting";
	}
	
	private void freeCardList(Model model) {
		List<Map<String, Object>> cardList = new ArrayList<>();
		List<Card> memberCardList = adminService.selectCardListByFree();
		if (memberCardList != null) {
			for (Card card : memberCardList) {
				FileDTO mainImgFile = adminService.selectMainImgFileByCardIdx(card.getCardIdx());
				List<FileDTO> imgFile = adminService.selectImgFileListByCardIdx(card.getCardIdx());
				cardList.add(Map.of("card", card, "fileDTO", mainImgFile, "fileDTOAll", imgFile));
			}
		}
		model.addAttribute("cardList", cardList);
	}

	private void tradeCardList(Model model) {
		List<Map<String, Object>> cardList = new ArrayList<>();
		List<Card> memberCardList = adminService.selectCardListByTrade();
		if (memberCardList != null) {
			for (Card card : memberCardList) {
				FileDTO mainImgFile = adminService.selectMainImgFileByCardIdx(card.getCardIdx());
				List<FileDTO> imgFile = adminService.selectImgFileListByCardIdx(card.getCardIdx());
				cardList.add(Map.of("card", card, "fileDTO", mainImgFile, "fileDTOAll", imgFile));
			}
		}
		model.addAttribute("cardList", cardList);
	}

	private void allCardList(Model model) {
		List<Map<String, Object>> cardList = new ArrayList<>();
		List<Card> memberCardList = adminService.selectCardList();
		if (memberCardList != null) {
			for (Card card : memberCardList) {
				FileDTO mainImgFile = adminService.selectMainImgFileByCardIdx(card.getCardIdx());
				List<FileDTO> imgFile = adminService.selectImgFileListByCardIdx(card.getCardIdx());
				cardList.add(Map.of("card", card, "fileDTO", mainImgFile, "fileDTOAll", imgFile));
			}
		}
		model.addAttribute("cardList", cardList);
	}
}
