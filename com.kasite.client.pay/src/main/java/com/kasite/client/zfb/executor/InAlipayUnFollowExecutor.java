/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.kasite.client.zfb.executor;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.coreframework.util.DateOper;
import com.kasite.client.zfb.util.AlipayMsgBuildUtil;
import com.kasite.core.common.bean.bo.AuthInfoVo;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.ApiModule;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.util.SpringContextUtil;
import com.kasite.core.common.util.XMLUtil;
import com.kasite.core.serviceinterface.module.rf.IReportFormsService;
import com.yihu.wsgw.api.InterfaceMessage;

import net.sf.json.JSONObject;


/**
 * 取消关注服务窗执行器
 * 
 * @author baoxing.gbx
 * @version $Id: InAlipayUnFollowExecutor.java, v 0.1 Jul 24, 2014 4:29:29 PM baoxing.gbx Exp $
 */
public class InAlipayUnFollowExecutor implements ActionExecutor {

	private IReportFormsService reportFormService = SpringContextUtil.getBean(IReportFormsService.class);
	
    /** 业务参数 */
    private JSONObject bizContent;

    public InAlipayUnFollowExecutor(JSONObject bizContent) {
        this.bizContent = bizContent;
    }

    public InAlipayUnFollowExecutor() {
        super();
    }

    @Override
    public String execute(AuthInfoVo authInfo) {

        //取得发起请求的支付宝账号id
        final String fromUserId = bizContent.getString("FromUserId");

      //关注数据统计
    	String dcApi = ApiModule.ReportForms.DataCollection.getName();;
		Document document = DocumentHelper.createDocument();
		Element req = document.addElement(KstHosConstant.REQ);
		XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, "7006");
		Element service = req.addElement(KstHosConstant.DATA);
		XMLUtil.addElement(service, "ChannelId", KstHosConstant.ZFB_CHANNEL_ID);
		XMLUtil.addElement(service, "ChannelName", KstHosConstant.ZFB_CHANNEL_NAME);
		XMLUtil.addElement(service, "Api", "");
		XMLUtil.addElement(service, "DataType", "1");
		XMLUtil.addElement(service, "DataValue", -1);
		XMLUtil.addElement(service, "Remark", null);
		
		try {
			InterfaceMessage msg = new InterfaceMessage();
			msg.setApiName(dcApi);
			msg.setParam(document.asXML());
			msg.setParamType(KstHosConstant.INTYPE);
			msg.setOutType(KstHosConstant.OUTTYPE);
			msg.setAuthInfo(authInfo.toString());
			msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
			msg.setVersion(KstHosConstant.V);
			if(null != reportFormService) {
				String result = reportFormService.DataCollection(msg);
				KasiteConfig.print("--result-->"+result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		//调用云数据收集接口
		dcApi = ApiModule.ReportForms.DataCloudCollection.getName();;
		
		Document document2 = DocumentHelper.createDocument();
		Element req2 = document2.addElement(KstHosConstant.REQ);
		XMLUtil.addElement(req2, KstHosConstant.TRANSACTIONCODE, "7006");
		Element service2 = req2.addElement(KstHosConstant.DATA);
		XMLUtil.addElement(service2, "ChannelId", KstHosConstant.ZFB_CHANNEL_ID);
		XMLUtil.addElement(service2, "DataType", 101);
		XMLUtil.addElement(service2, "DataCount", -1);
		XMLUtil.addElement(service2, "TargetType", "1");
		
		
		try {
			InterfaceMessage msg = new InterfaceMessage();
			msg.setApiName(dcApi);
			msg.setParam(document2.asXML());
			msg.setParamType(KstHosConstant.INTYPE);
			msg.setOutType(KstHosConstant.OUTTYPE);
			msg.setAuthInfo(authInfo.toString());
			msg.setSeq(DateOper.getNow("yyyyMMddHHmmssSSS"));
			msg.setVersion(KstHosConstant.V);
			if(null != reportFormService) {
				String result2 = reportFormService.DataCloudCollection(msg);
				KasiteConfig.print("--result2-->"+result2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//ConfigUtil.print("InAlipayFollowExecutor-DataCollection:"+resp.getResult().toString());
		
    	//ConfigUtil.print("InAlipayChatTextExecutor"+bizContent.toString());

        return AlipayMsgBuildUtil.buildBaseAckMsg(authInfo.getConfigKey(),fromUserId);
    }
}
