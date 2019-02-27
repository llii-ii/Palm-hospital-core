package com.kasite.client.hospay.module.bill.entity.bill.bo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * @author cc
 * TODO 初始化微信/支付宝一些请求参数
 */
@Component
public class RequestHandlerParam {

     /************************微信请求参数*****************************/
     /** 查询订单URL*/
     @Value("${ChannelWXConfig.queryOrderUrl}")
     public String queryOrderUrl;
     /**退款URL*/
     @Value("${ChannelWXConfig.refundUrl}")
     public String refundUrl;
     /** 订阅号APPID*/
     @Value("${ChannelWXConfig.subAppId}")
     public String appId;
     /** 商户号(子)*/
     @Value("${ChannelWXConfig.subMerchantId}")
     public String merchantId;
     /** 商户密钥*/
     @Value("${ChannelWXConfig.parentMerchantKey}")
     public String merchantKey;
     /** 父APPID（子商户模式启用）*/
     @Value("${ChannelWXConfig.parentAppId}")
     public String parentAppId;
     /** 父商户号（子商户模式启用）*/
     @Value("${ChannelWXConfig.parentMerchantId}")
     public String parentMerchantId;
     /** 退费证书*/
     @Value("${ChannelWXConfig.certPath}")
     public String certPath;
     /** 异步通知地址*/
     @Value("${ChannelWXConfig.notifyUrl}")
     public String notifyUrl;

     /**************************支付宝请求参数*************************/
     /** 接口网关*/
     @Value("${ChannelZFBConfig.alipayGateWay}")
     public String alipayGateWay;
     /** 支付宝AppId*/
     @Value("${ChannelZFBConfig.appId}")
     public String zfbAppId;
     /** 应用私钥*/
     @Value("${ChannelZFBConfig.privateKey}")
     public String privateKey;
     /** 应用公钥*/
     @Value("${ChannelZFBConfig.publicKey}")
     public String publicKey;
     /** 支付宝公钥*/
     @Value("${ChannelZFBConfig.alipayPublicKey}")
     public String alipayPublicKey;
     /** 签名类型*/
     @Value("${ChannelZFBConfig.signType}")
     public String signType;


     /****************************用户授权参数*****************************/
     /** 用户名*/
     @Value("${AuthenticatorConfig.userName}")
     public String userName;
     /** 主机名*/
     @Value("${AuthenticatorConfig.hostName}")
     public String hostName;
     /** 秘钥*/
     @Value("${AuthenticatorConfig.secret}")
     public String secret;
     /** 密码*/
     @Value("${AuthenticatorConfig.passWord}")
     public String passWord;
     /** 汇总时间限制开关*/
     @Value("${AuthenticatorConfig.isDefault}")
     public String isDefault;


     /****************************页面配置参数******************************/
     /** 页面配置参数*/
     @Value("${Page.operationButton}")
     public String operationButton;
     /** 每日汇总页面是否展示*/
     @Value("${Page.everyDayBillIsShow}")
     public String everyDayBillIsShow;
     /** 每日汇总明细页面是否展示*/
     @Value("${Page.everyDayBillDetailIsShow}")
     public String everyDayBillDetailIsShow;
     /** 分类汇总页面是否展示*/
     @Value("${Page.classifySummaryBillIsShow}")
     public String classifySummaryBillIsShow;
     /** 实时退费页面是否展示*/
     @Value("${Page.alwaysRefundIsShow}")
     public String alwaysRefundIsShow;

     /****************************几方汇总配置参数******************************/
     @Value("${several.parties}")
     public String severalParties;

     /****************************Excel文件保存路径******************************/
     @Value("${file.path}")
     public String filePath;

     /****************************统一对账系统配置参数******************************/
     @Value("${balanceConfig.operationJson}")
     public String operationJson;

     /****************************HIS配置参数************************************/
     @Value("${HisConfig.url}")
     public String hisUrl;
     @Value("${HisConfig.instance}")
     public String hisClientInstance;
     @Value("${HisConfig.type}")
     public String hisType;


}
