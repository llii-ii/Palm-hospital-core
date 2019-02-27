package com.kasite.core.common.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.mappers.CommonMapper;
import com.kasite.core.common.req.PageVo;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.R;
import com.yihu.hos.constant.IConstant;


public abstract class AbstractControllerBasic<T> extends AbstractController {
	@Autowired
	protected CommonMapper<T> mapper;
	
	protected abstract Class<T> getClazz();
	/**
	 * 新增
	 * @param reqParam
	 * @param request
	 * @param response
	 * @return
	 */
	public R add(String reqParam,HttpServletRequest request, HttpServletResponse response) {
		JSONObject paramJs = JSONObject.parseObject(reqParam);
		JSONObject reqJs = paramJs.getJSONObject(IConstant.REQ);
		JSONObject _DATAJS = reqJs.getJSONObject(IConstant.DATA);
		T t = JSON.toJavaObject(_DATAJS, getClazz());
		mapper.insertSelective(t);
		return R.ok();
	}
	/**
	 * 根据主键删除
	 * @param reqParam
	 * @param request
	 * @param response
	 * @return
	 */
	public R delete(String reqParam,HttpServletRequest request, HttpServletResponse response) {
		JSONObject paramJs = JSONObject.parseObject(reqParam);
		JSONObject reqJs = paramJs.getJSONObject(IConstant.REQ);
		JSONObject _DATAJS = reqJs.getJSONObject(IConstant.DATA);
		Object o = _DATAJS.get("id");
		mapper.deleteByExample(o);
		return R.ok();
	}
	/**
	 * 修改
	 * @param reqParam
	 * @param request
	 * @param response
	 * @return
	 */
	public R update(String reqParam,HttpServletRequest request, HttpServletResponse response) {
		JSONObject paramJs = JSONObject.parseObject(reqParam);
		JSONObject reqJs = paramJs.getJSONObject(IConstant.REQ);
		JSONObject _DATAJS = reqJs.getJSONObject(IConstant.DATA);
		T t = JSON.toJavaObject(_DATAJS, getClazz());
		mapper.updateByPrimaryKeyIncludeEmpty(t);
		return R.ok();
	}
	/**
	 * 列表查询
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	public R queryList(String reqParam,HttpServletRequest request, HttpServletResponse response) {
		PageVo pageVo = null;
		CommonResp<T> resp = null;
		JSONObject paramJs = JSONObject.parseObject(reqParam);
		JSONObject reqJs = paramJs.getJSONObject(IConstant.REQ);
		JSONObject _DATAJS = reqJs.getJSONObject(IConstant.DATA);
		if(_DATAJS.containsKey(IConstant.PAGE)) {
			JSONObject pageJs = _DATAJS.getJSONObject(IConstant.PAGE);
			if(!pageJs.isEmpty()) {
				pageVo = new PageVo(pageJs.getIntValue(IConstant.PINDEX),pageJs.getIntValue(IConstant.PSIZE));
			}
		}
		String transactionCode = "";
		T t = JSON.toJavaObject(_DATAJS, getClazz());
		//分页查询
		if(null != pageVo) {
			RowBounds row = new RowBounds(pageVo.getPIndex(),pageVo.getPSize());
			int count = mapper.selectCount(t);
			pageVo.setPCount(count);
			List<T> list = mapper.selectByRowBounds(t, row);
			resp = new CommonResp<>(null,
					transactionCode, 
					RetCode.Success.RET_10000,  
					list,
					pageVo);
		}else {
			//未分页查询
			List<T> list = mapper.select(t);
			resp = new CommonResp<>(null,
					transactionCode, 
					RetCode.Success.RET_10000,  
					list);
		}
		return R.ok(resp.toJSONResult());

	}
}
