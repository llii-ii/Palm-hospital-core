package com.kasite.core.serviceinterface.common.cache.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.kasite.core.serviceinterface.common.cache.dao.ISysCacheMapper;
import com.kasite.core.serviceinterface.common.cache.dbo.SysCache;
import com.kasite.core.serviceinterface.common.cache.service.CacheKeyEnum;
import com.kasite.core.serviceinterface.common.cache.service.CacheService;
@Service
public class CacheServiceImpl implements CacheService{

	@Autowired
	private ISysCacheMapper mapper;
	
	@Override
	@CachePut(value = "sysCache", key = "#sysCache.sid") 
	public SysCache save(SysCache sysCache) {
		mapper.insert(sysCache);
		return sysCache;
	}
	@Override
	@CacheEvict(value = "sysCache", key = "#sid")
	public void remove(String sid) {
		SysCache record = new SysCache();
		record.setSid(sid);
		mapper.delete(record);
	}
	@Override
	@Cacheable(value = "sysCache", key = "#sid")
	public SysCache getSysCache(String sid) {
		SysCache record = new SysCache();
		record.setSid(sid);
		return mapper.selectOne(record);
	}
	
	
	@Override
	public String getKey(CacheKeyEnum keyEnum,String sid) {
		return null;
	}

	
//	  @CachePut(value = "people", key = "#person.id")    public Person save(Person person) {        Person p = personRepository.save(person);        System.out.println("为id、key为:" + p.getId() + "数据做了缓存");        return p;    }     @Override    @CacheEvict(value = "people")//2    public void remove(Long id) {        System.out.println("删除了id、key为" + id + "的数据缓存");        //这里不做实际删除操作    }     @Override    @Cacheable(value = "people", key = "#person.id")//3    public Person findOne(Person person) {        Person p = personRepository.findOne(person.getId());        System.out.println("为id、key为:" + p.getId() + "数据做了缓存");        return p;    }
}
