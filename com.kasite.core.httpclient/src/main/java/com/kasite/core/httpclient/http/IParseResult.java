package com.kasite.core.httpclient.http;

public interface IParseResult {

	String parse(String result) throws Exception;
	
	String parse(byte[] bytes) throws Exception;
	
}
