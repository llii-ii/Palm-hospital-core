package com.kasite.client.qywechat.service;

import com.kasite.client.qywechat.constant.QyWeChatConstant;
import com.kasite.client.qywechat.util.WeiXinUtil;

import net.sf.json.JSONObject;

/**
 * 部门业务类
 * 
 * @author 無
 *
 */
public class DepartmentService {
	//private static Logger log = LoggerFactory.getLogger(DepartmentService.class);

//	// 1.创建部门
//	public void createDepartment(Department department) {
//
//		// 1.获取json字符串：将Department对象转换为json字符串
//		Gson gson = new Gson();
//		// 使用gson.toJson(jsonDepartment)即可将jsonDepartment对象顺序转成json
//		String jsonDepartment = gson.toJson(department);
//
//		// 3.调用接口，发送请求，创建部门
//		JSONObject jsonObject = WeiXinUtil.httpRequest(WeiXinUtil.createDepartment_url, "POST", jsonDepartment);
//		System.out.println("jsonObject:" + jsonObject.toString());
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				log.error("创建部门失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
//			} else {
//				System.out.println("成功！");
//			}
//		}
//	}

	// 2.更新部门
//	public void updateDepartment(Department department) {
//		// 1.获取json字符串：将Department对象转换为json字符串
//		Gson gson = new Gson();
//		// 使用gson.toJson(jsonDepartment)即可将jsonDepartment对象顺序转成json
//		String jsonDepartment = gson.toJson(department); 
//		// 3.调用接口，发送请求，更新部门
//		JSONObject jsonObject = WeiXinUtil.httpRequest(WeiXinUtil.updateDepartment_url, "POST", jsonDepartment);
//		System.out.println("jsonObject:" + jsonObject.toString());
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				log.error("更新部门失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
//			} else {
//				System.out.println("成功！");
//			}
//		}
//	}

	// 3.删除部门
//	public void deleteDepartment(String departmentId) {
//		// 1.获取请求的url
//		String deleteDepartment_url = WeiXinUtil.deleteDepartment_url.replace("ID", departmentId);
//		// 2.调用接口，发送请求，删除部门
//		JSONObject jsonObject = WeiXinUtil.httpRequest(deleteDepartment_url, "GET", null);
//		// 3.错误消息处理
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				log.error("删除部门失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
//			} else {
//				System.out.println("成功！");
//			}
//		}
//	}

	// 4.获取部门列表
	public static JSONObject getDepartmentList(String departmentId, String wxkey) throws Exception {
		// 1.获取请求的url
		String getDepartmentList_url = QyWeChatConstant.GETDEPARTMENTLIST_URL.replace("ID", departmentId);
		// 2.调用接口，发送请求，获取成员
		return WeiXinUtil.httpRequest(getDepartmentList_url, "GET", null, wxkey, true);
	}

}