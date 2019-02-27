package test.com.kasite.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;


public class Q {

	private static String read(String fileName) throws IOException, URISyntaxException {
		byte[] data = Files.readAllBytes(new File(Q.class.getResource(fileName).toURI()).toPath());
		return new String(data, StandardCharsets.UTF_8);
	}
	
	public static void main(String[] args) throws Exception, URISyntaxException {
		// TODO Auto-generated method stub
		String s = read("querypersion.txt");
		String[] strs = s.split("\r");
		int a = strs.length;
		int j = 0;
		System.out.println("<Response>");
		while(j < a) {
			String s1 = strs[j];
			String s2 = strs[j+1];
			System.out.println("<"+s2+">"+s1+"</"+s2+">");
			j = j+2;
		}
		System.out.println("</Response>");
	}

}
