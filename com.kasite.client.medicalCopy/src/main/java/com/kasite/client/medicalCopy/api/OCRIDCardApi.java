package com.kasite.client.medicalCopy.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kasite.core.common.config.OcrIdCardConfig;
import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 身份证认证api
 * 
 * @author cjy
 * @version V1.0
 * @date 2018年10月8日 
 */
@Controller
@RequestMapping(value = "/IDCard")
public class OCRIDCardApi {
	
	@Autowired
	OcrIdCardConfig ocridCardConfig;
	
	//private static String api_key = "uWC4iGnReEFir5THOOaxPIRJUiRLgRe0";  //api_key
	//private static String api_secret = "RUv-hSLPaXnHl71_as0UwK447c4eumeG";  //api_secret
	
	/**
	 * 身份证认证
	 * @param api_key 
	 * @param api_secret 
	 * @param image_file
	 * */
	@RequestMapping(value = "/OCRIDCardAPI.do")
	@ResponseBody
	public String ocrIDCardAPI(File file) {
		//String repUrl = "https://api-cn.faceplusplus.com/cardpp/v1/ocridcard";
		String repUrl = ocridCardConfig.getAuth().getRepUrl();
		SoapResponseVo result = HttpRequestBus.create(repUrl, RequestType.fileUploadPost)
				.addHttpParam("api_key", ocridCardConfig.getAuth().getApiKey())
				.addHttpParam("api_secret", ocridCardConfig.getAuth().getApiSecret())
				.addHttpParam("legality", "1")
				.setFile("image_file", file)
				.send();
		
		return decodeUnicode(result.getResult().toString());
	}
	
	/**
	 * 模板识别
	 * @param api_key 
	 * @param api_secret 
	 * @param image_file
	 * */
	@RequestMapping(value = "/ocrTemplateAPI.do")
	@ResponseBody
	public String ocrTemplateAPI(File file) {
		//String repUrl = "https://api-cn.faceplusplus.com/cardpp/v1/ocridcard";
		String repUrl = ocridCardConfig.getAuth().getTemUrl();
		SoapResponseVo result = HttpRequestBus.create(repUrl, RequestType.fileUploadPost)
				.addHttpParam("api_key", ocridCardConfig.getAuth().getApiKey())
				.addHttpParam("api_secret", ocridCardConfig.getAuth().getApiSecret())
				.addHttpParam("template_id", ocridCardConfig.getAuth().getTemId())
				.setFile("image_file", file)
				.send();
		
		return decodeUnicode(result.getResult().toString());
	}
	
	/**
	 * unicode 解码
	 * @param String str
	 * @return String
	 */
	 public static String decodeUnicode(String str) {
		  Charset set = Charset.forName("UTF-16");
		  Pattern p = Pattern.compile("\\\\u([0-9a-fA-F]{4})");
		  Matcher m = p.matcher( str );
		  int start = 0 ;
		  int start2 = 0 ;
		  StringBuffer sb = new StringBuffer();
		  while( m.find( start ) ) {
		   start2 = m.start() ;
		   if( start2 > start ){
		    String seg = str.substring(start, start2) ;
		    sb.append( seg );
		   }
		   String code = m.group( 1 );
		   int i = Integer.valueOf( code , 16 );
		   byte[] bb = new byte[ 4 ] ;
		   bb[ 0 ] = (byte) ((i >> 8) & 0xFF );
		   bb[ 1 ] = (byte) ( i & 0xFF ) ;
		   ByteBuffer b = ByteBuffer.wrap(bb);
		   sb.append( String.valueOf( set.decode(b) ).trim() );
		   start = m.end() ;
		  }
		  start2 = str.length() ;
		  if( start2 > start ){
		   String seg = str.substring(start, start2) ;
		   sb.append( seg );
		  }
		  return sb.toString() ;
	}
	
	 public static void main(String[] args) {

		 String api_key = "ycrAkiZyI8RFtNAyp8_tZ6xmdr4WmKBy";  //api_key
		 String api_secret = "kzDv4NGAABdzQE-SH_Ssh2MuaGKsFVca";  //api_secret
		 String repUrl = "https://api-cn.faceplusplus.com/cardpp/v1/ocridcard";
		 File file = new File(System.getProperty("user.dir")+"\\234234.png");
		 SoapResponseVo result = HttpRequestBus.create(repUrl, RequestType.fileUploadPost)
				 .addHttpParam("api_key", api_key)
				 .addHttpParam("api_secret", api_secret)
				 .addHttpParam("legality", "1")
				 .setFile("image_file", file)
				 .send();
		 System.out.println(decodeUnicode(result.getResult().toString()));
//		JSONObject json = JSONObject.fromObject(decodeUnicode(result.getResult().toString()));
//		JSONArray jsonArray = JSONArray.fromObject(json.getString("cards"));
//		JSONObject ob = (JSONObject) jsonArray.get(0);
//		JSONObject legality = JSONObject.fromObject(ob.getString("legality"));
//		System.out.println("用工具合成或者编辑过的身份证图片Edited "+legality.getString("Edited"));
//		System.out.println("正式身份证的复印件Photocopy "+legality.getString("Photocopy"));
//		System.out.println("正式身份证照片ID Photo "+legality.getString("ID Photo"));
//		System.out.println("手机或电脑屏幕翻拍的照片Screen "+legality.getString("Screen"));
//		System.out.println("临时身份证照片Temporary ID Photo "+legality.getString("Temporary ID Photo"));
	}
}
