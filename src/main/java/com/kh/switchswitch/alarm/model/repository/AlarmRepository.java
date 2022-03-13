package com.kh.switchswitch.alarm.model.repository;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.kh.switchswitch.alarm.model.dto.Alarm;

import lombok.Delegate;

@Mapper
public interface AlarmRepository {

	@Insert("insert into alarm values(sc_alarm_idx.nextval, #{senderIdx}, #{receiverIdx}, #{alarmType}, 0, #{reqIdx}, sysdate)")
	void insertAlarm(Alarm alarm);

	@Update("update alarm set is_read=#{isRead} where alarm_idx=#{alarmIdx}")
	void updateAlarmIsRead(Alarm alarm);

	@Select("select sc_alarm_idx.currval from dual")
	Integer selectCurrScAlarmIdx();

	@Select("select count(*) from alarm where receiver_idx=#{receiverIdx} and sysdate < send_date + 7")
	Integer selectAlarmCnt(Integer receiverIdx);

	@Select("select * from (select rownum rnum, alarmr.* from (select alarm.* from alarm alarm"
			+ " where receiver_idx=#{receiverIdx} and sysdate < send_date + 7 order by is_read, alarm_idx desc) alarmr"
			+ " ) where rnum between #{startAlarm} and #{lastAlarm}")
	List<Alarm> selectAlarmListWithReceiverIdx(Map<String, Integer> map);

	@Delete("delete from alarm where receiverIdx =#{memberIdx}")
	void deleteAlarmByMemberIdx(Integer memberIdx);

}
