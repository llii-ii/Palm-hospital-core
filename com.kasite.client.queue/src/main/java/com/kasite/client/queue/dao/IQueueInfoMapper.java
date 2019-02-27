package com.kasite.client.queue.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.kasite.client.queue.bean.dbo.QueueInfo;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IQueueInfoMapper
 * @author: lcz
 * @date: 2018年7月25日 下午2:15:44
 */
public interface IQueueInfoMapper extends Mapper<QueueInfo>{

	@Update("UPDATE Q_QUEUEINFO SET IFMSG=#{ifMsg}, REMINDNO=#{reMindNo},CHANNELID = #{channelId} where QUERYID = #{queryId} ")
	int removeReMindNo(@Param("ifMsg")String ifMsg,@Param("reMindNo")String reMindNo,@Param("channelId")String channelId,@Param("queryId")String queryId);
	
}
