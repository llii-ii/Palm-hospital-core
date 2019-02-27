package test.com.kasite.core.httpclient;

import com.kasite.core.httpclient.http.HttpRequest;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

public class TestSoap {
	public static void main(String[] args) throws Exception {
		testSoap1();
		System.exit(-1);
	}
	
	public static void testSoap1() throws Exception{
		/*String url = "http://61.146.237.26:5555/Kingdee.SelfService.Service.MmryService.svc";
		String soapxml = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tem=\"http://tempuri.org/\"><soapenvaddsoapheader><SOAPAction><![CDATA[http://tempuri.org/IMmryService/QueryDoctor]]></SOAPAction></soapenvaddsoapheader><soapenv:Header/><soapenv:Body>  <tem:QueryDoctor>      <tem:xmlParam><![CDATA[<Request><TradeCode>1013</TradeCode><ExtOrgCode>1</ExtOrgCode><HospitalId>01</HospitalId><ClientType>1</ClientType><ExtUserID>000</ExtUserID><DepartmentCode>1006</DepartmentCode></Request>]]></tem:xmlParam>   </tem:QueryDoctor></soapenv:Body></soapenv:Envelope>";
		SoapResponseVo vo = HttpRequestBus.create(url, RequestType.soap1).setParam(soapxml).send();
		System.out.println(vo.getCode());
		System.out.println(vo.getResult());
		System.out.println("处理Soap请求结果：");
		System.out.println(HttpRequest.formateSoapResp(vo.getResult()));*/
	}
}
