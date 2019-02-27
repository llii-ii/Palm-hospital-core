
package com.yihu.hos;

import com.kasite.core.common.constant.ApiModule;

/**
 * API模块枚举类 用于自定义API
 * 
 * @author 無
 * @version V1.0
 * @date 2018年4月26日 上午9:35:37
 */
public interface ApiCustomizeModule extends ApiModule {

	enum CustomizeApiModule implements ApiCustomizeModule {
		// TODO 添加自定义的Api 如：
		Test("basic.BasicApi.Test"),
		Test2("order.orderApi.Test2"),
		;

		private String name;

		CustomizeApiModule(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String getModuleName() {
			// TODO Auto-generated method stub
			return null;
		}
	}

}
