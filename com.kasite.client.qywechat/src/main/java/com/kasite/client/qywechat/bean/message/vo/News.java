package com.kasite.client.qywechat.bean.message.vo;

import java.util.List;

/**
 * 图文
 * 
 * @author 無
 *
 */
public class News {
	// 文章列表
	private List<Article> articles;

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}

}