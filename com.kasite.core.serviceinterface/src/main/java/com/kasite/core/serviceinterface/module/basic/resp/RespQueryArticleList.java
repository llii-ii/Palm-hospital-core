package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryArticleList
 * @author: lcz
 * @date: 2018年7月23日 上午11:49:31
 */
public class RespQueryArticleList extends AbsResp{
	
	private String articleId;
	
	private String title;
	
	private String createDate;
	
	private Integer status;
	
	private Integer type;
	
	private String bigImgUrl;
	
	private String imgUrl;
	
	private Integer typeClass;
	
	private String isSueDate;
	
	private String linkUrl;
	
	private String lastModify;
	
	private Integer isHead;
	
	private Integer finalDeal;
	
	private String contents;
	
	private String hosId;

	public String getArticleId() {
		return articleId;
	}

	public void setArticleId(String articleId) {
		this.articleId = articleId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBigImgUrl() {
		return bigImgUrl;
	}

	public void setBigImgUrl(String bigImgUrl) {
		this.bigImgUrl = bigImgUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Integer getTypeClass() {
		return typeClass;
	}

	public void setTypeClass(Integer typeClass) {
		this.typeClass = typeClass;
	}

	public String getIsSueDate() {
		return isSueDate;
	}

	public void setIsSueDate(String isSueDate) {
		this.isSueDate = isSueDate;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLastModify() {
		return lastModify;
	}

	public void setLastModify(String lastModify) {
		this.lastModify = lastModify;
	}

	public Integer getIsHead() {
		return isHead;
	}

	public void setIsHead(Integer isHead) {
		this.isHead = isHead;
	}

	public Integer getFinalDeal() {
		return finalDeal;
	}

	public void setFinalDeal(Integer finalDeal) {
		this.finalDeal = finalDeal;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getHosId() {
		return hosId;
	}

	public void setHosId(String hosId) {
		this.hosId = hosId;
	}
	
}
