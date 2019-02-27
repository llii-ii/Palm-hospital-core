package test.com.kasite.core.elastic;


import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

public class TestElasticQuery {

	public static void main(String[] args) throws Exception {
//		Map<String, Object> _search = 
//				ElasticBus.create()
//				.setSize(1000)
//				.createSimpleQuery()
//				.addWildcard("_appname", "*123*")
//		.createElasticQuerySimpleBool()
////		.addMustRange(ElasticRangeEnum.gte, "inserttime", "2018-01-02 00:00:00")//>= 
////		.addMustRange(ElasticRangeEnum.lt, "inserttime", "2018-01-03 00:00:00")//<
////		.addMustNotTerm("modeid", "0") //  !=
//		.addMustTerm("_id", "_67612")// =
//		.addShouldTerm("123", "123")
//		.getSearch();
//		System.out.println(JSONObject.toJSON(_search).toString());
//		queryParam = "{\"query\": { \"match_all\": {}},\"size\":3}";
//		try{
//			String ips = "127.0.0.1:9200";
//			ElasticRestClientBusSender sender = ElasticRestClientBus.create(ips,ElasticRestTypeEnum.POST)
//			.set_search(_search);
//			
//			//yylog01 => 发送到指定索引    ：select * from table   where  _search
//			ElasticRestResponse resp = sender.setScroll(true).simpleQuery("clinicregisetrinfo");
//			//返回值的解析
//			resp.setParse(new IElasticQueryResponseScrollParse() {
//				@Override
//				public void parse(ElasticQueryResponse response,
//						ElasticQueryResponseHits_Hits hist, String _source,int index) {
//					
//					System.out.println(_source);
//					
//					
//				}
//			});
//			
////			Response response = ElasticRestClientBus.getInstall().simpleQuery("yylog01", queryParam);
////			String result = EntityUtils.toString(response.getEntity());
////			System.out.println(result);
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		System.exit(0);
//		String param = _search.toString();
//		
//		SoapResponseVo vo = HttpRequestBus.create("http://172.18.20.76:9200/yylog01/_search", RequestType.post)
//				.contentType("application/json;charset=utf-8")
//				.setParam(param)
//				.send();
//		System.out.println(vo.getResult());
//				
//		JSONObject jsonObject = JSONObject.fromObject(vo.getResult());
//		
//		ElasticQueryResponse response = ElasticJSONUtil.toBean(jsonObject, ElasticQueryResponse.class);
//		System.out.println(response.get_shards());
//		
//		if(null != jsonObject.get("hits")){
//			JSONObject hits = jsonObject.getJSONObject("hits");
//			Object _scroll_id = jsonObject.get("_scroll_id");
//			int total = hits.getInt("total");
//			List<JSONObject> result = new ArrayList<JSONObject>();
//			JSONArray resultlist = hits.getJSONArray("hits");
//			java.util.Iterator<JSONObject> iter = resultlist.iterator();
//			while(iter.hasNext()){
//				JSONObject histsObj = iter.next();
//				JSONObject r = histsObj.getJSONObject("_source");
//				System.out.println(r.get("orderid"));
//				result.add(r);
//			}
//			if(null != _scroll_id){
//				int size = total;
//				while(size > 0){
//					String url = "http://172.18.20.76:9200/_search/scroll?scroll=1m&scroll_id="+ _scroll_id.toString();
//					System.out.println(url);
//					vo = HttpRequestBus.create(url, RequestType.post)
//					.contentType("application/json;charset=utf-8")
//					.send();
//					jsonObject = JSONObject.fromObject(vo.getResult());
//					hits = jsonObject.getJSONObject("hits");
//					_scroll_id = jsonObject.get("_scroll_id");
//					resultlist = hits.getJSONArray("hits");
//					size = resultlist.size();
//					iter = resultlist.iterator();
//					while(iter.hasNext()){
//						JSONObject histsObj = iter.next();
//						JSONObject r = histsObj.getJSONObject("_source");
//						System.out.println(r.get("orderid"));
//						result.add(r);
//					}
//				}
//				//_search/scroll?scroll=1m&scroll_id= DnF1ZXJ5VGhlbkZldGNoAgAAAAAAAAbzFm1ubzV0VnJQU1ZDMVFzX3EwdXhJbkEAAAAAAAAG9BZtbm81dFZyUFNWQzFRc19xMHV4SW5B
//			}
//			
//			System.out.println(result.size());
//			
//			
//		}
//		
		
		
		SoapResponseVo vo = HttpRequestBus.create("http://47.106.71.162:18086/_settings", RequestType.put)
				.setParam("{ \"number_of_replicas\" : 0 }").send();
		System.out.println(vo.getResult());
	}
}
