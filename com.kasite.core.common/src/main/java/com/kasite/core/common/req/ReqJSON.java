package com.kasite.core.common.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.kasite.core.common.constant.RetCode.Success;
import com.kasite.core.common.resp.CommonResp;
import com.kasite.core.common.util.DataUtils;
import com.kasite.core.common.util.StringUtil;
import com.yihu.hos.constant.IConstant;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 */
public class ReqJSON extends AbsReq {
	private JSONObject reqParam;
	private Page<?> page;
	public ReqJSON(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType() == 0) {
			this.reqParam = _DATAJS;
		}else if(msg.getParamType() == 1){
			if(null != __DATA__) {
				this.reqParam = JSONObject.parseObject(DataUtils.xml2JSONObject(msg.getParam()).toString()).getJSONObject("Data");
			}
		}
	}
	
	public <T> T parse(Class<T> clazz) {
		this.page = getPageHelper();
		if(null != clazz && null != reqParam) {
			return JSON.toJavaObject(reqParam, clazz);
		}
		return null;
	}
	public <T> Page<T> getPageHelper(){
		PageVo pageVo = getPage();
		if(null == pageVo || (pageVo.getPIndex() == 0 && pageVo.getPSize() == 0)) {
			//如果分页参数为空。则默认查询分页都是查询 第一页 10 条
			pageVo = new PageVo();
			pageVo.setPIndex(1);
			pageVo.setPSize(10);
		}
		Page<T> page = PageHelper.startPage(pageVo.getPIndex(), pageVo.getPSize());
		if(null != reqParam) {
			String orderBy = reqParam.getString(IConstant.ORDERBY);
			if(StringUtil.isNotBlank(orderBy)) {
				page.setOrderBy(orderBy);
			}
		}
		return page;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> CommonResp<T> getResult(){
		if(null != page) {
			CommonResp resp = new CommonResp(getMsg(), Success.RET_10000,page.getResult());
			resp.setPCount(page.getTotal());
			resp.setPIndex(page.getPageNum());
			resp.setPSize(page.getPageSize());
			return resp;
		}
		return new CommonResp(getMsg(), Success.RET_10000);
	}
	
	/**
	 * 默认根据主键查询
	 * @return
	 */
	public Object getPkId() {
		return reqParam.get("PkId");
	}
	
	public JSONObject getReqParam() {
		return reqParam;
	}
	public void setReqParam(JSONObject reqParam) {
		this.reqParam = reqParam;
	}
}
