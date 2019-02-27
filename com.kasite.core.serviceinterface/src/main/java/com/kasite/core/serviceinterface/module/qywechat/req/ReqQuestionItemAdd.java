package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * REQ通用UID入参
 * 
 * @author 無
 *
 */
public class ReqQuestionItemAdd extends AbsReq {
	public ReqQuestionItemAdd(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.themeid = XMLUtil.getString(dataEl, "themeid", false);
		this.questid = XMLUtil.getInt(dataEl, "questid", true, null);
		this.itemvalue = XMLUtil.getString(dataEl, "itemvalue", false);
		this.sortnum = XMLUtil.getString(dataEl, "sortnum", false);
		this.operatorid = XMLUtil.getString(dataEl, "operatorid", false);
		this.operatorname = XMLUtil.getString(dataEl, "operatorname", false);
	}

	/**
	 * 主键 选项ID
	 */
	private Long id;

	/**
	 * 问题ID
	 */
	private Integer questid;

	/**
	 * 选项值
	 */
	private String itemvalue;
	/**
	 * 投票或问卷主题UID
	 */
	private String themeid;
	/**
	 * 排序编号
	 */
	private String sortnum;

	/**
	 * 状态 默认0=有效 1=无效
	 */
	private Integer status;

	/**
	 * 操作人ID
	 */
	private String operatorid;

	/**
	 * 操作人姓名
	 */
	private String operatorname;

	/**
	 * 插入时间
	 */
	private String inserttime;

	/**
	 * 修改时间
	 */
	private String updatetime;

	public String getThemeid() {
		return themeid;
	}

	public void setThemeid(String themeid) {
		this.themeid = themeid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuestid() {
		return questid;
	}

	public void setQuestid(Integer questid) {
		this.questid = questid;
	}

	public String getItemvalue() {
		return itemvalue;
	}

	public void setItemvalue(String itemvalue) {
		this.itemvalue = itemvalue;
	}

	public String getSortnum() {
		return sortnum;
	}

	public void setSortnum(String sortnum) {
		this.sortnum = sortnum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOperatorid() {
		return operatorid;
	}

	public void setOperatorid(String operatorid) {
		this.operatorid = operatorid;
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public String getInserttime() {
		return inserttime;
	}

	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	public String getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(String updatetime) {
		this.updatetime = updatetime;
	}

}
