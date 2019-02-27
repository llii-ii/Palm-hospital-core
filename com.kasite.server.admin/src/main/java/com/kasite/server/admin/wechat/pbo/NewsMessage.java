package com.kasite.server.admin.wechat.pbo;  

  
/**
 * 图文消息
 * @author 無
 *
 */
public class NewsMessage extends BaseMessage {  
    //图文
    private News news;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }
    


}  