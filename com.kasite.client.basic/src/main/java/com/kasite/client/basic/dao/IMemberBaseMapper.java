package com.kasite.client.basic.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Update;

import com.kasite.core.serviceinterface.module.basic.dbo.MemberBase;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IMemberBaseMapper
 * @author: lcz
 * @date: 2018年7月18日 下午4:25:42
 */
public interface IMemberBaseMapper extends Mapper<MemberBase>{

//	/**
//	 * 根据OpenId清除所有默认就诊人设置
//	 * @param map
//	 * @return
//	 */
//	@Update("UPDATE B_MEMBER M,B_USER_MEMBER UM SET M.ISDEFAULTMEMBER=#{isDefaultMember} WHERE M.MEMBERID=UM.MEMBERID AND UM.OPENID = #{openId} AND M.ISDEFAULTMEMBER=1")
//	int clearDefaultMember(Map<String,Object> map);
	
}
