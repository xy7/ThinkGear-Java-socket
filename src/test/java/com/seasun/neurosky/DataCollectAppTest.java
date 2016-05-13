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
		LocalDateTime time = LocalDateTime.now();
		
		dca.poorSignalEvent(time, 1);
		dca.blinkEvent(time, 10);
		dca.eegPowerEvent(time, 1, 2, 3, 4, 5, 6, 7, 8);
		dca.rawEegEvent(time, 10, 0);
		dca.esenseEvent(time, 20, 20);
	}

}
