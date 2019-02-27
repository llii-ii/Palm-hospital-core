package com.kasite.client.wechat.message.req;

import java.util.List;
import java.util.Map;

import org.dom4j.Element;

import com.kasite.core.common.util.StringUtil;


/**
 * @author MECHREV
 */
public class NewsMessage extends BaseMessage<NewsMessage> {  
	/** 图文消息个数，限制为10条以内*/
    private int articleCount;  
    /** 多条图文消息信息，默认第一个item为大图*/
    private List<Article> articles;  

  
    public int getArticleCount() {  
        return articleCount;  
    }  
  
    public void setArticleCount(int articleCount) {  
    	this.articleCount = articleCount;  
    }  
  
    public List<Article> getArticles() {  
        return articles;  
    }  
  
    public void setArticles(List<Article> articles) {  
    	this.articles = articles;  
    }

    public void addNodeCDATA(Element root,String name,String val)throws Exception{
    	Element e = root.addElement(name);
    	e.addCDATA(val);
    }
    
    @Override
	public NewsMessage parse(Map<String, String> map) throws Exception {
		super.parseBasic(map);
		this.articleCount = StringUtil.isEmpty(articles)?0:articles.size();
		return this;
	}  
} 