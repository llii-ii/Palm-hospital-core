package com.kasite.client.qywechat.bean.message.resp;

import java.util.List;

/**
 * @desc : 图文消息
 * 
 * @author: shirayner
 * @date : 2017-8-17 下午2:03:31
 */
public class RespNewsMessage extends RespBaseMessage {
	// 图文消息个数，限制为10条以内
	private int ArticleCount;
	// 多条图文消息信息，默认第一个item为大图
	private List<RespArticle> Articles;

	public int getArticleCount() {
		return ArticleCount;
	}

	public void setArticleCount(int articleCount) {
		ArticleCount = articleCount;
	}

	public List<RespArticle> getArticles() {
		return Articles;
	}

	public void setArticles(List<RespArticle> articles) {
		Articles = articles;
	}
}