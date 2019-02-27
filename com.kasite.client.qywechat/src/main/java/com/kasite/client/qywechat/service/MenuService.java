package com.kasite.client.qywechat.service;

/**
 * 菜单业务类
 * 
 * @author 無
 *
 */
public class MenuService {
	//private static Logger log = LoggerFactory.getLogger(MenuService.class);

	/**
	 * 1.创建菜单
	 * 
	 * @param menu        菜单实例
	 * @param accessToken 有效的access_token
	 * @return 0表示成功，其他值表示失败
	 */
//	public void createMenu(Menu menu, int agentId) {
//
//		// 1.获取json字符串：将Menu对象转换为json字符串
//		Gson gson = new Gson();
//		String jsonMenu = gson.toJson(menu); // 使用gson.toJson(user)即可将user对象顺序转成json
//		System.out.println("jsonMenu:" + jsonMenu);
//
//		// 2.获取请求的url
//		String create_menu_url = WeiXinUtil.create_menu_url.replace("AGENTID", String.valueOf(agentId));
//
//		// 3.调用接口,发送请求，创建菜单
//		JSONObject jsonObject = WeiXinUtil.httpRequest(create_menu_url, "POST", jsonMenu);
//
//		// 4.错误消息处理
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
//			} else {
//				System.out.println("创建菜单成功!");
//			}
//		}
//
//	}

	/**
	 * 获取菜单
	 * 
	 * @param accessToken
	 * @param menu
	 * @param agentId
	 */
//	public void getMenu(int agentId) {
//
//		// 2.获取请求的url
//		String get_menu_url = WeiXinUtil.get_menu_url.replace("AGENTID", String.valueOf(agentId));
//
//		// 3.调用接口,发送请求，创建菜单
//		JSONObject jsonObject = WeiXinUtil.httpRequest(get_menu_url, "GET", "");
//
//		// 4.错误消息处理
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
//			} else {
//				System.out.println("获取菜单成功!");
//			}
//		}
//
//	}

	/**
	 * 删除菜单
	 * 
	 * @param accessToken
	 * @param menu
	 * @param agentId
	 */
//	public void deleteMenu(int agentId) {
//
//		// 2.获取请求的url
//		String delete_menu_url = WeiXinUtil.delete_menu_url.replace("AGENTID", String.valueOf(agentId));
//
//		// 3.调用接口,发送请求，创建菜单
//		JSONObject jsonObject = WeiXinUtil.httpRequest(delete_menu_url, "GET", "");
//
//		// 4.错误消息处理
//		if (null != jsonObject) {
//			if (0 != jsonObject.getInt("errcode")) {
//				log.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
//			} else {
//				System.out.println("删除菜单成功!");
//			}
//		}
//
//	}

	/**
	 * 2.组装菜单数据 测试数据
	 * 
	 * @return
	 */
//	public Menu getMenu() {
//		ViewButton btn11 = new ViewButton();
//		btn11.setName("JSSDK测试");
//		btn11.setType("view");
//		btn11.setUrl("http://www.baidu.com");
//
//		CommonButton btn12 = new CommonButton();
//		btn12.setName("使用帮助");
//		btn12.setType("click");
//		btn12.setKey("12");
//
//		CommonButton btn13 = new CommonButton();
//		btn13.setName("翻译功能");
//		btn13.setType("click");
//		btn13.setKey("13");
//
//		ViewButton btn14 = new ViewButton();
//		btn14.setName("上传图片");
//		btn14.setType("view");
//		btn14.setUrl("http://www.baidu.com");
//
//		ViewButton btn15 = new ViewButton();
//		btn15.setName("上传图片2");
//		btn15.setType("view");
//		btn15.setUrl("http://www.baidu.com");
//
//		CommonButton btn21 = new CommonButton();
//		btn21.setName("歌曲点播");
//		btn21.setType("click");
//		btn21.setKey("21");
//
//		CommonButton btn22 = new CommonButton();
//		btn22.setName("经典游戏");
//		btn22.setType("click");
//		btn22.setKey("22");
//
//		CommonButton btn23 = new CommonButton();
//		btn23.setName("美女电台");
//		btn23.setType("click");
//		btn23.setKey("23");
//
//		CommonButton btn24 = new CommonButton();
//		btn24.setName("人脸识别");
//		btn24.setType("click");
//		btn24.setKey("24");
//
//		CommonButton btn25 = new CommonButton();
//		btn25.setName("聊天唠嗑");
//		btn25.setType("click");
//		btn25.setKey("25");
//
//		CommonButton btn31 = new CommonButton();
//		btn31.setName("Q友圈");
//		btn31.setType("click");
//		btn31.setKey("31");
//
//		CommonButton btn33 = new CommonButton();
//		btn33.setName("幽默笑话");
//		btn33.setType("click");
//		btn33.setKey("33");
//
//		CommonButton btn34 = new CommonButton();
//		btn34.setName("用户反馈");
//		btn34.setType("click");
//		btn34.setKey("34");
//
//		CommonButton btn35 = new CommonButton();
//		btn35.setName("关于我们");
//		btn35.setType("click");
//		btn35.setKey("35");
//
//		ViewButton btn32 = new ViewButton();
//		btn32.setName("周边搜索");
//		btn32.setType("view");
//		btn32.setUrl("http://www.baidu.com");
//
//		ComplexButton mainBtn1 = new ComplexButton();
//		mainBtn1.setName("生活助手");
//		mainBtn1.setSub_button(new Button[] { btn11, btn12, btn13, btn14, btn15 });
//
//		ComplexButton mainBtn2 = new ComplexButton();
//		mainBtn2.setName("休闲驿站");
//		mainBtn2.setSub_button(new Button[] { btn21, btn22, btn23, btn24, btn25 });
//
//		ComplexButton mainBtn3 = new ComplexButton();
//		mainBtn3.setName("更多");
//		mainBtn3.setSub_button(new Button[] { btn31, btn33, btn34, btn35, btn32 });
//
//		/**
//		 * 这是企业号目前的菜单结构，每个一级菜单都有二级菜单项<br>
//		 * 
//		 * 在某个一级菜单下没有二级菜单的情况，menu该如何定义呢？<br>
//		 * 比如，第三个一级菜单项不是“更多体验”，而直接是“幽默笑话”，那么menu应该这样定义：<br>
//		 * menu.setButton(new Button[] { mainBtn1, mainBtn2, btn33 });
//		 */
//		Menu menu = new Menu();
//		menu.setButton(new Button[] { mainBtn1, mainBtn2, mainBtn3 });
//
//		return menu;
//	}

}