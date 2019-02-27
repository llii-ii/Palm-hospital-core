 	//form序列化成对象
	 $.fn.serializeObject = function() {
      	var o = {};
      	var a = this.serializeArray();
      	$.each(a, function() {
          if (o[this.name] !== undefined) {
              if (!o[this.name].push) {
                  o[this.name] = [o[this.name]];
              }
              o[this.name].push(this.value || '');
          } else {
              o[this.name] = this.value || '';
          }
      	});
     	return o;
    };
    //封装JSON对象返回Json字符串
	function makeParamForm(hosId,api,TransactionCode,formObj){
		var obj = {};
   		var params = {};
   		var req = {};
   		obj.Api = api;
   		var formResultObj = formObj.serializeObject();
   		formResultObj.HosId = hosId;
   		req.TransactionCode = TransactionCode;
   		req.Service = formResultObj;
   		params.Req = req;
   		obj.Params=params;
   		return $.toJSON(obj);
	}
	function makeParam(hosId,api,TransactionCode,formObj){
		var obj = {};
   		var params = {};
   		var req = {};
   		obj.Api = api;
   		var formResultObj = formObj.serializeObject();
   		formResultObj.HosId = hosId;
   		req.TransactionCode = TransactionCode;
   		req.Service = formResultObj;
   		params.Req = req;
   		obj.Params=params;
   		return $.toJSON(obj);
	}
	//通过aja传送JSon
	function ajaxJson(url,param){
		var result;
		$.ajax({
			type: 'POST',
			url: url+'?v='+(new Date().getTime()),
			data: param,
			async: false,
			timeout : 6000,
			contentType: "application/json", //必须有
			cache : false,
			dataType: 'json',
			success: function(data){
				result = data;
 			}
		});
		return result;
	 };