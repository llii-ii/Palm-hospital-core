package com.kasite.core.common.config;
/*
 * // 测试企业ID
 public final static String corpId = "wwda4e2c1313550d93";
 // 应用秘钥
 public final static String agentSecret = "AKTAj1ClLLjIZdY9-H7CO1zzLCOkg-Ynpl-IPPS90jk";
 // 通讯录秘钥
 public final static String contactsSecret = "zzTujtKFQ4nytlstGVndPiB7rO9dFRLE-G-NUqK0lAU";
 // 应用ID
 public final static int agentId = 1000002;
 */
public enum QyWeChatConfig {
	/**企业ID*/
	corpid,
	/**应用秘钥*/
	agentsecret,
	/**通讯录秘钥*/
	contactssecret,
	/**应用ID*/
	agentid,
	/**ConfigKey*/
	configkey,
	/**eventoken  接收事件服务器*/
	eventoken,
	/**接收事件服务器 EncodingAESKey*/
	encodingaeskey
}
