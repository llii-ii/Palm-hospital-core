package com.kasite.client.wechat.message.resp;

import java.util.List;

import org.dom4j.Element;

/**
 * @author MECHREV
 */
public class NewsMessage extends BaseMessage<NewsMessage> {  
	/** 图文消息个数，限制为10条以内*/
    private String articleCount;  
    /** 多条图文消息信息，默认第一个item为大图*/
    private List<Article> articles;  

  
    public String getArticleCount() {  
        return articleCount;  
    }  
  
    public void setArticleCount(String articleCount) {  
    	this.articleCount = articleCount;  
    }  
  
    public List<Article> getArticles() {  
        return this.articles;  
    }  
  
    public void setArticles(List<Article> articles) {  
    	this.articles = articles;  
    }
    @Override
    public void addNodeCDATA(Element root,String name,String val)throws Exception{
    	Element e = root.addElement(name);
    	e.addCDATA(val);
    }
    
    public void addNodesCDATA(Element root,List<Article> articles)throws Exception{
    	Element e = root.addElement("Articles");
    	for (Article obj : articles) {
    		Element item = e.addElement("item");
    		Element title = item.addElement("Title");
    		title.addCDATA(obj.getTitle());
    		Element description = item.addElement("Description");
    		description.addCDATA(obj.getDescription());
    		Element picUrl = item.addElement("PicUrl");
    		picUrl.addCDATA(obj.getPicUrl());
    		Element url = item.addElement("Url");
    		url.addCDATA(obj.getUrl());
		}
    }
	@Override
	public String make(Element root) throws Exception {
		super.makeBasic(root);
		this.addNodeCDATA(root,"ArticleCount",articleCount);
		addNodesCDATA(root,articles);
		return root.asXML();
	}  
} 