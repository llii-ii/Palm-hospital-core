package com.kasite.client.order.bean.dbo;

import javax.persistence.Id;
import javax.persistence.Table;

import com.kasite.core.common.bean.dbo.BaseDbo;

import tk.mybatis.mapper.annotation.KeySql;

/**
 * 二维码信息点
 * 
 * @author 無
 *
 */
@Table(name="QR_GUIDE")
public class Guide extends BaseDbo{
	/** 主键 */
	@Id
	@KeySql(useGeneratedKeys=true)
	private Long id;
	/** 标题 */
	private String title;
	/** 订单ID */
	private String orderId;
	/** 副标题 */
	private String subTitle;
	/** 1为正常 -1为删除 */
	private Integer status;
	
	private String content;
	
	/**
	 * 信息点用途
	 */
	private Integer usageType;

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getUsageType() {
		return usageType;
	}

	public void setUsageType(Integer usageType) {
		this.usageType = usageType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long guideId) {
		this.id = guideId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	
}
