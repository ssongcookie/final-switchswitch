package com.kh.switchswitch.chat.model.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.switchswitch.chat.model.dto.ChatMessages;
import com.kh.switchswitch.chat.model.dto.Chatting;

@Mapper
public interface ChatRepository {
	@Select("select * from chat_messages where chatting_idx=#{chattingIdx}  order by cm_idx asc")
	List<ChatMessages> selectChatMessagesList(Integer receiverIdx);

	@Select("select * from chat_messages where cm_idx=#{cmIdx}")
	ChatMessages selectChatMessages(Integer cmIdx);
	
	@Insert("insert into chat_messages values(sc_chat_idx.nextval,#{chattingIdx},#{senderId},#{message},sysdate,1)")
	void insertChatMessage(ChatMessages chatMessages);

	@Update("update chat_messages set is_read=0 where cm_idx=#{cmIdx}")
	void updateChatIsRead(Integer cmIdx);
	
	@Update("update chat_messages set is_read=0 where chatting_idx=#{chattingIdx}")
	void updateAllChatIsRead(Integer chattingIdx);

	@Insert("insert into chatting values(sc_chatting_idx.nextval,#{attendee1},#{attendee2},sysdate)")
	void insertChatting(Chatting chatting);
	
	@Insert("insert into chatting_history values(#{chattingIdx},#{attendee1},#{attendee2},#{createAt})")
	void insertChattingHistory(Chatting chatting);

	@Select("select * from chatting where ATTENDEE1 =#{memberIdx} or ATTENDEE2 = #{memberIdx}")
	List<Chatting> selectAllChattingByMemberIdx(Integer memberIdx);

	@Select("select message from chat_messages  where chatting_idx = #{chattingIdx} and ROWNUM <=1 ORDER BY cm_idx desc")
	String selectLastChatMessages(Integer chattingIdx);

	@Select("SELECT COUNT(*) from chat_messages where chatting_idx=#{chattingIdx} and is_read=1 and sender_id !=#{senderId}")
	int selectCountOfIsReadByChattingIdx(@Param("chattingIdx") Integer chattingIdx,@Param("senderId")Integer senderId);
	
	@Select("SELECT TO_CHAR(CREATED_AT,'hh:mm') FROM chat_messages where cm_idx=#{cmIdx}")
	String selectSendTimeByCmId(Integer cmIdx);

	@Select("select * from chatting where chatting_idx=#{chattingIdx}")
	Chatting selectChattingByChattingIdx(Integer chattingIdx);
	
	@Select("select * from chatting where (attendee1 =#{requestedMemIdx} or attendee2=#{requestedMemIdx}) and (attendee1 =#{requestMemIdx} or attendee2=#{requestMemIdx})")
	Chatting selectChattingByAttendeeMemIdxs(@Param("requestedMemIdx")Integer requestedMemIdx,@Param("requestMemIdx") Integer requestMemIdx);

	@Update("update chatting set attendee1 = null where chatting_idx = #{chattingIdx}")
	void updateChattingAttendee1(Integer chattingIdx);
	
	@Update("update chatting set attendee2 = null where chatting_idx = #{chattingIdx}")
	void updateChattingAttendee2(Integer chattingIdx);


}
