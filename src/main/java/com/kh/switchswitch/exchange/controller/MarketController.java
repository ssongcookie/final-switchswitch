package com.kh.switchswitch.exchange.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.kh.switchswitch.card.model.dto.Card;
import com.kh.switchswitch.card.model.dto.CardRequestList;
import com.kh.switchswitch.card.model.dto.SearchCard;
import com.kh.switchswitch.card.model.repository.CardRepository;
import com.kh.switchswitch.card.model.service.CardService;
import com.kh.switchswitch.common.util.FileDTO;
import com.kh.switchswitch.exchange.model.service.ExchangeService;
import com.kh.switchswitch.member.model.dto.MemberAccount;
import com.kh.switchswitch.member.model.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("market")
public class MarketController {
	
	private final CardService cardService;
	private final ExchangeService exchangeService;
	private final MemberService memberService;
	private final CardRepository cardRepository;
	
	@GetMapping("cardmarket")
	public void exchangeCard() {}
	
	@GetMapping("freemarket")
	public void freemarket() {}

//	전체카드조회
	@ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    @GetMapping("getcard")
	public String responseBodyJason(HttpServletResponse response) {
		
		List<Card> allCard = cardService.selectAllCardExceptDone();
		
		for (Card content : allCard) {
			List imgUrl = new ArrayList();
			content.setMemberRate(exchangeService.selectMyRate(content.getMemberIdx()));
			List<FileDTO> cardImgs = cardRepository.selectFileInfoByCardIdx(content.getCardIdx());
		    for (FileDTO img : cardImgs) {
		    	imgUrl.add(img.getDownloadURL());
			}
		    content.setImgUrl(imgUrl);
        }
		
		String json = new Gson().toJson(allCard);
		
		log.info("json={}" ,json);
		return json;
	}
	
	@ResponseStatus(code = HttpStatus.OK)
    @ResponseBody
    @GetMapping("getfreecard")
	public String responseBodyJason2(HttpServletResponse response) {
		
		List<Card> allCard = cardRepository.selectAllFreeCardExceptDone();
		
		for (Card content : allCard) {
			List imgUrl = new ArrayList();
			content.setMemberRate(exchangeService.selectMyRate(content.getMemberIdx()));
			List<FileDTO> cardImgs = cardRepository.selectFileInfoByCardIdx(content.getCardIdx());
		    for (FileDTO img : cardImgs) {
		    	imgUrl.add(img.getDownloadURL());
			}
		    content.setImgUrl(imgUrl);
        }
		
		String json = new Gson().toJson(allCard);
		
		log.info("json={}" ,json);
		return json;
	}

//	카테고리
	@CrossOrigin("*")
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping("category")
    @ResponseBody
    public String searchCardList(@RequestBody SearchCard searchCard
    		, HttpServletResponse response) throws JsonMappingException, JsonProcessingException {
        
		log.info("string={}" ,searchCard);
		
        List<Card> allCard = cardService.selectCardTrim(searchCard);
        
        for (Card card : allCard) {
			List imgUrl = new ArrayList();
			card.setMemberRate(exchangeService.selectMyRate(card.getMemberIdx()));
			List<FileDTO> cardImgs = cardRepository.selectFileInfoByCardIdx(card.getCardIdx());
		    for (FileDTO img : cardImgs) {
		    	imgUrl.add(img.getDownloadURL());
			}
		    card.setImgUrl(imgUrl);
		}
        
        String json = new Gson().toJson(allCard);
        log.info("json={}" ,json);
        
        return json;
    }
	
//	카테고리
	@CrossOrigin("*")
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping("freecategory")
    @ResponseBody
    public String searchCardList2(@RequestBody SearchCard searchCard
    		, HttpServletResponse response) throws JsonMappingException, JsonProcessingException {
        
		log.info("string={}" ,searchCard);
		
        List<Card> allCard = cardRepository.selectFreeCardTrim(searchCard);
        
        for (Card card : allCard) {
			List imgUrl = new ArrayList();
			card.setMemberRate(exchangeService.selectMyRate(card.getMemberIdx()));
			List<FileDTO> cardImgs = cardRepository.selectFileInfoByCardIdx(card.getCardIdx());
		    for (FileDTO img : cardImgs) {
		    	imgUrl.add(img.getDownloadURL());
			}
		    card.setImgUrl(imgUrl);
		}
        
        String json = new Gson().toJson(allCard);
        log.info("json={}" ,json);
        
        return json;
    }
	
	@CrossOrigin("*")
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping("card")
    @ResponseBody
    public String searchCard(@RequestBody Card card,
    		@AuthenticationPrincipal MemberAccount memberAccount
    		, HttpServletResponse response) throws JsonMappingException, JsonProcessingException {
        
		log.info("sting={}" ,card);
		
        Card searchCard = cardService.selectCardWithCardIdx(card.getCardIdx());
        
		if(memberAccount != null) {
			CardRequestList requestCard = cardRepository.selectRequestdCardByMemberIdx(searchCard.getCardIdx(),memberAccount.getMemberIdx());
			log.info("requestCard={}" ,requestCard);
			if(requestCard == null) {
				searchCard.setReqIdx(0);
				searchCard.setRequestedCardIdx(0);
			} else {
				searchCard.setReqIdx(requestCard.getReqIdx());
				searchCard.setRequestedCardIdx(requestCard.getRequestedCard());
			}
		}
        
		List imgUrl = new ArrayList();
        List<FileDTO> cardImgs = cardRepository.selectFileInfoByCardIdx(searchCard.getCardIdx());
	    for (FileDTO img : cardImgs) {
	    	imgUrl.add(img.getDownloadURL());
		}
	    searchCard.setImgUrl(imgUrl);
        
        String memberNick = memberService.selectMemberNickWithMemberIdx(searchCard.getMemberIdx());
        searchCard.setMemberNick(memberNick);
        
        Date regDt = searchCard.getRegDate();
        SimpleDateFormat dt = new SimpleDateFormat("MM.dd");
        String dateFormat = dt.format(regDt);
        searchCard.setDateParse(dateFormat);
        
        String json = new Gson().toJson(searchCard);
        log.info("json={}" ,json);
        
        return json;
    }
	
	
	
}
