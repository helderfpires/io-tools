package com.gc.iotools.stream.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class LogUtilsTest {

	class InnerClass {
		public String testMethod() {
			return LogUtils.getCaller(InnerClass.class, 2);
		}

		public String testMethod2() {
			return testMethod();
		}

	}

	@Test
	public void testGetCaller() {
		final InnerClass ic = new InnerClass();
		final String result = ic.testMethod2();
		System.out.println(result);
		assertFalse("Result contains 'not found' ",
				result.contains("not found"));
		assertEquals("Result deoesn't contain 'InnerClass' ", -1,
				result.indexOf("InnerClass"));
		assertTrue("Result contains LogUtilsTest " + result,
				result.indexOf("LogUtilsTest") >= 0);
		assertEquals("Number of logged frames", 2, result.split(" / ").length);
	}
}
