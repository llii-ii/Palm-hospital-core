package com.kasite.client.basic.thread;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.constant.KstHosConstant;
import com.kasite.core.common.req.CommonReq;
import com.kasite.core.common.util.LogUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.basic.dbo.Member;
import com.kasite.core.serviceinterface.module.handler.HandlerBuilder;
import com.kasite.core.serviceinterface.module.his.handler.IMemberAutoUnbindService;
import com.kasite.core.serviceinterface.module.his.req.ReqHisMemberAutoUnbind;
import com.yihu.wsgw.api.InterfaceMessage;

/**
 * 成员自动解绑异步线程
 * @author lcz
 *
 */
public class MemberAutoUnbindThread extends Thread{

	private final static Log log = LogFactory.getLog(KstHosConstant.LOG4J_BASIC);
	
	private InterfaceMessage msg;
	private List<Member> memberList;
	
	public MemberAutoUnbindThread(InterfaceMessage msg,List<Member> memberList){
		this.msg = msg;
		this.memberList = memberList;
	}
	
	@Override
	public void run() {
		try {
			//查询某个用户的成员信息时，进行自动较验，不通过的自动解绑
			IMemberAutoUnbindService autoUnbind = HandlerBuilder.get().getCallHisService(KasiteConfig.getAuthInfo(msg), IMemberAutoUnbindService.class);
			if(autoUnbind!=null) {
				//自动较验成员卡信息，不存在的自动解绑
				for (Member mm : memberList) {
					if(StringUtil.isNotBlank(mm.getCardNo())) {
						Map<String, String> map = new ConcurrentHashMap<String, String>();
						map.put("memberName", mm.getMemberName());
						map.put("cardNo", mm.getCardNo());
						map.put("cardType", mm.getCardType());
						map.put("hisMemberId", mm.getHisMemberId());
						CommonReq<ReqHisMemberAutoUnbind> unbindReq = new CommonReq<ReqHisMemberAutoUnbind>(new ReqHisMemberAutoUnbind(msg,mm.getMemberId(),mm.getCardType(),mm.getCardNo(),map));
						autoUnbind.memberAutoUnbind(unbindReq);
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			LogUtil.error(log, e);
		}
		
	}
	
	
	
}
