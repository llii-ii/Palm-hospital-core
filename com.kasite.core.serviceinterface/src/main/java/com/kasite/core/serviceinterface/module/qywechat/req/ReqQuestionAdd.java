package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * REQ增加问题
 * 
 * @author 無
 *
 */
public class ReqQuestionAdd extends AbsReq {
	public ReqQuestionAdd(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.themeid = XMLUtil.getString(dataEl, "themeId", true);
		this.questname = XMLUtil.getString(dataEl, "questName", true);
		this.questtype = XMLUtil.getInt(dataEl, "questType", true, null);
		this.ismust = XMLUtil.getInt(dataEl, "isMust", false, null);
		this.operatorid = XMLUtil.getString(dataEl, "operatorId", false);
		this.operatorname = XMLUtil.getString(dataEl, "operatorName", false);
		this.itemArray = XMLUtil.getString(dataEl, "itemArray", false);
	}

	/**
	 * 主键 问题ID
	 */
	private Long id;

	/**
	 * 投票或问卷主题ID
	 */
	private String themeid;

	/**
	 * 问题名称
	 */
	private String questname;

	/**
	 * 问题类型 0=单选 1=多选 2=问答 3=打分
	 */
	private Integer questtype;

	/**
	 * 排序编号
	 */
	private Integer sortnum;

	/**
	 * 状态 0=有效 1=无效
	 */
	private Integer status;

	/**
	 * 该题是否必答 默认0=是 1=否
	 */
	private Integer ismust;

	/**
	 * 操作人ID
	 */
	private String operatorid;

	/**
	 * 操作人姓名
	 */
	private String operatorname;

	/**
	 * 问题选项数组
	 */
	private String itemArray;

	public String getItemArray() {
		return itemArray;
	}

	public void setItemArray(String itemArray) {
		this.itemArray = itemArray;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getThemeid() {
		return themeid;
	}

	public void setThemeid(String themeid) {
		this.themeid = themeid;
	}

	public String getQuestname() {
		return questname;
	}

	public void setQuestname(String questname) {
		this.questname = questname;
	}

	public Integer getQuesttype() {
		return questtype;
	}

	public void setQuesttype(Integer questtype) {
		this.questtype = questtype;
	}

	public Integer getSortnum() {
		return sortnum;
	}

	public void setSortnum(Integer sortnum) {
		this.sortnum = sortnum;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getIsmust() {
		return ismust;
	}

	public void setIsmust(Integer ismust) {
		this.ismust = ismust;
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

}
