package com.kasite.core.common.util;


import java.io.IOException;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

public class StringAdapter extends TypeAdapter<String>{

	@Override
	public String read(JsonReader arg0) throws IOException {
		if (arg0.peek() == JsonToken.NULL) {  
			arg0.nextNull();  
            return null;  
        }  
		
        String val = arg0.nextString();
        if (val.equals("")) { // 反序列化时将 "" 转为 null  
            return null;  
        } else {  
            return val; 
        }  
	}

	@Override
	public void write(JsonWriter arg0, String arg1) throws IOException {
		if (arg1 == null) {  
			arg0.value(""); // 序列化时将 null 转为 ""  
        } else {  
        	arg0.value(arg1);
        }
		
	}

}
