package com.kasite.core.serviceinterface.module.msg.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * @author wufadong 2017年12月11日 17:31:15 
 * TODO 短信推送请求对象
 */
public class ReqSendSms extends AbsReq{
	/**
	 * 短信手机号
	 */
	private String moblie;
	/**
	 * 短信内容
	 */
	private String content;
	/**
	 * 调用方渠道号
	 */
	private String channelId;
	/**
	 * 调用OperatorId
	 */
	private String operatorId;
	/**
	 * 调用OperatorName
	 */
	private String operatorName;
	
	/**
	 * 阿里云的短信模版Code  如果是通过阿里云发送的话 必填:短信模板-可在短信控制台中找到 
	 */
	private String templateCode;
	/**
	 * 阿里云的短信模版对应的内容 模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为 : {"name":'张三',"code": 123456 }
	 */
	private String templateParam;
	/**
	 * 阿里云的短信模版 短信签名-可在短信控制台中找到 如果为空默认是yml中配置的签名，如果没有配置签名则默认是 ：卡思特
	 */
	private String signName;
	
	
	/**
	 * @param msg
	 * @throws AbsHosException
	 */
	public ReqSendSms(InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEL = root.element(KstHosConstant.DATA);
		this.moblie =  XMLUtil.getString(dataEL, "Moblie", true);
		this.content =  XMLUtil.getString(dataEL, "Content", false);
		this.channelId = XMLUtil.getString(dataEL, "ChannelId", false);
		this.operatorId = XMLUtil.getString(dataEL, "OperatorId", false);
		this.operatorName = XMLUtil.getString(dataEL, "OperatorName", false);
		this.templateCode = XMLUtil.getString(dataEL, "TemplateCode", false);
		this.templateParam = XMLUtil.getString(dataEL, "TemplateParam", false);
		this.signName = XMLUtil.getString(dataEL, "SignName", false);
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getTemplateParam() {
		return templateParam;
	}

	public void setTemplateParam(String templateParam) {
		this.templateParam = templateParam;
	}

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getMoblie() {
		return moblie;
	}

	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	/**
	 * @Title: ReqSendSms
	 * @Description: 
	 * @param msg
	 * @param moblie
	 * @param content
	 * @param channelId
	 * @param operatorId
	 * @param operatorName
	 * @throws AbsHosException
	 */
	public ReqSendSms(InterfaceMessage msg, String moblie, String content, 
			String channelId, String operatorId, String operatorName,
			String signName,String templateCode,String templateParam) throws AbsHosException {
		super(msg);
		this.moblie = moblie;
		this.content = content;
		this.channelId = channelId;
		this.operatorId = checkOperatorId(operatorId);
		this.operatorName = checkOperatorName(operatorName);
		this.templateCode = templateCode;
		this.templateParam = templateParam;
		this.signName = signName;
	}
	
	
}
