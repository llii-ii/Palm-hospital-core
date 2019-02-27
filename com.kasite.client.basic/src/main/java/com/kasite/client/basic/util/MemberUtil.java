//<<<<<<< .mine
//package com.kasite.client.basic.util;
//
//
//import org.dom4j.Document;
//import org.dom4j.DocumentHelper;
//import org.dom4j.Element;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import com.kasite.core.common.constant.KstHosConstant;
//import com.kasite.core.common.util.XMLUtil;
//import com.kasite.core.serviceinterface.module.basic.IBasicService;
//import com.kasite.core.serviceinterface.module.basic.dbo.Member;
//import com.yihu.hos.exception.AbsHosException;
//import com.yihu.wsgw.api.InterfaceMessage;
//
///**
// * @author linjf TODO
// */
//@Component
//public class MemberUtil {
//	
//	@Autowired
//	private IBasicService basicService;
//
//	public Member queryMemberBean(String channelId, String cardType,String cardNo,String memberId,String hospitalNo) {
//		Document document = DocumentHelper.createDocument();
//		Element req = document.addElement(KstHosConstant.REQ);
//		XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, KstHosConstant.ORDERISCANCELCODE);
//		Element data = req.addElement(KstHosConstant.DATA);
//		XMLUtil.addElement(data, "ChannelId", channelId);
//		XMLUtil.addElement(data, "OpId", "");
//		XMLUtil.addElement(data, "CardType", cardType);
//		XMLUtil.addElement(data, "MemberId", memberId);
//		XMLUtil.addElement(data, "CardNo", cardNo);
//		XMLUtil.addElement(data, "HospitalNo", hospitalNo);
//		InterfaceMessage msg = new InterfaceMessage();
//		msg.setParam(document.asXML());
//		msg.setParamType(KstHosConstant.INTYPE);
//		msg.setOutType(KstHosConstant.OUTTYPE);
//		String resp = basicService.QueryMemberList(msg);
//		try {
//			Document docParse = XMLUtil.parseXml(resp);
//			Element el = docParse.getRootElement();
//			if (KstHosConstant.SUCCESSCODE.equals(XMLUtil.getString(el, KstHosConstant.RESPCODE, true))) {
//				Element data1 = el.element(KstHosConstant.DATA);
//				Member member = new Member();
//				member.setMemberName(XMLUtil.getString(data1, "MemberName", false));
//				member.setCardNo(XMLUtil.getString(data1, "CardNo", false));
//				member.setCardType(XMLUtil.getString(data1, "CardType", false));
//				member.setChannelId(XMLUtil.getString(data1, "MemberId", false));
//				member.setMobile(XMLUtil.getString(data1, "Mobile", false));
//				member.setIdCardNo(XMLUtil.getString(data1, "IdCardNo", false));
//				member.setSex(XMLUtil.getInt(data1, "Sex", false));
//				member.setBirthDate(XMLUtil.getString(data1, "BirthDate", false));
//				member.setAddress(XMLUtil.getString(data1, "Address", false));
//				return member;
//			}
//		} catch (AbsHosException e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//	
//||||||| .r4749
//=======
////package com.kasite.client.basic.util;
////
////
////import org.dom4j.Document;
////import org.dom4j.DocumentHelper;
////import org.dom4j.Element;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Component;
////
////import com.kasite.client.basic.bean.dbo.Member;
////import com.kasite.client.basic.service.IBasicService;
////import com.kasite.core.common.constant.KstHosConstant;
////import com.yihu.hos.exception.AbsHosException;
////import com.yihu.hos.util.XMLUtil;
////import com.yihu.wsgw.api.InterfaceMessage;
////
/////**
//// * @author linjf TODO
//// */
////@Component
////public class MemberUtil {
////	
////	
////	private static IBasicService basicService;
////	
////	@Autowired
////	public void setBasicService(IBasicService basicService) {
////		MemberUtil.basicService = basicService;
////	}
//>>>>>>> .r4784
////
////	public static Member queryMemberBean(String channelId, String cardType,String cardNo,String memberId,String hospitalNo) {
////		Document document = DocumentHelper.createDocument();
////		Element req = document.addElement(KstHosConstant.REQ);
////		XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, KstHosConstant.ORDERISCANCELCODE);
////		Element data = req.addElement(KstHosConstant.DATA);
////		XMLUtil.addElement(data, "ChannelId", channelId);
////		XMLUtil.addElement(data, "OpId", "");
////		XMLUtil.addElement(data, "CardType", cardType);
////		XMLUtil.addElement(data, "MemberId", memberId);
////		XMLUtil.addElement(data, "CardNo", cardNo);
////		XMLUtil.addElement(data, "HospitalNo", hospitalNo);
////		InterfaceMessage msg = new InterfaceMessage();
////		msg.setParam(document.asXML());
////		msg.setParamType(KstHosConstant.INTYPE);
////		msg.setOutType(KstHosConstant.OUTTYPE);
////		String resp = basicService.QueryMemberList(msg);
////		try {
////			Document docParse = XMLUtil.parseXml(resp);
////			Element el = docParse.getRootElement();
////			if (KstHosConstant.SUCCESSCODE.equals(XMLUtil.getString(el, KstHosConstant.RESPCODE, true))) {
////				Element data1 = el.element(KstHosConstant.DATA);
////				Member member = new Member();
////				member.setMemberName(XMLUtil.getString(data1, "MemberName", false));
////				member.setCardNo(XMLUtil.getString(data1, "CardNo", false));
////				member.setCardType(XMLUtil.getString(data1, "CardType", false));
////				member.setChannelId(XMLUtil.getString(data1, "MemberId", false));
////				member.setMobile(XMLUtil.getString(data1, "Mobile", false));
////				member.setIdCardNo(XMLUtil.getString(data1, "IdCardNo", false));
////				member.setSex(XMLUtil.getInt(data1, "Sex", false));
////				member.setBirthDate(XMLUtil.getString(data1, "BirthDate", false));
////				member.setAddress(XMLUtil.getString(data1, "Address", false));
////				return member;
////			}
////		} catch (AbsHosException e) {
////			e.printStackTrace();
////		}
////		return null;
////	}
////	
//////
//////	public static String queryMember(String channelId, String cardNo) {
//////		Document document = DocumentHelper.createDocument();
//////		Element req = document.addElement(KstHosConstant.REQ);
//////		XMLUtil.addElement(req, KstHosConstant.TRANSACTIONCODE, KstHosConstant.ORDERISCANCELCODE);
//////		Element data = req.addElement(KstHosConstant.DATA);
//////		XMLUtil.addElement(data, "ChannelId", channelId);
//////		XMLUtil.addElement(data, "OpId", "");
//////		XMLUtil.addElement(data, "CardType", "1");
//////		XMLUtil.addElement(data, "MemberId", "");
//////		XMLUtil.addElement(data, "CardNo", cardNo);
//////		String resp = "";
//////		resp = RpcUtil.callRpc(ApiModule.Basic.QueryMemberList.getName(), channelId, req.asXML(), 1, 1);
//////		String name = null;
//////		try {
//////			Document docParse = XMLUtil.parseXml(resp);
//////			Element el = docParse.getRootElement();
//////			if (Constant.SUCCESSCODE.equals(XMLUtil.getString(el, Constant.RESPCODE, true))) {
//////				Element dataEl = el.element(Constant.DATA);
//////				if( dataEl != null ) {
//////					name = XMLUtil.getString(dataEl, "MemberName", false);
//////				}
//////			}
//////		} catch (AbsHosException e) {
//////			e.printStackTrace();
//////		}
//////		return name;
//////	}
//////
//////	public static String queryHosNo(String channelId, String hosNo) {
//////		Document document = DocumentHelper.createDocument();
//////		Element req = document.addElement(Constant.REQ);
//////		XMLUtil.addElement(req, Constant.TRANSACTIONCODE, Constant.ORDERISCANCELCODE);
//////		Element data = req.addElement(Constant.DATA);
//////		XMLUtil.addElement(data, "ChannelId", channelId);
//////		XMLUtil.addElement(data, "OpId", "");
//////		XMLUtil.addElement(data, "CardType", "1");
//////		XMLUtil.addElement(data, "MemberId", "");
//////		XMLUtil.addElement(data, "HospitalNo", hosNo);
//////		String resp = "";
//////		resp = RpcUtil.callRpc(ApiModule.Basic.QueryMemberList.getName(), channelId, req.asXML(), 1, 1);
//////		try {
//////			Document docParse = XMLUtil.parseXml(resp);
//////			Element el = docParse.getRootElement();
//////			if (Constant.SUCCESSCODE.equals(XMLUtil.getString(el, Constant.RESPCODE, true))) {
//////				Element data1 = el.element(Constant.DATA);
//////				String name = XMLUtil.getString(data1, "MemberName", false);
//////				return name;
//////			}
//////		} catch (AbsHosException e) {
//////			// TODO Auto-generated catch block
//////			e.printStackTrace();
//////		}
//////
//////		return null;
//////	}
////
////	
////	
////}
