package com.kasite.client.basic.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.kasite.core.serviceinterface.module.basic.dbo.UserMember;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IUserMemberMapper
 * @author: lcz
 * @date: 2018年8月9日 下午8:05:08
 */
public interface IUserMemberMapper extends Mapper<UserMember>{
	
	
	/**
	 * 将用户、成员关联关系移到历史表
	 * @param openId
	 * @param memberId
	 * @throws Exception
	 */
	@Update("INSERT INTO B_USER_MEMBER_OLD SELECT * FROM B_USER_MEMBER WHERE OPENID=#{openId} AND MEMBERID=#{memberId}")
	void moveUserMemberToOld(@Param("openId")String openId,@Param("memberId")String memberId) throws Exception;
	/**
	 * 删除用户、成员关联关系
	 * @param openId
	 * @param memberId
	 * @throws Exception
	 */
	@Update("DELETE FROM B_USER_MEMBER WHERE OPENID=#{openId} AND MEMBERID=#{memberId}")
	void delUserMember(@Param("openId")String openId,@Param("memberId")String memberId) throws Exception;
}
