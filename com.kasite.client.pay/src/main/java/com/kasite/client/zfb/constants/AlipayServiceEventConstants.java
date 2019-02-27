/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package com.kasite.client.zfb.constants;

/**
 * 支付宝服务窗事件常量
 * 
 * @author taixu.zqq
 * @version $Id: AlipayServiceEvent.java, v 0.1 2014年7月24日 下午1:40:04 taixu.zqq Exp $
 */
public class AlipayServiceEventConstants {

    /**网关验证事件*/
    public static final String VERIFYGW_EVENT = "verifygw";

    /**服务窗关注事件*/
    public static final String FOLLOW_EVENT   = "follow";

    /**服务窗取消关注事件*/
    public static final String UNFOLLOW_EVENT = "unfollow";

    /**服务窗菜单点击事件*/
    public static final String CLICK_EVENT    = "click";

    /**服务窗进入事件*/
    public static final String ENTER_EVENT = "enter";
    
    public static final String CODE_TYPE = "PERM"; // 二维码类型，目前只支持两种类型： TEMP：临时的（默认）； PERM：永久的
    public static final String SHOW_LOGO =  "N"; //二维码中间是否显示服务窗logo，Y：显示；N：不显示（默认）
}
