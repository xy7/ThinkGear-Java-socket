package com.seasun.neurosky;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Test;

public class ThinkGearSocketTest {

	@Test
	public void test() {
		LocalDateTime ldt = LocalDateTime.now();
		System.out.println(ldt.toString().substring(0, 19) + "." + ldt.getNano() );
	}

}
