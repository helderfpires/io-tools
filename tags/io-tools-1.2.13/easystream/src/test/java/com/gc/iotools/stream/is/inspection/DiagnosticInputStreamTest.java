package com.gc.iotools.stream.is.inspection;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.junit.AfterClass;
import org.junit.BeforeClass;

public class DiagnosticInputStreamTest {

	@BeforeClass
	public static void setUp() {
		DiagnosticInputStream.resetFinalizationErrors();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		System.gc();
		Thread.sleep(2000);
		final String[] errors = DiagnosticInputStream.getFinalizationErrors();
		assertEquals("number of errors", 1, errors.length);
		assertTrue("Error not closed [" + errors[0] + "]",
				errors[0].contains("NOT_CLOSED"));
		System.out.println(errors[0]);
	}

	@org.junit.Test
	public void testCloseNotCalled() throws Exception {
		final ByteArrayInputStream bais = new ByteArrayInputStream(
				"1234".getBytes());
		final DiagnosticInputStream<InputStream> diagIs = new DiagnosticInputStream<InputStream>(
				bais);
		diagIs.read();
		// errors will be collected in tearDown
		diagIs.clearInstanceWarnings();
	}

	@org.junit.Test
	public void testMethodCalledAfterClose() throws Exception {
		final ByteArrayInputStream bais = new ByteArrayInputStream(
				"1234".getBytes());
		final DiagnosticInputStream<InputStream> diagIs = new DiagnosticInputStream<InputStream>(
				bais);
		diagIs.close();
		diagIs.read();
		final String[] instanceWarnings = diagIs.getInstanceWarnings();
		assertEquals("Warning number", 1, instanceWarnings.length);
		assertTrue("warn string [" + instanceWarnings[0] + "]",
				instanceWarnings[0].startsWith("ALREADY_CLOSED"));
		assertTrue("isMethodCalledAfterClose ",
				diagIs.isMethodCalledAfterClose());
		final String statusString = diagIs.getStatusMessage();
		assertNotNull("Status string", statusString);
		assertTrue("Status string contains error",
				statusString.contains("ALREADY_CLOSED"));
		diagIs.clearInstanceWarnings();
	}

	@org.junit.Test
	public void testWarnDoubleClose() throws Exception {
		final ByteArrayInputStream bais = new ByteArrayInputStream(
				"1".getBytes());
		final DiagnosticInputStream<InputStream> diagIs = new DiagnosticInputStream<InputStream>(
				bais);
		diagIs.close();
		diagIs.close();
		final String[] instanceWarnings = diagIs.getInstanceWarnings();
		assertEquals("Warning number", 1, instanceWarnings.length);
		assertTrue("warn string [" + instanceWarnings[0] + "]",
				instanceWarnings[0].startsWith("MULTIPLE_CLOSE"));
		assertTrue("countClose ", diagIs.getCloseCount() == 2);
		diagIs.clearInstanceWarnings();
	}
}
