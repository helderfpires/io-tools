package com.gc.iotools.stream.is.inspection;

/*
 * Copyright (c) 2008,2011 Davide Simonetti. This source code is released
 * under the BSD License.
 */
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.stream.utils.LogUtils;

/**
 * <p>
 * Detects and log useful debug informations about the stream passed in the
 * constructor, and detects wrong usage patterns.
 * </p>
 * <ul>
 * <li><code>InputStream</code> methods accessed after invocation of
 * <code>close()</code>.</li>
 * <li>Multiple invocations of <code>close()</code> method.</li>
 * <li>Missing <code>close()</code> invocation. Stream being garbage collected
 * without <code>close()</code> being called.</li>
 * </ul>
 * <p>
 * It normally acts as a {@link FilterInputStream} simply forwarding all the
 * calls to the <code>InputStream</code> passed in the constructor, but also
 * keeping track of the usage of the methods.
 * </p>
 * <p>
 * Errors are both logged at WARN level and available through the standard
 * class interface. Future version will allow the customization of this
 * behavior disable the logging.
 * </p>
 * <p>
 * It is designed to detect also errors that happens during object
 * finalization, but to detect these errors in tests you must be very careful
 * on your test design (see example). Errors in finalizers are available
 * trough the {@linkplain #getFinalizationErrors()} method.
 * </p>
 * <p>
 * It's an useful tool in unit tests to detect wrong handling of the streams,
 * but it can be used in main applications too since it adds a very little
 * overhead in standard situations.
 * </p>
 * <p>
 * Sample Usage (in Junit 4):
 * 
 * <pre>
 * &#064;org.junit.Test
 * public void testWarnDoubleClose() throws Exception {
 *  InputStream myTestData = ....
 *  DiagnosticInputStream&lt;InputStream&gt; diagIs = 
 *  	    new DiagnosticInputStream&lt;InputStream&gt;(myTestData);
 *  //The class and the method under test
 *  MyClassUnderTest.myMethodUnderTest(diagIs);
 *  final String[] instanceWarnings = diagIs.getInstanceWarnings();
 *  assertTrue(&quot;No problems&quot; + diagIs.getStatusMessage(), 
 * 			instanceWarnings.length == 0);
 * }
 * </pre>
 * </p>
 * <p>
 * If your code free resources in <code>finalize()</code> methods, or the
 * libraries you use do so you must use a more complex testing strategy
 * because the references to to the active <code>DiagnosticInputStream</code>
 * instance in your Junit prevents the class from being garbage collected. See
 * the wiki for details and {@linkplain #getFinalizationErrors()}.
 * </p>
 * 
 * @since 1.2.6
 * @author dvd.msnt
 * @param <T>
 *            Type of the wrapped (contained)<code>InputStream</code>.
 */
