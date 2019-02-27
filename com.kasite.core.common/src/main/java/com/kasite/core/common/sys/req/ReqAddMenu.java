package com.kasite.core.common.sys.req;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.constant.RetCode;
import com.kasite.core.common.exception.RRException;
import com.kasite.core.common.req.AbsReq;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 
 * @className: ReqAddMenu
 * @author: lcz
 * @date: 2018年8月30日 下午9:21:33
 */
public class ReqAddMenu extends AbsReq {


	/**
	 * 父菜单ID，一级菜单为0
	 */
	private Long parentId;

	/**
	 * 菜单名称
	 */
	private String name;

	/**
	 * 菜单URL
	 */
	private String url;

	/**
	 * 授权(多个用逗号分隔，如：a.b.c,a.b.hello)
	 */
	private String perms;

	/**
	 * 类型 0：目录 1：菜单 2：按钮
	 */
	private Integer type;

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 排序
	 */
	private Integer orderNum;


	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getPerms() {
		return perms;
	}

	public void setPerms(String perms) {
		this.perms = perms;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * @Title: ReqAddMenu
	 * @Description:
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqAddMenu(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		if(msg.getParamType()==0) {
			this.parentId = getDataJs().getLongValue("ParentId");//默认0
			this.name = getDataJs().getString("Name");
			if (StringUtil.isBlank(this.name)) {
				throw new RRException(RetCode.Common.ERROR_PARAM, "参数菜单名称不能为空。");
			}
			this.url = getDataJs().getString("Url");
			this.perms = getDataJs().getString("Perms");
			this.type = getDataJs().getInteger("Type");
			if (this.type == null || this.type < 0) {
				throw new RRException(RetCode.Common.ERROR_PARAM, "参数菜单类型不能为空。");
			}
			this.icon = getDataJs().getString("Icon");
			this.orderNum = getDataJs().getInteger("OrderNum");
		}
	}

}
