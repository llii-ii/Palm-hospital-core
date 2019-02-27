package com.kasite.client.business.module.sys.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coreframework.util.StringUtil;
import com.kasite.client.business.module.sys.dao.SysUserTokenDao;
import com.kasite.core.common.sys.service.SysUserTokenService;
import com.kasite.core.common.sys.service.pojo.SysUserTokenEntity;
import com.kasite.core.common.sys.verification.TokenGenerator;
import com.kasite.core.common.util.DateOper;
import com.kasite.core.common.util.R;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl implements SysUserTokenService {
	@Autowired
	private SysUserTokenDao sysUserTokenDao;
	//2小时后过期
	private final static int EXPIRE = 3600 * 2;

	@Override
	public SysUserTokenEntity queryByUserId(Long userId) throws Exception {
		return sysUserTokenDao.selectByPrimaryKey(userId);
	}

	@Override
	public SysUserTokenEntity queryByToken(String token) throws Exception {
		SysUserTokenEntity entity = new SysUserTokenEntity();
		entity.setToken(token);
		return sysUserTokenDao.selectOne(entity);
	}

	@Override
	public void save(SysUserTokenEntity token) throws Exception {
		sysUserTokenDao.insert(token);
	}
	
	@Override
	public void update(SysUserTokenEntity token) throws Exception {
		sysUserTokenDao.updateByPrimaryKeySelective(token);
	}

	@Override
	public R createToken(long userId,String user_agent,long exptimes,String ptoken) throws Exception {
		//生成一个token
		String token = null == ptoken ? TokenGenerator.generateValue():ptoken;
		if(exptimes <= 0) {
			exptimes = EXPIRE;
		}
		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + exptimes * 1000);
		//判断是否生成过token
		SysUserTokenEntity tokenEntity = queryByUserId(userId);
		String format = "yyyy-MM-dd HH:mm:ss";
		String createTime = DateOper.getNow(format);
    	String invalidTime = DateOper.formatDate(expireTime, format);
		if(tokenEntity == null){
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);
			//保存token
			save(tokenEntity);
		}else{
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);
			//更新token
			update(tokenEntity);
		}
		R r = R.ok().put("code", "10000").put("token", token)
				.put("invalid_datetime", invalidTime)
				.put("invalid_time", exptimes)
				.put("create_time", createTime)
				.put("access_token", token)
				.put("expire", exptimes);

		return r;
	}
	

	
}
