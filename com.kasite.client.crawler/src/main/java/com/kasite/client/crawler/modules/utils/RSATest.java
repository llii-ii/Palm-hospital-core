package com.kasite.client.crawler.modules.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;

import com.kasite.core.httpclient.http.HttpRequestBus;
import com.kasite.core.httpclient.http.RequestType;
import com.kasite.core.httpclient.http.SoapResponseVo;

public class RSATest {
	private static String password = "d7ZyhqhH";
	private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDCKrm4ZHMD3oSZCwcRdcvB6BMHHEUOM/VQDw+DBN8lnQb1AzehKcJhFbJSisg8DvKxJ9y+8TRicI/uY/Ds6VIfRuM79dssH3dmqS0gbNNh/HuVVgVcz0g1mJLA16teaS+VdAlD8gan7YIHtnzlPQV6tbICrPut9sCUsNvZfWdrJQIDAQAB";
	private static String url = "http://27.154.233.186:9999/api/packages";
	private static String filePath = "/Users/daiyanshui/Desktop/上饶/demo/0dae00055a2c2010bdedcbbb5c9d86f6.zip";
	private static String org_code ="jkzl";
	public static void main(String[] args) throws Exception {
		Key key = RSA.genPublicKey(publicKey);
		String package_crypto = RSA.encrypt(password, key);
		System.out.println(package_crypto);
		File file = new File(filePath);
		BufferedInputStream is = null;
		try {
			if(null != file) {
				is = new BufferedInputStream( new FileInputStream(file));
				//读取文件内容  
		        byte[] b = new byte[is.available()];  
		        is.read(b);
		        String md5 = "";
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            md.update(b);;
	            BigInteger bigInt = new BigInteger(1, md.digest());
	            System.out.println("文件md5值：" + bigInt.toString(16));
	            md5 = bigInt.toString();
		        System.out.println("请求URL = "+ url); 
				is.close();
				uploadMultiFile(filePath, org_code, package_crypto, md5);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(null != is) {
				is.close();
			}
		}
	}
	
	

	/**
	 * 厦门上饶区域平台文件上传。
	 * */
	private static void uploadMultiFile(String filePath,String org_code,String package_crypto,String md5) {
		        
        SoapResponseVo result = HttpRequestBus.create(url, RequestType.fileUploadPost)
        		.addHttpParam("package_crypto", package_crypto)
        		.addHttpParam("md5", md5)
        		.setFile("pack",new File(filePath))
        		.addHttpParam("org_code", org_code).send();
        System.out.println(result.getCode()); 
        
//        File file = new File(filePath);
//        RequestBody fileBody = RequestBody.create(MediaType.parse("application/zip"), file);
//        MultipartBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM) 
//                .addFormDataPart("pack", file.getName(), fileBody)
//                .addFormDataPart("org_code", org_code)
//                .addFormDataPart("package_crypto", package_crypto)
//                .addFormDataPart("md5", md5)
//                .build();
//        
//        Request request = new Request.Builder()
//                .url(url)
//                .post(requestBody)
//                .build();
////        LogInterceptor l = new LogInterceptor();
//        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLogger());
//        final okhttp3.OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
//        OkHttpClient okHttpClient  = httpBuilder
//                //设置超时
//                .connectTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(15, TimeUnit.SECONDS)
//                .addNetworkInterceptor(interceptor)
//                .build();
//        okHttpClient.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
////                sysout(TAG, "uploadMultiFile() e=" + e);
//            		e.printStackTrace();
//            }
//
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                System.out.println(response.code());
//                System.out.println(response.body().string());
//                
//            }
//        });
    }
	
//	
//	
//	 public static String doPostSubmitBody(String url,Map<String, String> map,
//	            String filePath, byte[] body_data, String charset) {
//	        // 设置三个常用字符串常量：换行、前缀、分界线（NEWLINE、PREFIX、BOUNDARY）；
//	        final String NEWLINE = "\r\n"; // 换行，或者说是回车
//	        final String PREFIX = "--"; // 固定的前缀
//	        final String BOUNDARY = "59083976eebd"; // 分界线，boundary，可以是任意字符串#
//	        HttpURLConnection httpConn = null;
//	        BufferedInputStream bis = null;
//	        DataOutputStream dos = null;
//	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//	        try {
//	            // 实例化URL对象。调用URL有参构造方法，参数是一个url地址；
//	            URL urlObj = new URL(url);
//	            // 调用URL对象的openConnection()方法，创建HttpURLConnection对象；
//	            httpConn = (HttpURLConnection) urlObj.openConnection();
//	            // 调用HttpURLConnection对象setDoOutput(true)、setDoInput(true)、setRequestMethod("POST")；
//	            httpConn.setDoInput(true);
//	            httpConn.setDoOutput(true);
//	            httpConn.setRequestMethod("POST");
//	            // 设置Http请求头信息；（Accept、Connection、Accept-Encoding、Cache-Control、Content-Type、User-Agent），不重要的就不解释了
//	            httpConn.setUseCaches(false);
//	            httpConn.setRequestProperty("Connection", "Keep-Alive");
//	            httpConn.setRequestProperty("Accept", "*/*");
//	            httpConn.setRequestProperty("Accept-Encoding", "gzip, deflate");
//	            httpConn.setRequestProperty("Cache-Control", "no-cache");
//	            // 这个比较重要，按照上面分析的拼装出Content-Type头的内容
//	            httpConn.setRequestProperty("Content-Type","multipart/form-data; boundary=" + BOUNDARY);
//	            httpConn.setRequestProperty("User-Agent"," Apache-HttpClient/4.5.2 (Java/1.8.0_144)");
//	            // 调用HttpURLConnection对象的connect()方法，建立与服务器的真实连接；
//	            httpConn.connect();
//
//	            // 调用HttpURLConnection对象的getOutputStream()方法构建输出流对象；
//	            dos = new DataOutputStream(httpConn.getOutputStream());
//	            dos.writeBytes(NEWLINE);
////	            // 获取表单中上传控件之外的控件数据，写入到输出流对象（根据上面分析的抓包的内容格式拼凑字符串）；
//	            if (map != null && !map.isEmpty()) { // 这时请求中的普通参数，键值对类型的，相当于上面分析的请求中的username，可能有多个
//	                for (Map.Entry<String, String> entry : map.entrySet()) {
//	                    String key = entry.getKey(); // 键，相当于上面分析的请求中的username
//	                    String value = map.get(key); // 值，相当于上面分析的请求中的sdafdsa
//	                    dos.writeBytes(PREFIX + BOUNDARY + NEWLINE); // 像请求体中写分割线，就是前缀+分界线+换行
//	                    dos.writeBytes("Content-Disposition: form-data; " + "name=\"" + key + "\"" + NEWLINE); // 拼接参数名，格式就是Content-Disposition: form-data; name="key" 其中key就是当前循环的键值对的键，别忘了最后的换行 
//	                    dos.writeBytes("Content-Type: text/plain; charset=ISO-8859-1" + NEWLINE); 
//	                    dos.writeBytes("Content-Transfer-Encoding: 8bit" + NEWLINE);
//	                    dos.writeBytes(NEWLINE); // 空行
//	                    dos.writeBytes(URLEncoder.encode(value.toString(), charset)); // 将值写入
//	                    dos.writeBytes(NEWLINE); // 换行
//	                } 
//	            }
//
//	            // 获取表单中上传附件的数据，写入到输出流对象（根据上面分析的抓包的内容格式拼凑字符串）；
//	            if (body_data != null && body_data.length > 0) {
//	                dos.writeBytes(PREFIX + BOUNDARY + NEWLINE);// 像请求体中写分割线，就是前缀+分界线+换行
//	                String fileName = filePath.substring(filePath.lastIndexOf(File.separatorChar) + 1); // 通过文件路径截取出来文件的名称，也可以作文参数直接传过来
//	                // 格式是:Content-Disposition: form-data; name="请求参数名"; filename="文件名"
//	                // 不要忘了换行
//	                dos.writeBytes("Content-Disposition: form-data; " + "name=\"pack\" ; filename=\"" + fileName + NEWLINE);
//	                dos.writeBytes("Content-Type: application/octet-stream" + NEWLINE);
//	                dos.writeBytes("Content-Transfer-Encoding: binary" + NEWLINE);
//	                // 换行，重要！！不要忘了
//	                dos.writeBytes(NEWLINE);
//	                dos.write(body_data); // 上传文件的内容
//	                dos.writeBytes(NEWLINE); // 最后换行
//	            }
//	            dos.writeBytes(PREFIX + BOUNDARY + PREFIX + NEWLINE); // 最后的分割线，与前面的有点不一样是前缀+分界线+前缀+换行，最后多了一个前缀
//	            dos.flush();
//
//	            // 调用HttpURLConnection对象的getInputStream()方法构建输入流对象；
//	            byte[] buffer = new byte[8 * 1024];
//	            int c = 0;
//	            // 调用HttpURLConnection对象的getResponseCode()获取客户端与服务器端的连接状态码。如果是200，则执行以下操作，否则返回null；
//	            int statusCode = (httpConn.getResponseCode());
//	            if(statusCode == 200) {
//	            	 	bis = new BufferedInputStream(httpConn.getInputStream());
//	                 while ((c = bis.read(buffer)) != -1) {
//	                     baos.write(buffer, 0, c);
//	                     baos.flush();
//	                 }
//	            }else {
//	            		System.out.println("上传文件异常："+statusCode);
//	            }
//	            String result =  new String(baos.toByteArray(), charset);
//	            // 将输入流转成字节数组，返回给客户端。
//	            return result;
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	        } finally {
//	            try {
//	                if (dos != null)
//	                    dos.close();
//	                if (bis != null)
//	                    bis.close();
//	                if (baos != null)
//	                    baos.close();
//	                httpConn.disconnect();
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	            }
//	        }
//	        return null;
//	    }
}
