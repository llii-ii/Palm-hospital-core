package test.com.kasite.client.crawler.xlsdictread;

import java.util.Map;

import com.kasite.client.crawler.config.data.DictBus1_5;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		Map<String, Data15PkVo> map = buser.getData15Map("HDSD00_85");
//		for (Map.Entry<String, Data15PkVo> entity : map.entrySet()) {
//			System.out.println( entity.getKey() + "\t" + entity.getValue().getPrivateDes() +"\t"+entity.getValue().getIsNotNum() +"\t"+ entity.getValue().getPrivateDictName());
//		}
//		String v = buser.get1_5Value("CV07.10.005", "1.1");
		
		
		Map<String, String> map = DictBus1_5.getInstall().getDictMap("GB/T 2659-2000");
		for (Map.Entry<String, String> entity : map.entrySet()) {
			System.out.println(entity.getKey() +"\t"+entity.getValue());
		}
		System.exit(-1);
	}

}
