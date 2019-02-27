package com.kasite.core.common.validator.dict.impl;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import com.coreframework.util.StringUtil;
import com.kasite.core.common.config.KasiteConfig;
import com.kasite.core.common.validator.dict.IDC10;

public class IDC10Impl implements IDC10{

	private static String idc10 = "";
	
	private static String read(String fileName) throws IOException, URISyntaxException {
		byte[] data = Files.readAllBytes(new File(IDC10Impl.class.getResource(fileName).toURI()).toPath());
		return new String(data, StandardCharsets.UTF_8);
	}

	public static void main(String[] args) throws IOException, Exception {
		String n = new IDC10Impl().getName("A02.901");
		KasiteConfig.print(n);
	}
	
	@Override
	public String getName(String value) {
		try {
			if(StringUtil.isBlank(idc10)) {
				idc10 = read("IDC10.txt");
			}
			String[] idc10s = idc10.split("\n");
			for (String string : idc10s) {
				String[] dic = string.split("\t");
				if(value.equals(dic[0])) {
					return dic[1];
				}
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

}
