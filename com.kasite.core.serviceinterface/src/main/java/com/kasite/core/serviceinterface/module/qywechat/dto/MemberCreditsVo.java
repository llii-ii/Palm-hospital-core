package com.kasite.core.serviceinterface.module.qywechat.dto;

import com.kasite.core.common.resp.AbsResp;

/**
 * 成员积分vo
 * 
 * @author 無
 *
 */
public class MemberCreditsVo extends AbsResp {

	private String memberid;

	private String membername;

	private Integer allCredits;

	public String getMemberid() {
		return memberid;
	}

	public void setMemberid(String memberid) {
		this.memberid = memberid;
	}

	public String getMembername() {
		return membername;
	}

	public void setMembername(String membername) {
		this.membername = membername;
	}

	public Integer getAllCredits() {
		return allCredits;
	}

	public void setAllCredits(Integer allCredits) {
		this.allCredits = allCredits;
	}

}
