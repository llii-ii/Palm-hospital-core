package com.kasite.core.common.util.wechat.constants;

import java.util.HashMap;
import java.util.Map;

import com.kasite.core.common.util.wechat.Ticket;
import com.kasite.core.common.util.wechat.Token;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;


/**
 * 微信 端常量定义
 * 主要包含所有与微信端交互部分端URL地址
 * 在系统初始化启动的时候如果 参数中有配置代理的URL 默认会将URL通过
 * 配置的url进行初始化
 * @author MECHREV
 */
public class WeiXinConstant {
	/**获取微信用户信息*/
	public static String USER_URL = "https://api.weixin.qq.com/cgi-bin/user/info?lang=zh_CN&access_token={0}";
	/**获取微信token*/
	public static String token_url ="https://api.weixin.qq.com/cgi-bin/token";
	/**新增微信公众号菜单*/
	public static String ADDMENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token={0}";
	/**查询公众号当前菜单*/
	public static String GETMENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token={0}";
	/**删除公众号菜单*/
	public static String DELMENU_URL = "https://api.weixin.qq.com/cgi-bin/menu/delete?access_token={0}";
	/**通过开放平台 进行 oauth2 鉴权*/
	public static String CODE_URL = "https://open.weixin.qq.com/connect/oauth2/authorize"; 
	/**获取oauth2 鉴权 access_token*/
	public static String OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token"; 
	/**oauth2 access_token 刷新时间*/
	public static String OAUTH2_REFRESH_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token"; 
	/**oauth2 鉴权后通过 access_token 获取微信用户信息*/
	public static String OAUTH2_ACCESS_TOKEN_USER_URL = "https://api.weixin.qq.com/sns/userinfo?access_token={0}"; 
	/**创建二维码*/
	public static String CREATE_QRCODE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token={0}";
	/**获取用户组*/
	public static String GET_GROUP_URL = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token={0}";
	/**获取用户列表*/
	public static String GET_USERLIST_URL = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={0}";
	/**批量获取用户*/
	public static String BATCH_GETUSERINFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token={0}";
	/**推送消息*/
	public static String SEND_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token={0}";
	/**模板消息*/
	public static String SEND_TEMPLATE_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={0}"; 
	/**获取模板消息列表*/
	public static String GET_ALL_PRIVATE_TEMPLATE = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token={0}";  
	/**客服接口-发消息*/
	public static String SEND_CUSTOM_MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token={0}";  
	/**网页授权时拉取用户信息*/
	public static String GET_USERINFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token={0}";  
	/**获得jsapi_ticket*/
	public static String GETTICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}";  
	/**获取临时素材*/
	public static String GETMEDIA_URL = "https://api.weixin.qq.com/cgi-bin/media/get?access_token={0}";  
	/** 调用微信生成短链接 */
    public static String SHORTURL = "https://api.weixin.qq.com/cgi-bin/shorturl?access_token={0}";
	/** 国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语*/ 
    public static String LANG = "zh_CN";
	/**
	 * 微信对账单下载地址
	 */
	public static String DOWNLOADBILL = "https://api.mch.weixin.qq.com/pay/downloadbill";
	/**
	 * 从微信生成二维码
	 */
	public static String 	SHOWQRCODE = "https://mp.weixin.qq.com/cgi-bin/showqrcode";
	/**
	 * 
	 */
	public static String SESSIONURL= "session_url";
	/**
	 * 查询订单
	 */
	public static String ORDER_URL = "https://api.mch.weixin.qq.com/pay/orderquery";
	/**
	 * 关闭订单
	 */
	public static String ORDER_CLOSE_URL = "https://api.mch.weixin.qq.com/pay/closeorder";
	/**
	 * 退款
	 */
	public static String REFUND_URL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	/**
	 * 查询退款
	 */
	public static String REFUNDQUERY_URL = "https://api.mch.weixin.qq.com/pay/refundquery";
	/**
	 * 统一支付接口
	 */
	public static String PAY_UNIFIEDORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	
	
	public static Map<String,Token> tokens = new HashMap<String,Token>();
	public static Map<String,Ticket> tickets = new HashMap<String,Ticket>();
	public static String CHECK_SIGNA_FALSE_MSG = "微信接入验证失败";
	public static Map<Integer,String> appids = new HashMap<Integer,String>();
	public static final String SCOPE_BASE = "snsapi_base"; 
	public static final String SCOPE_USERINFO = "snsapi_userinfo"; 
	/**
	 * 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	 */
	public static final String DEVICE_INFO = "WEB";
	
	public static final String CHARSET = "UTF-8";

	/**货币默认类型 人民币*/
	public static final String FEE_TYPE = "CNY";
	
	public static final String PAY_BODY = "挂号支付";
	
	public static final String TRADE_TYPE_JSAPI = "JSAPI";
	public static final String TRADE_TYPE_NATIVE = "NATIVE";
	public static final String TRADE_TYPE_APP = "APP";
	public static final String TRADE_TYPE_WAP = "WAP";
	
	public static final String SIGNTYPE_MD5 = "MD5";
	
	public static final String FAIL = "FAIL"; 
	
	public static final String SUCCESS = "SUCCESS"; 
	/** 
     * 返回消息类型：文本 
     */  
    public static final String RESP_MESSAGE_TYPE_TEXT = "text";  
  
    /** 
     * 返回消息类型：音乐 
     */  
    public static final String RESP_MESSAGE_TYPE_MUSIC = "music";  
  
    /** 
     * 返回消息类型：图文 
     */  
    public static final String RESP_MESSAGE_TYPE_NEWS = "news";  
  
    /** 
     * 请求消息类型：文本 
     */  
    public static final String REQ_MESSAGE_TYPE_TEXT = "text";  
  
    /** 
     * 请求消息类型：图片 
     */  
    public static final String REQ_MESSAGE_TYPE_IMAGE = "image";  
  
    /** 
     * 请求消息类型：链接 
     */  
    public static final String REQ_MESSAGE_TYPE_LINK = "link";  
  
    /** 
     * 请求消息类型：地理位置 
     */  
    public static final String REQ_MESSAGE_TYPE_LOCATION = "location";  
  
    /** 
     * 请求消息类型：音频 
     */  
    public static final String REQ_MESSAGE_TYPE_VOICE = "voice";  
  
    /** 
     * 请求消息类型：推送 
     */  
    public static final String REQ_MESSAGE_TYPE_EVENT = "event";  
  
    /** 
     * 事件类型：subscribe(订阅) 
     */  
    public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";  
  
    /** 
     * 事件类型：unsubscribe(取消订阅) 
     */  
    public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";  
  
    /** 
     * 事件类型：CLICK(自定义菜单点击事件) 
     */  
    public static final String EVENT_TYPE_CLICK = "CLICK";  
	
    /** 
     * 事件类型：VIEW(自定义菜单点击事件) 
     */  
    public static final String EVENT_TYPE_VIEW = "VIEW";  
    /**
     * 事件推送群发结果
     */
    public static final String MASSS_END_JOB_FINISH = "MASSSENDJOBFINISH";  
    /** 
     * 事件类型：扫描带参数二维码事件(已经关注公众号) 
     */ 
    public static final String EVENT_SCAN = "SCAN";  
    
    /** 
     * OPENID
     */ 
    public static final String OPENID = "openid";  
    
	
	public static void errorsCallback(JSONObject json,int code) throws JSONException{
		switch (code) {
		case -9:{
			json.put("errmsg", "系统错误请联系管理员.");
			break;
		}
		case 0:{
			json.put("errmsg", "操作成功");
			break;
		}
		case -1:{
			json.put("errmsg", "系统繁忙");
			break;
		}
		case    40001:{ json.put("errmsg","获取access_token时AppSecret错误，或者access_token无效"); break; }
		case    40002:{ json.put("errmsg","不合法的凭证类型"); break; }
		case    40003:{ json.put("errmsg","不合法的OpenID"); break; }
		case    40004:{ json.put("errmsg","不合法的媒体文件类型"); break; } 
		case    40005:{ json.put("errmsg","不合法的文件类型"); break; } 
		case    40006:{ json.put("errmsg","不合法的文件大小"); break; } 
		case    40007:{ json.put("errmsg","不合法的媒体文件id"); break; } 
		case    40008:{ json.put("errmsg","不合法的消息类型"); break; }
		case    40009:{ json.put("errmsg","不合法的图片文件大小"); break; }
		case    40010:{ json.put("errmsg","不合法的语音文件大小"); break; }
		case    40011:{ json.put("errmsg","不合法的视频文件大小"); break; } 
		case    40012:{ json.put("errmsg","不合法的缩略图文件大小"); break; } 
		case    40013:{ json.put("errmsg","不合法的APPID"); break; } 
		case    40014:{ json.put("errmsg","不合法的access_token"); break; } 
		case    40015:{ json.put("errmsg","不合法的菜单类型"); break; }
		case    40016:{ json.put("errmsg","不合法的按钮个数"); break; } 
		case    40017:{ json.put("errmsg","不合法的按钮个数"); break; }
		case    40018:{ json.put("errmsg","不合法的按钮名字长度"); break; }
		case    40019:{ json.put("errmsg","不合法的按钮KEY长度"); break; } 
		case    40020:{ json.put("errmsg","不合法的按钮URL长度"); break; } 
		case    40021:{ json.put("errmsg","不合法的菜单版本号"); break; } 
		case    40022:{ json.put("errmsg","不合法的子菜单级数"); break; } 
		case    40023:{ json.put("errmsg","不合法的子菜单按钮个数"); break; } 
		case    40024:{ json.put("errmsg","不合法的子菜单按钮类型"); break; }
		case    40025:{ json.put("errmsg","不合法的子菜单按钮名字长度"); break; } 
		case    40026:{ json.put("errmsg","不合法的子菜单按钮KEY长度"); break; }
		case    40027:{ json.put("errmsg","不合法的子菜单按钮URL长度"); break; }
		case    40028:{ json.put("errmsg","不合法的自定义菜单使用用户"); break; }
		case    40029:{ json.put("errmsg","不合法的oauth_code"); break; }
		case    40030:{ json.put("errmsg","不合法的refresh_token"); break; }
		case    40031:{ json.put("errmsg","不合法的openid列表"); break; }
		case    40032:{ json.put("errmsg","不合法的openid列表长度"); break; }
		case    40033:{ json.put("errmsg","不合法的请求字符，不能包含\\uxxxx格式的字符"); break; }
		case    40035:{ json.put("errmsg","不合法的参数"); break; }
		case    40038:{ json.put("errmsg","不合法的请求格式"); break; }
		case    40039:{ json.put("errmsg","不合法的URL长度"); break; }
		case    40050:{ json.put("errmsg","不合法的分组id"); break; } 
		case    40051:{ json.put("errmsg","分组名字不合法"); break; } 
		case    41001:{ json.put("errmsg","缺少access_token参数"); break; }
		case    41002:{ json.put("errmsg","缺少appid参数"); break; }
		case    41003:{ json.put("errmsg","缺少refresh_token参数"); break; }
		case    41004:{ json.put("errmsg","缺少secret参数"); break; } 
		case    41005:{ json.put("errmsg","缺少多媒体文件数据"); break; }
		case    41006:{ json.put("errmsg","缺少media_id参数"); break; } 
		case    41007:{ json.put("errmsg","缺少子菜单数据"); break; }
		case    41008:{ json.put("errmsg","缺少oauth code"); break; }
		case    41009:{ json.put("errmsg","缺少openid"); break; }
		case    42001:{ json.put("errmsg","access_token超时"); break; }
		case    42002:{ json.put("errmsg","refresh_token超时"); break; }
		case    42003:{ json.put("errmsg","oauth_code超时"); break; }
		case    43001:{ json.put("errmsg","需要GET请求"); break; }
		case    43002:{ json.put("errmsg","需要POST请求"); break; } 
		case    43003:{ json.put("errmsg","需要HTTPS请求"); break; }
		case    43004:{ json.put("errmsg","需要接收者关注"); break; }
		case    43005:{ json.put("errmsg","需要好友关系"); break; }
		case    44001:{ json.put("errmsg","多媒体文件为空"); break; }
		case    44002:{ json.put("errmsg","POST的数据包为空"); break; }
		case    44003:{ json.put("errmsg","图文消息内容为空"); break; }
		case    44004:{ json.put("errmsg","文本消息内容为空"); break; }
		case    45001:{ json.put("errmsg","多媒体文件大小超过限制"); break; }
		case    45002:{ json.put("errmsg","消息内容超过限制"); break; }
		case    45003:{ json.put("errmsg","标题字段超过限制"); break; }
		case    45004:{ json.put("errmsg","描述字段超过限制"); break; }
		case    45005:{ json.put("errmsg","链接字段超过限制"); break; }
		case    45006:{ json.put("errmsg","图片链接字段超过限制"); break; }
		case    45007:{ json.put("errmsg","语音播放时间超过限制"); break; }
		case    45008:{ json.put("errmsg","图文消息超过限制"); break; }
		case    45009:{ json.put("errmsg","接口调用超过限制"); break; }
		case    45010:{ json.put("errmsg","创建菜单个数超过限制"); break; }
		case    45015:{ json.put("errmsg","回复时间超过限制"); break; }
		case    45016:{ json.put("errmsg","系统分组，不允许修改"); break; }
		case    45017:{ json.put("errmsg","分组名字过长"); break; } 
		case    45018:{ json.put("errmsg","分组数量超过上限"); break; }
		case    46001:{ json.put("errmsg","不存在媒体数据"); break; }
		case    46002:{ json.put("errmsg","不存在的菜单版本"); break; }
		case    46003:{ json.put("errmsg","不存在的菜单数据"); break; }
		case    46004:{ json.put("errmsg","不存在的用户"); break; }
		case    47001:{ json.put("errmsg","解析JSON/XML内容错误"); break; }
		case    48001:{ json.put("errmsg","api功能未授权"); break; } 
		case    50001:{ json.put("errmsg","用户未授权该api"); break; }
		default:
			break;
		}
	}
//	/**公众号appid*/
//	public static String APPID = ""; 
//	/**公众号密钥*/
//	public static String SECRET = ""; 
//	/**微信token*/
//	public static String TOKEN = "";
//
//	static{
//    	try{
//    		TOKEN = KasiteConfig.getTenPayToken();
//    		APPID = KasiteConfig.getTenPayAppId();
//    		SECRET = KasiteConfig.getTenPaySecret();
//    	}catch (Exception e) {
//			e.printStackTrace();
//		}
//    }
	/**
	 * 获取token的模式
	 */
	public class WxTokenMode {
		public final static int MODE_1 = 1;//代理模式
		public final static int MODE_0 = 0;//非代理模式
	}
}
