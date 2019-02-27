package com.kasite.server.admin.wechat;

import java.util.ArrayList;
import java.util.List;

import com.kasite.server.admin.wechat.pbo.Article;
import com.kasite.server.admin.wechat.pbo.FileMessage;
import com.kasite.server.admin.wechat.pbo.ImgMessage;
import com.kasite.server.admin.wechat.pbo.Media;
import com.kasite.server.admin.wechat.pbo.News;
import com.kasite.server.admin.wechat.pbo.NewsMessage;
import com.kasite.server.admin.wechat.pbo.Text;
import com.kasite.server.admin.wechat.pbo.TextMessage;
import com.kasite.server.admin.wechat.pbo.Textcard;
import com.kasite.server.admin.wechat.pbo.TextcardMessage;
import com.kasite.server.admin.wechat.pbo.Video;
import com.kasite.server.admin.wechat.pbo.VideoMessage;
import com.kasite.server.admin.wechat.pbo.VoiceMessage;


/**@desc  : 消息推送之发送消息
 * 
 * @author: shirayner
 * @date  : 2017-8-18 上午10:04:55
 */
public class SendMessageTest {
	
	public static void main(String[] args) throws Exception {
		
//		Date date = DateOper.parse("2018-11-21T06:06:09.475Z");
//		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//		System.out.println(df2.format(new Date()));
//		
//		Long now = System.currentTimeMillis()+ 7200*100;
//		System.out.println(now);
//		Long time = now + 7200*1000;
//		System.out.println(time);
//		
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
//		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
//		Date date = formatter.parse("2018-11-21T06:06:09.475Z");
//		SimpleDateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//转换后的格式
//		System.out.println("NewDate-->"+FORMATTER.format(date));

//new WeiXinUtil()
		testSendTextMessage();
//		testSendTextMessage();
//		testSendTextcardMessage();
//	String accessToken= WeiXinUtil.getAccessToken(WeiXinUtil.corpId, WeiXinUtil.agentSecret).getToken();
	String accessToken = "QiarbgE5W48tD2rR6Aykwfc58_N6IlSlmDovLpFbSlErlucj-W6jN0m8AMJk_HX41IKQRnxekPE6qzaAwnPgpUI8zEmB1l7MN8kFJemvP1qFjC3v_E6LJdKfS08Cn-nVQF1ny6Vy-yQHwctq2fMn-tsVbqSlF6SKPMA3NDPC96Z8VRtgB5jApSiDiZbtkT7XQ5fncRez33KavTaEceAe-rA"; 
		//上传临时素材
//      String fileUrl="D:/1.jpg";
//      String type="image";
//      String media_id = new TempMaterialService().uploadTempMaterial(accessToken, type, fileUrl).getString("media_id");		
//      testSendImgMessage(media_id);
		
//		String fileUrl="D:/haicaowu1.mp3";
//	    String type="video";
//	    String media_id = new TempMaterialService().uploadTempMaterial(accessToken, type, fileUrl).getString("media_id");
//		testSendVoiceMessage(media_id);
		
//		String fileUrl="D:/1.mp4";
//	    String type="video";
//	    String media_id = new TempMaterialService().uploadTempMaterial(accessToken, type, fileUrl).getString("media_id");
//		testSendVideoMessage(media_id);
		
//		String fileUrl="D:/中奖名单.txt";
//	    String type="file";
//	    String media_id = new TempMaterialService().uploadTempMaterial(accessToken, type, fileUrl).getString("media_id");
//		testSendFileMessage(media_id);
		
//		testSendNewsMessage();
	}
	
