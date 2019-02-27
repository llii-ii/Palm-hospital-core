package com.kasite.core.serviceinterface.module.qywechat.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 故障报修req
 * 
 * @author 無
 *
 */
public class ReqFaultRepair extends AbsReq {
	/**
	 * 主键ID
	 */
	private Long id;
	/**
	 * 订单编号
	 */
	private String orderId;
	/**
	 * 故障标题
	 */
	private String title;
	/**
	 * 故障描述
	 */
	private String content;
	/**
	 * 申请人ID
	 */
	private String applyerId;
	/**
	 * 申请人姓名
	 */
	private String applyerName;

	/**
	 * 受理人/分配人 ID 多个用逗号分隔
	 */
	private String receiverId;

	/**
	 * 受理人/分配人 姓名 多个用逗号分隔
	 */
	private String receiverName;
	/**
	 * 维修人ID 多个逗号分隔
	 */
	private String repairerId;
	/**
	 * 维修人姓名 多个逗号分隔
	 */
	private String repairerName;
	/**
	 * 验收结果
	 */
	private String result;
	/**
	 * 订单状态 0=待受理 1=处理中 2=已驳回 3=已完成 4=已验收
	 */
	private Integer status;
	/**
	 * 申请时间
	 */
	private String insertTime;
	/**
	 * 修改时间
	 */
	private String updateTime;

	public ReqFaultRepair(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element ser = root.element(KstHosConstant.DATA);
		this.id = XMLUtil.getLong(ser, "id", true);
		this.orderId = XMLUtil.getString(ser, "orderId", false);
		this.title = XMLUtil.getString(ser, "title", false);
		this.content = XMLUtil.getString(ser, "content", false);
		this.applyerId = XMLUtil.getString(ser, "applyerId", false);
		this.applyerName = XMLUtil.getString(ser, "applyerName", false);
		this.receiverId = XMLUtil.getString(ser, "receiverId", false);
		this.result = XMLUtil.getString(ser, "result", false);
		this.receiverName = XMLUtil.getString(ser, "receiverName", false);
		this.repairerId = XMLUtil.getString(ser, "repairerId", false);
		this.repairerName = XMLUtil.getString(ser, "repairerName", false);
		this.status = XMLUtil.getInt(ser, "status", false);
		this.insertTime = XMLUtil.getString(ser, "insertTime", false);
		this.updateTime = XMLUtil.getString(ser, "updateTime", false);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getApplyerId() {
		return applyerId;
	}

	public void setApplyerId(String applyerId) {
		this.applyerId = applyerId;
	}

	public String getApplyerName() {
		return applyerName;
	}

	public void setApplyerName(String applyerName) {
		this.applyerName = applyerName;
	}

	public String getReceiverId() {
		return receiverId;
	}

	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getRepairerId() {
		return repairerId;
	}

	public void setRepairerId(String repairerId) {
		this.repairerId = repairerId;
	}

	public String getRepairerName() {
		return repairerName;
	}

	public void setRepairerName(String repairerName) {
		this.repairerName = repairerName;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(String insertTime) {
		this.insertTime = insertTime;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
