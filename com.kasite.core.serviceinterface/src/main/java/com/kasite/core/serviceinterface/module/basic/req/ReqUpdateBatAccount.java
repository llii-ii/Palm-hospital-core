package com.kasite.core.serviceinterface.module.basic.req;

import org.dom4j.Element;

import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.AbsReq;
import com.kasite.core.common.util.XMLUtil;
import com.yihu.hos.exception.AbsHosException;
import com.yihu.wsgw.api.InterfaceMessage;
/**
 * @author lq
 * @version 1.0
 * 2017-7-24 下午2:29:38
 */
public class ReqUpdateBatAccount extends AbsReq{
	
	/**
	 * 主键
	 */
	private String accountId;
	/**
	 * 公众号名称
	 */
	private String gzhName;
	/**
	 * 微信号
	 */
	private String wxh;
	/**
	 * 原始ID
	 */
	private String ysid;
	/**
	 * 级别；1普通订阅号,2普通服务号，３认证订阅号,４认证服务号
	 */
	private String level;
	/**
	 * 微信公众平台后台的AppId
	 */
	private String appId;
	/**
	 * 微信公众平台后台的AppSecret
	 */
	private String appSecret;
	/**
	 * 接口地址
	 */
	private String interfaceUrl;
	/**
	 * 与公众平台接入设置值一致，必须为英文或者数字，长度为3到32个字符. 请妥善保管, Token 泄露将可能被窃取或篡改平台的操作数据.
	 */
	private String token;
	/**
	 * 与公众平台接入设置值一致，必须为英文或者数字，长度为43个字符. 请妥善保管, EncodingAESKey 泄露将可能被窃取或篡改平台的操作数据
	 */
	private String encodingAeskey;
	/**
	 * 二维码图片地址
	 */
	private String qrcode;
	/**
	 * 头像图片地址
	 */
	private String headUrl;
	/**
	 * 接入状态: 1连接成功；2连接失败
	 */
	private String connState;
	/**
	 * 创建时间
	 */
	private String createTime;
	/**
	 * 修改时间
	 */
	private String updateTime;
	/**
	 * 操作人姓名
	 */
	private String optName;
	/**
	 * 平台来源
	 */
	private String sysId;
	/**
	 *渠道id(100123微信;100125支付宝;100126直达号) 
	 */
	private String channelId;
	/**
	 * 1有效；2无效
	 */
	private Integer state;
	
	public String getAccountId() {
		return accountId;
	}
	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}
	public String getGzhName() {
		return gzhName;
	}
	public void setGzhName(String gzhName) {
		this.gzhName = gzhName;
	}
	public String getWxh() {
		return wxh;
	}
	public void setWxh(String wxh) {
		this.wxh = wxh;
	}
	public String getYsid() {
		return ysid;
	}
	public void setYsid(String ysid) {
		this.ysid = ysid;
	}
	public String getLevel() {
		return level;
	}
	public void setLevel(String level) {
		this.level = level;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getAppSecret() {
		return appSecret;
	}
	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}
	public String getInterfaceUrl() {
		return interfaceUrl;
	}
	public void setInterfaceUrl(String interfaceUrl) {
		this.interfaceUrl = interfaceUrl;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getEncodingAeskey() {
		return encodingAeskey;
	}
	public void setEncodingAeskey(String encodingAeskey) {
		this.encodingAeskey = encodingAeskey;
	}
	public String getQrcode() {
		return qrcode;
	}
	public void setQrcode(String qrcode) {
		this.qrcode = qrcode;
	}
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}
	public String getConnState() {
		return connState;
	}
	public void setConnState(String connState) {
		this.connState = connState;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getOptName() {
		return optName;
	}
	public void setOptName(String optName) {
		this.optName = optName;
	}
	public String getSysId() {
		return sysId;
	}
	public void setSysId(String sysId) {
		this.sysId = sysId;
	}
	public String getChannelId() {
		return channelId;
	}
	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public ReqUpdateBatAccount (InterfaceMessage msg) throws AbsHosException {
		super(msg);
		Element dataEl = root.element(KstHosConstant.DATA);
		this.accountId = XMLUtil.getString(dataEl, "AccountId", true);
		this.gzhName = XMLUtil.getString(dataEl, "GzhName", true);
		this.wxh = XMLUtil.getString(dataEl, "Wxh", false);
		this.ysid = XMLUtil.getString(dataEl, "Ysid", true);
		this.level = XMLUtil.getString(dataEl, "Level", false);
		this.appId = XMLUtil.getString(dataEl, "AppId", true);
		this.appSecret = XMLUtil.getString(dataEl, "AppSecret", true);
		this.interfaceUrl = XMLUtil.getString(dataEl, "InterfaceUrl", false);
		this.token = XMLUtil.getString(dataEl, "Token", false);
		this.encodingAeskey = XMLUtil.getString(dataEl, "EncodingAeskey", false);
		this.qrcode = XMLUtil.getString(dataEl, "Qrcode", false);
		this.headUrl = XMLUtil.getString(dataEl, "HeadUrl", false);
		this.connState = XMLUtil.getString(dataEl, "ConnState", false);
		this.optName = XMLUtil.getString(dataEl, "OptName", false);
		this.sysId = XMLUtil.getString(dataEl, "SysId", false);
		this.channelId = XMLUtil.getString(dataEl, "ChannelId", false);
	}
}
