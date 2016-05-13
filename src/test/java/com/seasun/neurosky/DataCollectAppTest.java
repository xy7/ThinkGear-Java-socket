package com.seasun.neurosky;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class DataCollectAppTest {

	@Test
	public void test() {

		DataCollectApp dca = new DataCollectApp();
		dca.initJdbc();
		
		Map<String, Object> para = new HashMap<>();
		LocalDateTime ldt = LocalDateTime.now();
		para.put("t", ldt.toString().substring(0, 19).replace("T", " ") + "." + ldt.getNano());
		para.put("c", 13);
		dca.jdbc.update("insert into test(t, c) values(:t, :c)", para);
	}

}
