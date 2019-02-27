package com.kasite.client.basic.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.coreframework.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.kasite.client.basic.dao.IDictionaryMapper;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.resp.RespMap;
import com.kasite.core.common.util.BeanCopyUtils;
import com.kasite.core.serviceinterface.module.basic.IDictionaryService;
import com.kasite.core.serviceinterface.module.basic.dbo.Dictionary;
import com.kasite.core.serviceinterface.module.basic.req.ReqAddDictionary;
import com.kasite.core.serviceinterface.module.basic.req.ReqDelDictionary;
import com.kasite.core.serviceinterface.module.basic.req.ReqQueryDictList;
import com.kasite.core.serviceinterface.module.basic.req.ReqUpdateDictionary;
import com.kasite.core.serviceinterface.module.basic.resp.RespQueryDictList;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

/**
 * 
 * @className: DictionaryServiceImpl
 * @author: lcz
 * @date: 2018年8月27日 下午3:38:13
 */
@Service
public class DictionaryServiceImpl implements IDictionaryService{

	@Autowired
	private IDictionaryMapper dictionaryMapper;
	@Override
	public List<Dictionary> queryDictList(PageVo page, Dictionary dict) throws Exception {
		if(page!=null && page.getPIndex()!=null && page.getPSize()>0) {
			PageHelper.startPage(page.getPIndex(), page.getPSize());
		}
		List<Dictionary> list = dictionaryMapper.select(dict);
		page.initPCount(list);
		return list;
	}
	@Override
	public CommonResp<RespQueryDictList> queryDictList(CommonReq<ReqQueryDictList> req) throws Exception {
		ReqQueryDictList dictReq = req.getParam();
		Example example = new Example(Dictionary.class);
		Criteria criteria = example.createCriteria();
		if(dictReq.getId()!=null && dictReq.getId()>0) {
			criteria.andEqualTo("id", dictReq.getId());
		}
		if(StringUtil.isNotBlank(dictReq.getValue())) {
			criteria.andEqualTo("value", dictReq.getValue());
		}
		if(StringUtil.isNotBlank(dictReq.getKeyword())) {
			criteria.andEqualTo("keyword", dictReq.getKeyword());
		}
		if(dictReq.getUpId()!=null && dictReq.getUpId()>0) {
			criteria.andEqualTo("upId", dictReq.getUpId());
		}
		if(dictReq.getStatus()!=null && dictReq.getStatus()>=0) {
			criteria.andEqualTo("status", dictReq.getStatus());
		}
		List<Dictionary> list = null;
		if(dictReq.getPage()!=null && dictReq.getPage().getPIndex()!=null && dictReq.getPage().getPSize()>0) {
			PageHelper.startPage(dictReq.getPage().getPIndex()+1, dictReq.getPage().getPSize());
			list = dictionaryMapper.selectByExample(example);
			dictReq.getPage().initPCount(list);
		}else {
			list = dictionaryMapper.selectByExample(example);
		}
		List<RespQueryDictList> respList = new ArrayList<RespQueryDictList>();
		for (Dictionary dict : list) {
			RespQueryDictList resp = new RespQueryDictList();
			BeanCopyUtils.copyProperties(dict, resp, null);
			respList.add(resp);
		}
		return new CommonResp<>(req, KstHosConstant.DEFAULTTRAN, RetCode.Success.RET_10000, respList,dictReq.getPage());
	}
	
	@Override
	public CommonResp<RespMap> delDictionary(CommonReq<ReqDelDictionary> req) throws Exception {
		ReqDelDictionary delReq = req.getParam();
		if(delReq==null || delReq.getId()==null || delReq.getId()<=0) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "参数Id不能为空。");
		}
		dictionaryMapper.deleteByPrimaryKey(delReq.getId());
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000, "操作成功");
	}
	@Override
	public CommonResp<RespMap> updateDictionary(CommonReq<ReqUpdateDictionary> req) throws Exception {
		ReqUpdateDictionary upReq = req.getParam();
		if(upReq==null || upReq.getId()==null || upReq.getId()<=0) {
			return new CommonResp<RespMap>(req, RetCode.Common.ERROR_PARAM, "参数Id不能为空。");
		}
		Dictionary dict = new Dictionary();
		dict.setId(upReq.getId());
		dict.setUpId(upReq.getUpId());
		dict.setDicType(upReq.getDicType());
		dict.setKeyword(upReq.getKeyword());
		dict.setValue(upReq.getValue());
		dict.setStatus(upReq.getStatus());
		dict.setOperatorId(upReq.getOpenId());
		dict.setOperatorName(upReq.getOperatorName());
		dictionaryMapper.updateByPrimaryKeySelective(dict);
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000, "操作成功");
	}
	@Override
	public CommonResp<RespMap> addDictionary(CommonReq<ReqAddDictionary> req) throws Exception {
		ReqAddDictionary addReq = req.getParam();
		Dictionary dict = new Dictionary();
		dict.setUpId(addReq.getUpId());
		dict.setDicType(addReq.getDicType());
		dict.setKeyword(addReq.getKeyword());
		dict.setValue(addReq.getValue());
		dict.setStatus(1);//默认启用
		dict.setOperatorId(addReq.getOpenId());
		dict.setOperatorName(addReq.getOperatorName());
		dictionaryMapper.insertSelective(dict);
		return new CommonResp<RespMap>(req, RetCode.Success.RET_10000, "操作成功");
	}
	
	
	
}
