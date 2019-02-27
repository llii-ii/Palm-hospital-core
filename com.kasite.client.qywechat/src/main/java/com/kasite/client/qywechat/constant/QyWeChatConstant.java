package com.kasite.client.qywechat.constant;

/**
 * 企微全局变量
 * 
 * @author 無
 *
 */
public class QyWeChatConstant {
	/**
	 * 返回码
	 */
	public static final String ERR_CODE = "errcode";
	/**
	 * 返回码
	 */
	public static final int SUCCESS_CODE = 0;
	/**
	 * token过期
	 */
	public static final int TOKEN_EXPIRED_CODE = 42001;
	/**
	 * token过期
	 */
	public static final String TOKEN_EXPIRED_MSG = "access_token expired";
	/**
	 * token无效
	 */
	public static final int TOKEN_INVALID_CODE = 40014;
	/**
	 * token无效
	 */
	public static final String TOKEN_INVALID_MSG = "invalid access_token";
	/**
	 * 素材media_id
	 */
	public static final String MEDIA_ID = "media_id";
	/**
	 * 素材url
	 */
	public static final String URL = "url";
	/**
	 * 群聊人数上限
	 */
	public static final int CHAT_INFO_SIZE = 500;
	/**
	 * 群聊不存在CODE
	 */
	public static final int CHAT_NO_EXIST_CODE = 86003;

	/********************************* 学分更新类型 **********************************/
	// 个人学分变更
	public static final String CREDITS_UPDATE_TYPE_ONEUPDATE = "oneUpdate";
	// 个人学分清零
	public static final String CREDITS_UPDATE_TYPE_ONECLEAR = "oneClear";
	// 全员学分清零
	public static final String CREDITS_UPDATE_TYPE_ALLCLEAR = "allClear";

	/*********************************
	 * 企微请求url
	 ******************************************************************/
	// 获取access_token的接口地址（GET） 限200（次/天）
	public static String ACCESS_TOKEN_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid={0}&corpsecret={1}";
	// 获取jsapi_ticket的接口地址（GET） 限200（次/天）
	public static String JSAPI_TICKET_URL = "https://qyapi.weixin.qq.com/cgi-bin/get_jsapi_ticket?access_token={0}";
	// 获取访问用户身份 非通讯录api
	public static String GETUSERID_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/getuserinfo?access_token={0}&code=CODE";
	// 发送消息的地址
	public static String SENDMESSAGE_URL = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token={0}";

	/*********************************
	 * 素材相关
	 *********************************************************************/
	// 上传临时素材url
	public static String UPLOADTEMPMATERIAL_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/upload?access_token={0}&type=TYPE";
	// 获取临时素材url
	public static String GETTEMPMATERIAL_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/get?access_token={0}&media_id=MEDIA_ID";
	// 上传永久图片url
	public static String UPLOADIMG_URL = "https://qyapi.weixin.qq.com/cgi-bin/media/uploadimg?access_token={0}";

	/*********************************
	 * 菜单相关
	 *********************************************************************/
	// 菜单创建（POST） 限100（次/天）
	public static String CREATE_MENU_URL = "https://qyapi.weixin.qq.com/cgi-bin/menu/create?access_token={0}&agentid=AGENTID";
	// 获取菜单
	public static String GET_MENU_URL = "https://qyapi.weixin.qq.com/cgi-bin/menu/get?access_token={0}&agentid=AGENTID";
	// 删除菜单
	public static String DELETE_MENU_URL = "https://qyapi.weixin.qq.com/cgi-bin/menu/delete?access_token={0}&agentid=AGENTID";

	/*********************************
	 * 群聊相关
	 *********************************************************************/
	// 创建群聊会话
	public static String CREATE_CHAT_URL = "https://qyapi.weixin.qq.com/cgi-bin/appchat/create?access_token={0}";
	// 修改群聊会话
	public static String UPDATE_CHAT_URL = "https://qyapi.weixin.qq.com/cgi-bin/appchat/update?access_token={0}";
	// 获取群聊会话
	public static String GET_CHAT_URL = "https://qyapi.weixin.qq.com/cgi-bin/appchat/get?access_token={0}&chatid=CHATID";
	// 应用推送消息
	public static String SEND_CHAT_URL = "https://qyapi.weixin.qq.com/cgi-bin/appchat/send?access_token={0}";

	/*********************************
	 * 用户相关-通讯录api
	 ********************************************************************/
	public static String CREATEUSER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/create?access_token={0}";
	public static String GETUSER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/get?access_token={0}&userid=USERID";
	public static String UPDATEUSER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/update?access_token={0}";
	public static String DELETEUSER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/delete?access_token={0}&userid=USERID";
	public static String BATCHDELETEUSER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/batchdelete?access_token={0}";
	public static String GETDEPARTMENTUSER_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/simplelist?access_token={0}&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD";
	public static String GETDEPARTMENTUSERDETAILS_URL = "https://qyapi.weixin.qq.com/cgi-bin/user/list?access_token={0}&department_id=DEPARTMENT_ID&fetch_child=FETCH_CHILD";

	/*********************************
	 * 部门相关-通讯录api
	 *******************************************************************/
	public static String CREATEDEPARTMENT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/create?access_token={0}";
	public static String UPDATEDEPARTMENT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/update?access_token={0}";
	public static String DELETEDEPARTMENT_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/delete?access_token={0}&id=ID";
	public static String GETDEPARTMENTLIST_URL = "https://qyapi.weixin.qq.com/cgi-bin/department/list?access_token={0}&id=ID";
}