public class DiagnosticInputStream<T extends InputStream> extends
		FilterInputStream {
	private static int defaultLogDepth = 2;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DiagnosticInputStream.class);

	private static final Collection<String> STATIC_WARNINGS = new ArrayList<String>();

	public static String[] getFinalizationErrors() {
		return STATIC_WARNINGS.toArray(new String[STATIC_WARNINGS.size()]);
	}

	public static void resetFinalizationErrors() {
		STATIC_WARNINGS.clear();
	}

	public static void setDefaultLogDepth(final int defaultFrameDepth) {
		DiagnosticInputStream.defaultLogDepth = defaultFrameDepth;
	}

	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

	private int closeCount = 0;
	private String closeTrace;
	private final String constructorTrace;

	private final int logDepth;

	private boolean methodCalledAfterClose = false;

	private final Collection<String> warnings = new ArrayList<String>();

	/**
	 * @param in
	 *            the source InputStream.
	 */
	public DiagnosticInputStream(final T in) {
		this(in, defaultLogDepth);
	}

	/**
	 * @param inputStream
	 *            the source InputStream
	 * @param logDepth
	 *            Number of stack frames to log. It overrides the default
	 *            static value.
	 */
	public DiagnosticInputStream(final T inputStream, final int logDepth) {
		super(inputStream);
		if (inputStream == null) {
			throw new IllegalArgumentException(
					"InputStream passed in the constructor is null");
		}
		if (logDepth <= 0) {
			throw new IllegalArgumentException("Required logDepth is ["
					+ logDepth + "] but it must be >0");
		}
		this.logDepth = logDepth;
		this.constructorTrace = LogUtils.getCaller(getClass(), logDepth);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int available() throws IOException {
		checkCloseInvoked("available");
		return super.available();
	}

	private void checkCloseInvoked(final String methodName) {
		if (this.closeCount > 0) {
			this.methodCalledAfterClose = true;
			final String warning = "ALREADY_CLOSED: ["
					+ methodName
					+ "] called by ["
					+ LogUtils.getCaller(DiagnosticInputStream.class,
							this.logDepth) + "]";
			this.warnings.add(warning);
			LOGGER.warn(warning + "but the stream was already closed by ["
					+ this.closeTrace + "]");
		}
	}

	public void clearInstanceWarnings() {
		this.warnings.clear();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		if (this.closeCount == 0) {
			this.closeTrace = LogUtils.getCaller(DiagnosticInputStream.class,
					this.logDepth);
		} else {
			final String warning = "MULTIPLE_CLOSE : method "
					+ this.getClass().getSimpleName()
					+ ".close() is being called the["
					+ this.closeCount
					+ "]time by ["
					+ LogUtils.getCaller(DiagnosticInputStream.class,
							this.logDepth) + "]";
			LOGGER.warn(warning + " but stream was already closed by ["
					+ this.closeTrace + "]");
			this.warnings.add(warning);
		}
		this.closeCount++;
		super.close();
	}

	@Override
	public void finalize() throws Throwable {
		if (this.closeCount == 0) {
			final String msg = "NOT_CLOSED : Finalizing ["
					+ this.getClass().getSimpleName()
					+ "] but close was not called yet. Wrapping class["
					+ this.in.getClass().getSimpleName() + "]";
			this.warnings.add(msg);
			LOGGER.warn(msg + " Constructor trace:" + this.constructorTrace);
		}
		try {
			super.finalize();
		} finally {
			if (this.warnings.size() > 0) {
				final String statusString = getStatusMessage();
				LOGGER.warn(statusString);
				final String methodName = getConstructorCallerMethod();
				STATIC_WARNINGS.add(methodName + " : " + statusString);
			}
		}
	}

	/**
	 * Returns the number of times that close was called on this stream.
	 * 
	 * @return number of times that close was called on this stream.
	 */
	public int getCloseCount() {
		return this.closeCount;
	}

	private String getConstructorCallerMethod() {
		return this.constructorTrace.substring(
				this.constructorTrace.indexOf('.') + 1,
				this.constructorTrace.indexOf(':'));
	}

	/**
	 * <p>
	 * Return the current captured bytes, if capture was enabled.
	 * </p>
	 * <p>
	 * The capture buffer might be truncated if maxCapture is set.
	 * </p>
	 * 
	 * @since 1.2.9
	 * @return the current captured bytes.
	 */
	public byte[] getContent() {
		return this.baos.toByteArray();
	}

	public String[] getInstanceWarnings() {
		return this.warnings.toArray(new String[this.warnings.size()]);
	}

	/**
	 * <p>
	 * Returns a string representation of the usage errors of the stream until
	 * now. Null if no error happened yet.
	 * </p>
	 * 
	 * @return String message that represents the errors, null if no error.
	 */
	public String getStatusMessage() {
		String result = null;
		if (this.warnings.size() > 0) {
			final StringBuffer resultb = new StringBuffer(getClass()
					.getSimpleName());
			resultb.append(" constructed by [" + this.constructorTrace + "] ");
			if (this.closeCount > 0) {
				resultb.append("closed by: [" + this.closeTrace
						+ "] has warnings:");
			}
			boolean first = true;
			for (final String warning : this.warnings) {
				resultb.append(warning);
				resultb.append(first ? "" : "-----------");
				first = false;
			}
			result = resultb.toString();
		}
		return result;
	}

	/**
	 * <p>
	 * Returns the wrapped (original) <code>InputStream</code> passed in the
	 * constructor. Any calls made to the returned stream will not be tracked
	 * by <code>DiagnosticInputStream</code>, so this method should be used
	 * with care, and <code>close()</code> and <code>read()</code> methods
	 * should'nt be called on the returned <code>InputStream</code>. Instead
	 * these methods should be called on <code>DiagnosticInputStream</code>
	 * that simply forwards them to the underlying stream.
	 * </p>
	 * 
	 * @return The original <code>InputStream</code> passed in the constructor
	 */
	public T getWrappedInputStream() {
		@SuppressWarnings("unchecked")
		final T result = (T) super.in;
		return result;
	}

	public boolean isMethodCalledAfterClose() {
		return this.methodCalledAfterClose;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void mark(final int readlimit) {
		checkCloseInvoked("mark");
		super.mark(readlimit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean markSupported() {
		checkCloseInvoked("markSupported");
		return super.markSupported();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read() throws IOException {
		checkCloseInvoked("read()");
		return super.read();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(final byte[] b) throws IOException {
		checkCloseInvoked("read(byte[])");
		return super.read(b);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		checkCloseInvoked("read(byte[],int,int)");
		return super.read(b, off, len);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void reset() throws IOException {
		checkCloseInvoked("reset");
		super.reset();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long skip(final long n) throws IOException {
		checkCloseInvoked("skip");
		return super.skip(n);
	}
}
