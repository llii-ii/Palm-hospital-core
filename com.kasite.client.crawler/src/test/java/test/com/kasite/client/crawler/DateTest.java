package test.com.kasite.client.crawler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTest {

	
	public static void main(String[] args) throws ParseException {

		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		System.out.println(df2.format(new Date()));
		
		df2.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date dt2 = df2.parse("2016-08-12T16:00Z");
		System.out.println(dt2);
		
		String data="2015-05-29T08:59:36Z";
        data = data.replaceAll("[A-Z]+"," ").trim();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date newData = sdf.parse(data);
        
        
        
	}
}
