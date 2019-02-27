package com.kasite.client.rf.dao;

import java.sql.Timestamp;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.kasite.client.rf.bean.dto.RfCloudData;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: ICloudDataMapper
 * @author: lcz
 * @date: 2018年7月31日 下午3:20:34
 */
public interface ICloudDataMapper extends Mapper<RfCloudData>{
	
	@Update("UPDATE RF_CLOUD_DATA SET DATACOUNT=DATACOUNT+#{dataCount} , UPDATEDATE = #{updateDate} WHERE CHANNELID=#{channelId} and  DATATYPE=#{dataType} and DATE=#{date} ")
	int updateCloundData(@Param("dataCount")int dataCount,@Param("updateDate")Timestamp updateDate,@Param("channelId")String channelId,@Param("dataType")String dataType,@Param("date")String date);
}