    //1.发送文本消息
    public static void testSendTextMessage(){
        //0.设置消息内容
        String content="恭喜你抽中纸巾一包，领奖码：3523。\n请于2018年11月20日前联系黄嘤嘤领取。\n详情请见" +
                "<a href=\"https://shop.zhe800.com/products/ze150924150248000588\">勿相忘纸巾" +
                "</a>，本消息由JAVA发送。";

        //1.创建文本消息对象
        TextMessage message=new TextMessage();
        //1.1非必需
        message.setTouser("fage");  //不区分大小写
        //textMessage.setToparty("1");
        //txtMsg.setTotag(totag);
        //txtMsg.setSafe(0);

        //1.2必需
        message.setMsgtype("text");
        message.setAgentid(WeiXinUtil.agentId);

        Text text=new Text();
        text.setContent(content);
        message.setText(text);

        //2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
//        String accessToken= WeiXinUtil.getAccessToken(WeiXinUtil.corpId, WeiXinUtil.agentSecret).getToken();
//        System.out.println("accessToken:"+accessToken);

        //3.发送消息：调用业务类，发送消息
        WeiXinUtil.sendMessage(message);

    }

    //2.发送文本卡片消息
    public static void testSendTextcardMessage(){
        //0.设置消息内容
        String title="系统通知";
        String description="<div class=\"gray\">2018年11月19日</div> "
        		+ "<div class=\"normal\">恭喜你抽中纸巾一包，领奖码：3523</div>"
        		+ "<div class=\"highlight\">请于2018年11月20日前联系黄嘤嘤领取</div>"
        		+ "<div>本消息由JAVA发送</div>"
        		+ "<div><img src='http://a.hiphotos.baidu.com/image/pic/item/d009b3de9c82d158ec9917f38d0a19d8bc3e425c.jpg' height='' weight='' alt='' /></div>";
        		//http://a.hiphotos.baidu.com/image/pic/item/d009b3de9c82d158ec9917f38d0a19d8bc3e425c.jpg
        String url="https://shop.zhe800.com/products/ze150924150248000588?jump_source=1&qd_key=qyOwt6Jn";

        //1.创建文本卡片消息对象
        TextcardMessage message=new TextcardMessage();
        //1.1非必需
        message.setTouser("fage");  //不区分大小写
        //message.setToparty("1");
        //message.setTotag(totag);
        //message.setSafe(0);

        //1.2必需
        message.setMsgtype("textcard");
        message.setAgentid(WeiXinUtil.agentId);

        Textcard textcard=new Textcard();
        textcard.setTitle(title);
        textcard.setDescription(description);
        textcard.setUrl(url);
        message.setTextcard(textcard);

        //2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
//        String accessToken= WeiXinUtil.getAccessToken(WeiXinUtil.corpId, WeiXinUtil.agentSecret).getToken();
//        System.out.println("accessToken:"+accessToken);

        //3.发送消息：调用业务类，发送消息
        WeiXinUtil.sendMessage(message);

    }

    //3.发送图片消息---无效的media_id
    public static void testSendImgMessage(String media_id){
        //0.设置消息内容
//        String media_id="MEDIA_ID";
        //1.创建图片消息对象
        ImgMessage message=new ImgMessage();
        //1.1非必需
        message.setTouser("fage");  //不区分大小写
        //textMessage.setToparty("1");
        //txtMsg.setTotag(totag);
        //txtMsg.setSafe(0);

        //1.2必需
        message.setMsgtype("image");
        message.setAgentid(WeiXinUtil.agentId);

        Media image=new Media();
        image.setMedia_id(media_id);
        message.setImage(image);

        //2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
//        String accessToken= WeiXinUtil.getAccessToken(WeiXinUtil.corpId, WeiXinUtil.agentSecret).getToken();
//        System.out.println("accessToken:"+accessToken);

        //3.发送消息：调用业务类，发送消息
        WeiXinUtil.sendMessage(message);

    }


