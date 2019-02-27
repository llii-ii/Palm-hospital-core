package test.com.kasite.client.crawler.checkdata;

public class CheckDataXLS {

	public static void main(String[] args) {

		
//		DictBus1_5.getInstall();
		
//		Map<String, Map<String, Data15PkVo>> map = Data1_5Bus.getInstall().getDictMaps();
//		for (Map.Entry<String, Map<String, Data15PkVo>> entity : map.entrySet()) {
//			String key = entity.getKey();
//			Map<String, Data15PkVo> m = entity.getValue();
//			for (Map.Entry<String, Data15PkVo> me : m.entrySet()) {
//				String k = me.getKey();
//				Data15PkVo v = me.getValue();
//				if("HDSD00_14_014".equals(v.getPrivateName())) {
//					System.out.println("tabName="+key);
//					
//				}
//			}
//		}
		
		
		System.out.println("2018-03-01 12:12:21".length());
		
		
		String dateStr = "2018-03-01 12:12:212";//JsonUtil.getJsonString(obj, event_time.toLowerCase());
		
		if(dateStr.length() > 19) {
			dateStr = dateStr.substring(0, 19);
		}
		System.out.println(dateStr);
	}

}
