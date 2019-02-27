package test.com.kasite.client.crawler.checkdata;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.common.json.JSONObject;
import com.kasite.client.crawler.modules.upload.datacloud.DataCloudFileUtil;

public class CheckOriginData {
	
	private static String read(String fileName) throws IOException, URISyntaxException {
		byte[] data = Files.readAllBytes(new File(CheckOriginData.class.getResource(fileName).toURI()).toPath());
		return new String(data, StandardCharsets.UTF_8);
	}
	
	public static void main(String[] args) throws Exception {
		String jsonstr = read("1.json");
		System.out.println();
		
		JSONObject json = new JSONObject(jsonstr);
		DataCloudFileUtil.write(json, "test",true);
	}
}


