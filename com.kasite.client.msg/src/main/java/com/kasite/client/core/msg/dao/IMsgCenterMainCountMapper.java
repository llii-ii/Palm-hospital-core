package com.kasite.client.core.msg.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.core.msg.bean.dbo.MsgQueueRecord;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IMsgSceneMapper
 * @author: zwl
 * @date: 2018年11月13日 下午10:29:51
 */
public interface IMsgCenterMainCountMapper extends Mapper<MsgQueueRecord>{
	@Select("(SELECT channelId AS channelId,"
			+ "COUNT(*)AS countNum FROM M_MSGQUEUE_RECORD "
			+ "WHERE state=1  and sendTime>=str_to_date(#{startTime},'%Y-%m-%d') and sendTime<=str_to_date(#{endTime},'%Y-%m-%d') and hosId=#{hosId} "
			+ "GROUP BY channelId )")
	List<Map<String, Object>> querySendCountByChn(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("hosId")String hosId);
	@Select("(SELECT modeType,scenename,"
			+ "COUNT(*)AS countNum FROM M_MSGQUEUE_RECORD "
			+ "WHERE state=1 and sendTime>=str_to_date(#{startTime},'%Y-%m-%d') and sendTime<=str_to_date(#{endTime},'%Y-%m-%d') and hosId=#{hosId} "
			+ "GROUP BY modeType,sceneName ORDER BY COUNT(modeType) DESC ) ")
	List<Map<String, Object>> querySendCountByModeType(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("hosId")String hosId);

	@Select("(SELECT operatorId AS operatorId,"
		+ "COUNT(*)AS countNum FROM M_MSGQUEUE_RECORD "
		+ "WHERE state=1 and sendTime>=str_to_date(#{startTime},'%Y-%m-%d') and sendTime<=str_to_date(#{endTime},'%Y-%m-%d') and hosId=#{hosId} "
		+ "GROUP BY operatorId ORDER BY COUNT(operatorId) DESC limit 0,10) ")
	List<Map<String, Object>> querySendCountByOperator(@Param("startTime")String startTime,@Param("endTime")String endTime,@Param("hosId")String hosId);

}
