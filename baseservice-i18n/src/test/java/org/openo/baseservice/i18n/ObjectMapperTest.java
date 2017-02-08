package org.openo.baseservice.i18n;

import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperTest {
	public static void main(String[] args) throws Exception{
		String s="{"
+"\"uca\":\"统一公共应用\","
+"\"Virtual Ne Name\":\"虚网元\""
+"}";
		ObjectMapper mapper=new ObjectMapper();
		HashMap m=mapper.readValue(s, HashMap.class);
		System.out.println(m.get("Virtual Ne Name"));
	}
}
