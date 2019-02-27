package com.kasite.server.admin.wechat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.coreframework.util.StringUtil;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.server.admin.util.PropertyUtil;
import com.kasite.server.admin.wechat.pbo.Textcard;
import com.kasite.server.admin.wechat.pbo.TextcardMessage;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import reactor.core.publisher.Mono;

public class WeChatNotifier extends AbstractStatusChangeNotifier {
	
	private static final Logger logger = LoggerFactory.getLogger(WeChatNotifier.class);
	
    private RestTemplate restTemplate = new RestTemplate();

    public WeChatNotifier(InstanceRepository repository) {
        super(repository);
    }
    
    @Override
    public Mono<Void> doNotify(InstanceEvent event, Instance instance) {
    	logger.info("状态变更："+instance.toString());
    	logger.info(instance.getRegistration().getName() + " is now " + instance.getStatusInfo().getStatus());
    	logger.info(instance.getStatusTimestamp() +":"+instance.getStatusInfo().getDetails().get("message"));
    	String timeName ="告警时间:";
    	String evenName ="告警信息:";
    	String hosId = "";
    	if(null!=instance.getInfo() && null!=instance.getInfo().getValues()) {
    		hosId = instance.getInfo().getValues().get("orgCode")+"";
    		logger.info("医院ID="+hosId);
    	}
    	if("UP".equals(instance.getStatusInfo().getStatus())) {
    		timeName ="上线时间:";
        	evenName ="";
    	}else {
    		//离线或者下线时 判断是否网络波动
    		try {
    			String api = instance.getRegistration().getServiceUrl()+"/api/ping.do";
    			SoapResponseVo result = HttpRequestBus.create(api, RequestType.get).send();
    			if("success".equals(result.getResult())) {
    				return Mono.fromRunnable(() -> restTemplate.postForEntity(WeiXinUtil.sendMessage_url, null,Void.class));
    			}
    		} catch (Exception e) {
    			System.out.println(e.getMessage());
    		}
    	}
    	//转换时间格式
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
    	SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String time = instance.getStatusTimestamp().toString();
    	try {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			Date date = sdf.parse(time);
			time = sdf2.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	//内容
    	String title =instance.getRegistration().getName() + " is now " + instance.getStatusInfo().getStatus();
		String description = "<div class=\"gray\">" + timeName + time + "</div>"
						   + "<div class=\"normal\">应用ID:" + instance.getId() + "</div>"
						   + "<div class=\"normal\">医院ID:" + hosId + "</div>";
		if (StringUtil.isNotBlank(instance.getStatusInfo().getDetails().get("exception"))) {
			description += "<div class=\"normal\">" + "异常信息:" + instance.getStatusInfo().getDetails().get("exception") + "</div>";
		}
		if (StringUtil.isNotBlank(instance.getStatusInfo().getDetails().get("message"))) {
			description += "<div class=\"normal\">" + evenName + instance.getStatusInfo().getDetails().get("message") + "</div>";
		}
		//发送卡片消息
//		SendTextcardMessage(title, description);
		String touser = "";
		if(StringUtil.isBlank(hosId)) {
			touser = PropertyUtil.getProperty("10000");
			logger.info("默认接收人="+touser);
		}
		SendMessageService.SendTextCardMessage(title, description, "", touser, "",hosId);
		
    	return Mono.fromRunnable(() -> restTemplate.postForEntity(WeiXinUtil.sendMessage_url, null,Void.class));
    	
    }
	
}
