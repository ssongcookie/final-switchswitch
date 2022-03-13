/*package com.kh.switchswitch.common.schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.kh.switchswitch.card.model.dto.Card;
import com.kh.switchswitch.card.model.repository.CardRepository;
import com.kh.switchswitch.member.model.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@EnableAsync
public class Schedule {
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private List<Map<String, Object>> cardsTop5 = new ArrayList<>();
	
	private final CardRepository cardRepository;
	private final MemberRepository memberRepository;
	
	@Scheduled(cron = "0 0/10 * * * *")
	public void setCardsTop5() {
		logger.debug("되는거 맞아?????");
		this.cardsTop5.clear();
		
		List<Card> cards = cardRepository.selectCardsTop5();
		if(cards != null) {
			for (Card card : cards) {
				this.cardsTop5.add(
						Map.of("card", card, 
								"fileDTO", cardRepository.selectFileInfoByCardIdx(card.getCardIdx()).get(0), 
								"cardOwnerRate", memberRepository.selectMemberScoreByMemberIdx(card.getMemberIdx()))) ;
			}
		}
	}
	
	public static void main(String[] args) {
        new AnnotationConfigApplicationContext(Schedule.class);
    }

	public List<Map<String, Object>> getCardsTop5() {
		return cardsTop5;
	}

}
*/
