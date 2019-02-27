package com.kasite.core.serviceinterface.module.basic.resp;

import java.util.List;

import com.kasite.core.common.resp.AbsResp;

/**
 * @author zhaoy 2018年08月23日 17:26:52 
 * TODO 查询科室列表响应对象(管理后台的科室树状列表)
 */
public class RespQueryBaseDeptTreeInfo extends AbsResp{
	
	/**科室代码**/
	private String id;
	
	/**科室名称**/
	private String text;
	
	/**子类**/
	private List<RespQueryBaseDeptTreeInfo> children;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<RespQueryBaseDeptTreeInfo> getChildren() {
		return children;
	}

	public void setChildren(List<RespQueryBaseDeptTreeInfo> children) {
		this.children = children;
	}

}
