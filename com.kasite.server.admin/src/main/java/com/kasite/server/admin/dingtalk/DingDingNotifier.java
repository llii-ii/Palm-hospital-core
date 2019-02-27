package com.kasite.server.admin.dingtalk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.coreframework.util.StringUtil;
import com.google.gson.JsonObject;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;
import com.kasite.server.admin.util.PropertyUtil;
import com.kasite.server.admin.wechat.SendMessageService;
import com.kasite.server.admin.wechat.WeiXinUtil;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.notify.AbstractStatusChangeNotifier;
import reactor.core.publisher.Mono;

public class DingDingNotifier extends AbstractStatusChangeNotifier {
	
	private static final Logger logger = LoggerFactory.getLogger(DingDingNotifier.class);
	
    private RestTemplate restTemplate = new RestTemplate();

    public DingDingNotifier(InstanceRepository repository) {
        super(repository);
    }
    
    public static void main(String[] args) {
		try {
			String posturl = "https://sxakzyy.kasitesoft.com/api/ping.do";
			SoapResponseVo result = HttpRequestBus.create(posturl, RequestType.get).send();
			System.out.println(result.getResult());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    
    @Override
    public Mono<Void> doNotify(InstanceEvent event, Instance instance) {
    	logger.info("状态变更："+instance.toString());
    	String timeName ="告警时间";
    	String evenName ="告警信息";
    	String hosId = "";
    	if(null!=instance.getInfo() && null!=instance.getInfo().getValues()) {
    		hosId = instance.getInfo().getValues().get("orgCode")+"";
    		logger.info("医院ID="+hosId);
    	}
    	if("UP".equals(instance.getStatusInfo().getStatus())) {
    		timeName ="上线时间";
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
    	
		String touser = "";
		String toparty = "";
		Boolean toAllUser = false;
		String author = "spring admin";
		
		net.sf.json.JSONObject formJson = new net.sf.json.JSONObject();
		formJson.put(timeName, time);
		formJson.put("应用ID", instance.getId()+"");
		formJson.put("医院ID", hosId);
		if (StringUtil.isNotBlank(instance.getStatusInfo().getDetails().get("exception"))) {
			formJson.put("异常信息", instance.getStatusInfo().getDetails().get("exception")+"");
		}
		if (StringUtil.isNotBlank(instance.getStatusInfo().getDetails().get("message"))) {
			formJson.put(evenName, instance.getStatusInfo().getDetails().get("message")+"");
		}
		String imageUrl = "";
		String content = "";
		String num = "";
		String unit = "";
		String messageUrl = "";
    	String title =instance.getRegistration().getName() + " is now " + instance.getStatusInfo().getStatus();
    	
    	if(StringUtil.isBlank(hosId)) {
			touser = PropertyUtil.getProperty("10000");
			logger.info("默认接收人="+touser);
		}else {
			touser = PropertyUtil.getProperty(hosId);
		}
    	
    	//医院告警时 加上默认推送人
		if(StringUtil.isNotBlank(touser) && !"10000".equals(hosId)) {
			String deaulf=PropertyUtil.getProperty("10000");
			logger.info("默认接收人:"+deaulf);
			if(StringUtil.isNotBlank(deaulf)) {
				String[] deaulfArr= deaulf.split("\\|");
				for (int i = 0; i < deaulfArr.length; i++) {
					if(touser.indexOf(deaulfArr[i])==-1) {
						touser +="|"+deaulfArr[i];
					}
				}
			}
		}
    	//获取用户名
		if (StringUtil.isNotBlank(touser)) {
			String touserName="";
			try {
				touserName = DingTalkUtil.getUserNamePL(touser.toLowerCase());
			} catch (Exception e) {
				touserName = touser;
			}
			formJson.put("接收人", touserName);
			touser = touser.toLowerCase().replace("|", ",");
			DingTalkUtil.sendDingDingOAMessage(touser, toparty, toAllUser, author, formJson, imageUrl, content, num, unit,messageUrl,title);
		}
		
    	return Mono.fromRunnable(() -> restTemplate.postForEntity(WeiXinUtil.sendMessage_url, null,Void.class));
    }
	
}

