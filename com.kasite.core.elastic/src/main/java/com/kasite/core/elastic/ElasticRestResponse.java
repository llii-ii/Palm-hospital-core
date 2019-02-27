 package com.kasite.core.elastic;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

public class ElasticRestResponse {
	private String result;
	private ElasticRestClientBusSender sender;
	public void setParse(IElasticQueryResponseScrollParse parse) throws Exception {
		if(null != parse){
			ElasticQueryResponse response = toResponse();
			parseResult(response, parse,0);
			int totalCount = response.getHits().getTotal();
			parse.parseTotal(totalCount);
			int size = 10;
			Map<String, Object> _search = sender.get_search();
			if(null != _search){
				Object size_search = _search.get(ElasticQuery.SIZE);
				if(null != size_search){
					size = Integer.parseInt(size_search.toString());
				}
			}
			int pageCount = 0;
			if(size != 0) {
				pageCount = totalCount / size; 				
			}
			String _scroll_id = response.get_scroll_id();
			if(null != _scroll_id && _scroll_id.length() > 0){
				for (int i = 1; i <= pageCount; i++) {
					ElasticRestResponse resp = sender.scroll(_scroll_id,totalCount,i);
					parseResult(resp.toResponse(), parse, i);
				}
			}
		}
	}
	/**
	 * 返回成json
	 * @param parse
	 * @throws Exception
	 */
	public ElasticQueryResponseJsonVo setParseToJson() throws Exception {
		ElasticQueryResponseJsonVo vo = new ElasticQueryResponseJsonVo();
		setParse((new IElasticQueryResponseScrollParse() {
			private ElasticQueryResponseJsonVo vo;
			public IElasticQueryResponseScrollParse setVo(ElasticQueryResponseJsonVo vo) {
				this.vo = vo;
				return this;
			}
			@Override
			public void parse(ElasticQueryResponse response, ElasticQueryResponseHits_Hits hist, String _source, int index) {
				JSONObject json = JSONObject.parseObject(_source);
				if(null != vo) {
					vo.get_sources().add(json);
				}
			}
			@Override
			public void parseTotal(int total) {
				if(null != vo) {
					vo.setTotal(total);
				}
			}
		}).setVo(vo));
		
		
		return vo;
	}
	
	private void parseResult(ElasticQueryResponse response,IElasticQueryResponseScrollParse parse,int index){
		List<ElasticQueryResponseHits_Hits> list = response.getHits().getHits();
		if(null != list){
			for (ElasticQueryResponseHits_Hits hit : list) {
				String _source = hit.get_source();
				parse.parse(response,hit,_source,index);
			}
		}
	}
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public ElasticQueryResponse toResponse() throws Exception {
		JSONObject jsonObject = JSONObject.parseObject(result);
		ElasticQueryResponse resp = ElasticJSONUtil.toBean(jsonObject, ElasticQueryResponse.class);
		return resp;
	}
	public ElasticRestResponse(ElasticRestClientBusSender sender) {
		this.sender = sender;
	}
	
}
