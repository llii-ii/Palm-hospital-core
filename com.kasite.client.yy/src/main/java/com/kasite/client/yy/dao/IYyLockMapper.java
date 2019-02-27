package com.kasite.client.yy.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.kasite.client.yy.bean.dbo.YyLock;

import tk.mybatis.mapper.common.Mapper;

/**
 * 
 * @className: IYyLockMapper
 * @author: lcz
 * @date: 2018年7月21日 下午2:02:00
 */
public interface IYyLockMapper extends Mapper<YyLock>{
	
	@Select({"<script>",
		"SELECT a.* FROM YY_LOCK a LEFT JOIN YY_UNLOCK b ON a.ORDERID = b.ORDERID ",
		"<where>",
			"<if test=\"lock!=null and lock.orderId!=null and lock.orderId!=''\">",
				" AND a.ORDERID=#{lock.orderId}",
			"</if>",
			"<if test=\"state==1\">",
				" <![CDATA[ AND b.ORDERID IS NULL ]]> ",
			"</if>",
			"<if test=\"lock!=null and lock.cardNo!=null and lock.cardNo!=''\">",
				" AND a.cardNo=#{lock.cardNo}",
			"</if>",
			"<if test=\"lock!=null and lock.memberId!=null and lock.memberId!=''\">",
			" AND a.memberId=#{lock.memberId}",
			"</if>",
			"<if test=\"lock!=null and lock.cardTypeCode!=null and lock.cardTypeCode!=''\">",
				" AND a.CARDTYPECODE=#{lock.cardTypeCode}",
			"</if>",
		"</where>",
		"</script>"
	})
	/**
	 * 查询未解锁的锁号记录，不验证有效时间
	 * @Description: 
	 * @param lock
	 * @param state
	 * @return
	 */
	YyLock getYyLock(@Param("lock")YyLock lock,@Param("state")int state);
	
	@Select({"<script>",
		"SELECT a.* FROM YY_LOCK a LEFT JOIN YY_UNLOCK b ON a.ORDERID = b.ORDERID ",
		"<where>",
			"<if test=\"lock!=null and lock.orderId!=null and lock.orderId!=''\">",
				" AND a.ORDERID=#{lock.orderId}",
			"</if>",
			"<if test=\"state==1\">",
				" <![CDATA[ AND b.ORDERID IS NULL AND NOW() < a.INVALIDDATE ]]> ",
			"</if>",
			"<if test=\"state==2\">",
				" <![CDATA[ AND NOW() >= a.INVALIDDATE AND a.INVALIDDATE >= SUBDATE(NOW(),7) AND b.ORDERID IS NULL ]]>",
			"</if>",
			"<if test=\"lock!=null and lock.cardNo!=null and lock.cardNo!=''\">",
				" AND a.cardNo=#{lock.cardNo}",
			"</if>",
			"<if test=\"lock!=null and lock.cardTypeCode!=null and lock.cardTypeCode!=''\">",
				" AND a.CARDTYPECODE=#{lock.cardTypeCode}",
			"</if>",
		"</where>",
		"</script>"
	})
	/**
	 * 查询过期、且未解锁的锁号记录
	 */
	List<YyLock> queryYyLock(@Param("lock")YyLock lock,@Param("state")int state);
	
}
