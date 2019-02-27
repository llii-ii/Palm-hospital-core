package com.kasite.core.serviceinterface.module.basic.resp;

import com.kasite.core.common.resp.AbsResp;

/**
 * 
 * @className: RespQueryArticle
 * @author: lcz
 * @date: 2018年7月23日 下午1:39:16
 */
public class RespQueryArticle  extends AbsResp{
	private String articleId;
	private String title;
	private String bigImgUrl;
	private String isSueDate;
	private String content;
	
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
	public String getBigImgUrl() {
		return bigImgUrl;
	}
	public void setBigImgUrl(String bigImgUrl) {
		this.bigImgUrl = bigImgUrl;
	}
	public String getIsSueDate() {
		return isSueDate;
	}
	public void setIsSueDate(String isSueDate) {
		this.isSueDate = isSueDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
	
}
