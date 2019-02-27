package test.com.kasite.client.crawler.coder;

import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class PingAnPrivate {
	
	public static void main(String[] args) {

		String jsonStr = "{\"credentialType\":\"\",\"birthday\":\"\",\"areaCode\":\"\",\"caseNum\":\"15XXXXXXX\",\"credentialNum\":\"\",\"gender\":\"\",\"treatEndDate\":\"2017XXXX\",\"phone\":\"\",\"areaName\":\"\",\"name\":\"\",\"hospNum\":\"\",\"treatBeginDate\":\"2017XXXX\"}";
		JSONObject json = JSON.parseObject(jsonStr);
		Set<String> keySet = json.keySet();
		StringBuffer sbf = new StringBuffer();
		for (String key : keySet) {
			sbf.append("public String get");
			String privateName = captureName(key);
			sbf.append(privateName).append("() {").append("\r\n\t\t").append("return json.getString(\"" + key + "\");")
					.append("\r\n}\r\n");
		}
		System.out.println(sbf.toString());
	}

	// 首字母大写
	public static String captureName(String name) {
		name = name.substring(0, 1).toUpperCase() + name.substring(1);
		return name;

	}
}