    //4.发送语音消息---无效的media_id
    public static void testSendVoiceMessage(String media_id){
        //0.设置消息内容
//        String media_id="MEDIA_ID";
        //1.创建语音消息对象
        VoiceMessage message=new VoiceMessage();
        //1.1非必需
        message.setTouser("@all");  //不区分大小写
        //textMessage.setToparty("1");
        //txtMsg.setTotag(totag);
        //txtMsg.setSafe(0);

        //1.2必需
        message.setMsgtype("voice");
        message.setAgentid(WeiXinUtil.agentId);

        Media voice=new Media();
        voice.setMedia_id(media_id);
        message.setVoice(voice);

        //2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
//        String accessToken= WeiXinUtil.getAccessToken(WeiXinUtil.corpId, WeiXinUtil.agentSecret).getToken();
//        System.out.println("accessToken:"+accessToken);

        //3.发送消息：调用业务类，发送消息
        WeiXinUtil.sendMessage(message);

    }

    //5.发送视频消息
    public static void testSendVideoMessage(String media_id){
        //0.设置消息内容
//        String media_id="MEDIA_ID";
        String title="奖品展示";
        String description="请以实物为准";


        //1.创建视频消息对象
        VideoMessage message=new VideoMessage();
        //1.1非必需
        message.setTouser("@all");  //不区分大小写
        //message.setToparty("1");
        //message.setTotag(totag);
        //message.setSafe(0);

        //1.2必需
        message.setMsgtype("video");
        message.setAgentid(WeiXinUtil.agentId);

        Video video=new Video();
        video.setMedia_id(media_id);
        video.setTitle(title);
        video.setDescription(description);
        message.setVideo(video);

        //2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
//        String accessToken= WeiXinUtil.getAccessToken(WeiXinUtil.corpId, WeiXinUtil.agentSecret).getToken();
//        System.out.println("accessToken:"+accessToken);

        //3.发送消息：调用业务类，发送消息
        WeiXinUtil.sendMessage(message);

    }

    //6.发送文件消息
    public static void testSendFileMessage(String media_id){
        //0.设置消息内容
//        String media_id="MEDIA_ID";

        //1.创建文件对象
        FileMessage message=new FileMessage();
        //1.1非必需
        message.setTouser("@all");  //不区分大小写
        //textMessage.setToparty("1");
        //txtMsg.setTotag(totag);
        //txtMsg.setSafe(0);

        //1.2必需
        message.setMsgtype("file");
        message.setAgentid(WeiXinUtil.agentId);

        Media file=new Media();
        file.setMedia_id(media_id);
        message.setFile(file);

        //2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
//        WeiXinUtil.getAccessToken(WeiXinUtil.corpId, WeiXinUtil.agentSecret);

        //3.发送消息：调用业务类，发送消息
        WeiXinUtil.sendMessage(message);

    }

    //7.发送图文消息
    public static void testSendNewsMessage(){

        //1.创建图文消息对象
        NewsMessage message=new NewsMessage();
        //1.1非必需
        message.setTouser("fage");  //不区分大小写
        //textMessage.setToparty("1");
        //txtMsg.setTotag(totag);
        //txtMsg.setSafe(0);

        //1.2必需
        message.setMsgtype("news");
        message.setAgentid(WeiXinUtil.agentId);
        //设置图文消息
        Article article1=new  Article();
        article1.setPicurl("https://z3.tuanimg.com/imagev2/trade/600x600.e840ced8f52cfdabb947a9b4d138bfa3.400x.jpg");
        article1.setTitle("中奖通知");
        article1.setDescription("恭喜你抽中纸巾一包，领奖码：3523。\n请于2018年11月20日前联系黄嘤嘤领取");
       
        article1.setUrl("https://shop.zhe800.com/products/ze150924150248000588");
        
        List<Article>  articles=new ArrayList<Article>();
        articles.add(article1);
        
        News news=new News();
        news.setArticles(articles);
        message.setNews(news);

        //2.获取access_token:根据企业id和通讯录密钥获取access_token,并拼接请求url
//        String accessToken= WeiXinUtil.getAccessToken(WeiXinUtil.corpId, WeiXinUtil.agentSecret).getToken();
//        System.out.println("accessToken:"+accessToken);

        //3.发送消息：调用业务类，发送消息
        WeiXinUtil.sendMessage(message);

    }




}