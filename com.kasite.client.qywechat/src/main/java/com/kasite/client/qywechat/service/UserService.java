package com.kasite.client.qywechat.service;

import com.kasite.client.qywechat.constant.QyWeChatConstant;
import com.kasite.client.qywechat.util.WeiXinUtil;
import com.kasite.core.common.util.StringUtil;
import com.kasite.core.serviceinterface.module.qywechat.resp.RespUser;

import net.sf.json.JSONObject;

/**
 * 成员业务类
 * 
 * @author 無
 *
 */
public class UserService {
	//private static Logger log = LoggerFactory.getLogger(UserService.class);

	public static void main(String[] args) throws Exception {
		UserService.getDepartmentUser("", "1", "0");
	}

	/**
	 * 根据CODE获取成员UserID 非通讯录api
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getUserId(String code, String wxkey) throws Exception {
		String getUserId_url = QyWeChatConstant.GETUSERID_URL.replace("CODE", code);
		return WeiXinUtil.httpRequest(getUserId_url, "GET", null, wxkey, false);
	}

	// 1.创建成员
//	public void createUser(User user) {
//		Gson gson = new Gson();
//		String jsonU1 = gson.toJson(user);
//		System.out.println("jsonU1:" + jsonU1);
//		JSONObject jsonObject = WeiXinUtil.httpRequest(WeiXinUtil.createUser_url, "POST", jsonU1);
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				log.error("创建成员失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
//			} else {
//				System.out.println("创建成员成功:" + user.getName());
//			}
//		}
//	}

	// 2.获取成员
	public static RespUser getUser(String wxkey, String userId) throws Exception {
		// 1.获取请求的url
		String getUser_url = QyWeChatConstant.GETUSER_URL.replace("USERID", userId);
		// 2.调用接口，发送请求，获取成员
		JSONObject jsonObject = WeiXinUtil.httpRequest(getUser_url, "GET", null, wxkey, true);
		RespUser respUser = new RespUser();
		respUser.setUserId(jsonObject.getString("userid"));
		respUser.setName(jsonObject.getString("name"));
		respUser.setPosition(jsonObject.getString("position"));
		respUser.setMobile(jsonObject.getString("mobile"));
		respUser.setGender(jsonObject.getInt("gender"));
		respUser.setEmail(jsonObject.getString("email"));
		respUser.setAvatar(jsonObject.getString("avatar"));
		respUser.setAlias(jsonObject.getString("alias"));

		String department = jsonObject.getString("department");
		if (StringUtil.isBlank(department)) {
			respUser.setDepartment(null);
		} else {
			department = department.substring(1, department.length() - 1);
			String[] depts = department.split(",");
			respUser.setDepartment(depts);
		}

		return respUser;
	}

	// 3.更新成员
//	public void updateUser(User user) {
//		// 1.获取json字符串：将user对象转换为json字符串
//		Gson gson = new Gson();
//		// 使用gson.toJson(user)即可将user对象顺序转成json
//		String jsonU1 = gson.toJson(user);
//		System.out.println("jsonU1:" + jsonU1);
//		// 3.调用接口，发送请求，创建成员
//		JSONObject jsonObject = WeiXinUtil.httpRequest(WeiXinUtil.updateUser_url, "POST", jsonU1);
//		// 4.错误消息处理
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				log.error("更新成员失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
//			} else {
//				System.out.println("成功！");
//			}
//		}
//	}

	// 4.删除成员
//	public void deleteUser(String userId) {
//		// 1.获取请求的url
//		String deleteUser_url = WeiXinUtil.deleteUser_url.replace("USERID", userId);
//		// 2.调用接口，发送请求，删除成员
//		JSONObject jsonObject = WeiXinUtil.httpRequest(deleteUser_url, "GET", null);
//		// 3.错误消息处理
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				log.error("删除成员失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
//			} else {
//				System.out.println("成功！");
//			}
//		}
//	}

	// 5.批量删除成员
//	public void batchdeleteUser(List<String> userIdList) {
//		// 1.获取json字符串：将user对象转换为json字符串
//		Map<String, Object> content = new HashMap<String, Object>();
//		content.put("useridlist", userIdList);
//		Gson gson = new Gson();
//		String useridlist = gson.toJson(content);
//		System.out.println("useridlist=" + useridlist);
//		// 3.调用接口，发送请求，创建成员
//		JSONObject jsonObject = WeiXinUtil.httpRequest(WeiXinUtil.batchdeleteUser_url, "POST", useridlist);
//		// 4.错误消息处理
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				log.error("批量删除成员失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"),
//						jsonObject.getString("errmsg"));
//			} else {
//				System.out.println("成功！");
//			}
//		}
//	}

	// 6.获取部门成员
	/**
	 * 
	 * @param departmentId 获取的部门id
	 * @param fetchChild   1/0：是否递归获取子部门下面的成员
	 * @throws Exception
	 */
	public static JSONObject getDepartmentUser(String wxkey, String departmentId, String fetchChild) throws Exception {
		// 1.获取请求的url
		String getDepartmentUser_url = QyWeChatConstant.GETDEPARTMENTUSER_URL.replace("DEPARTMENT_ID", departmentId)
				.replace("FETCH_CHILD", fetchChild);
		// 2.调用接口，发送请求，获取部门成员
		return WeiXinUtil.httpRequest(getDepartmentUser_url, "GET", null, wxkey, true);
	}

	// 6.获取部门成员详情
	/**
	 * 
	 * @param departmentId 获取的部门id
	 * @param fetchChild   1/0：是否递归获取子部门下面的成员
	 * @throws Exception
	 */
	public static JSONObject getDepartmentUserDetails(String wxkey, String departmentId, String fetchChild)
			throws Exception {
		// 1.获取请求的url
		String getDepartmentUser_url = QyWeChatConstant.GETDEPARTMENTUSERDETAILS_URL
				.replace("DEPARTMENT_ID", departmentId).replace("FETCH_CHILD", fetchChild);
		// 2.调用接口，发送请求，获取部门成员
		return WeiXinUtil.httpRequest(getDepartmentUser_url, "GET", null, wxkey, true);
	}

//	// 7.获取部门成员详情
//	public static void getDepartmentUserDetails(String departmentId, String fetchChild) {
//		// 1.获取请求的url
//		String getDepartmentUserDetails_url = WeiXinUtil.getDepartmentUserDetails_url
//				.replace("DEPARTMENT_ID", departmentId).replace("FETCH_CHILD", fetchChild);
//		// 2.调用接口，发送请求，获取部门成员
//		JSONObject jsonObject = WeiXinUtil.httpRequest(getDepartmentUserDetails_url, "GET", null);
//		System.out.println("jsonObject:" + jsonObject.toString());
//		// 3.错误消息处理
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				log.error("获取部门成员详情失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"),
//						jsonObject.getString("errmsg"));
//			} else {
//				System.out.println("成功！");
//			}
//		}
//	}

}