package com.gc.iotools.stream.reader.inspection;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. This source code is released
 * under the BSD License.
 */
import java.io.FilterReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.stream.utils.LogUtils;

/**
 * <p>
 * A decorating <code>Reader</code> that detects and log useful debug
 * informations about the stream passed in the constructor, and detects wrong
 * usage patterns.
 * </p>
 * <ul>
 * <li><code>Reader</code> methods accessed after invocation of
 * <code>close()</code>.</li>
 * <li>Multiple invocations of <code>close()</code> method.</li>
 * <li>Missing <code>close()</code> invocation. Stream being garbage collected
 * without <code>close()</code> being called.</li>
 * </ul>
 * <p>
 * It normally acts as a {@link FilterReader} simply forwarding all the calls to
 * the <code>Reader</code> passed in the constructor, but also keeping track of
 * the usage of the methods.
 * </p>
 * <p>
 * Errors are both logged at WARN level and available through the standard class
 * interface. Future version will allow the customization of this behavior
 * disable the logging.
 * </p>
 * <p>
 * It is designed to detect also errors that happens during object finalization,
 * but to detect these errors in tests you must be very careful on your test
 * design (see example). Errors in finalizers are available trough the
 * {@linkplain #getFinalizationErrors()} method.
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
 *  Reader myTestData = ....
 *  DiagnosticReader&lt;Reader&gt; diagIs =
 *  	    new DiagnosticReader&lt;Reader&gt;(myTestData);
 *  //The class and the method under test
 *  MyClassUnderTest.myMethodUnderTest(diagIs);
 *  final String[] instanceWarnings = diagIs.getInstanceWarnings();
 *  assertTrue(&quot;No problems&quot; + diagIs.getStatusMessage(),
 * 			instanceWarnings.length == 0);
 * }
 * </pre>
 * 
 * </p>
 * <p>
 * If your code free resources in <code>finalize()</code> methods, or the
 * libraries you use do so you must use a more complex testing strategy because
 * the references to to the active <code>DiagnosticReader</code> instance in
 * your Junit prevents the class from being garbage collected. See the wiki for
 * details and {@linkplain #getFinalizationErrors()}.
 * </p>
 * 
 * @since 1.2.14
 * @author Gabriele Contini
 * @param <T>
 *            Type of the wrapped (contained) <code>Reader</code> to use in
 *            {@link #getWrappedReader()}.
 * @version $Id: DiagnosticReader.java 523 2013-01-02 15:46:17Z
 *          gabriele.contini@gmail.com $
 */
public class DiagnosticReader<T extends Reader> extends FilterReader {
	private static int defaultLogDepth = 2;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DiagnosticReader.class);

	private static final Collection<String> STATIC_WARNINGS = new ArrayList<String>();

	/**
	 * <p>
	 * Returns an array of descriptions of finalization errors. For instance
	 * when the stream is finalized but it was not closed.
	 * </p>
	 * 
	 * @return Description of finalization erros as an array of
	 *         {@link java.lang.String} objects.
	 */
	public static String[] getFinalizationErrors() {
		return STATIC_WARNINGS.toArray(new String[STATIC_WARNINGS.size()]);
	}

	/**
	 * <p>
	 * resetFinalizationErrors
	 * </p>
	 */
	public static void resetFinalizationErrors() {
		STATIC_WARNINGS.clear();
	}

	/**
	 * <p>
	 * Setter for the field <code>defaultLogDepth</code>.
	 * </p>
	 * 
	 * @param defaultFrameDepth
	 *            a int.
	 */
	public static void setDefaultLogDepth(final int defaultFrameDepth) {
		DiagnosticReader.defaultLogDepth = defaultFrameDepth;
	}

	private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

	private int closeCount = 0;
	private String closeTrace;
	private final String constructorTrace;

	private final int logDepth;

	private boolean methodCalledAfterClose = false;

	private final Collection<String> warnings = new ArrayList<String>();

	/**
	 * <p>
	 * Constructor for DiagnosticReader.
	 * </p>
	 * 
	 * @param in
	 *            the source Reader.
	 */
	public DiagnosticReader(final T in) {
		this(in, defaultLogDepth);
	}

	/**
	 * <p>
	 * Constructor for DiagnosticReader.
	 * </p>
	 * 
	 * @param Reader
	 *            the source Reader
	 * @param logDepth
	 *            Number of stack frames to log. It overrides the default static
	 *            value.
	 */
	public DiagnosticReader(final T reader, final int logDepth) {
		super(reader);
		if (reader == null) {
			throw new IllegalArgumentException(
					"Reader passed in the constructor is null");
		}
		if (logDepth <= 0) {
			throw new IllegalArgumentException("Required logDepth is ["
					+ logDepth + "] but it must be >0");
		}
		this.logDepth = logDepth;
		this.constructorTrace = LogUtils.getCaller(getClass(), logDepth);
	}

	private void checkCloseInvoked(final String methodName) {
		if (this.closeCount > 0) {
			this.methodCalledAfterClose = true;
			final String warning = "ALREADY_CLOSED: [" + methodName
					+ "] called by ["
					+ LogUtils.getCaller(DiagnosticReader.class, this.logDepth)
					+ "]";
			this.warnings.add(warning);
			LOGGER.warn(warning + "but the stream was already closed by ["
					+ this.closeTrace + "]");
		}
	}

	/**
	 * <p>
	 * clearInstanceWarnings
	 * </p>
	 */
	public void clearInstanceWarnings() {
		this.warnings.clear();
	}

	/** {@inheritDoc} */
	@Override
	public void close() throws IOException {
		if (this.closeCount == 0) {
			this.closeTrace = LogUtils.getCaller(DiagnosticReader.class,
					this.logDepth);
		} else {
			final String warning = "MULTIPLE_CLOSE : method "
					+ this.getClass().getSimpleName()
					+ ".close() is being called the[" + this.closeCount
					+ "]time by ["
					+ LogUtils.getCaller(DiagnosticReader.class, this.logDepth)
					+ "]";
			LOGGER.warn(warning + " but stream was already closed by ["
					+ this.closeTrace + "]");
			this.warnings.add(warning);
		}
		this.closeCount++;
		super.close();
	}

	/** {@inheritDoc} */
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
	 * @return the current captured bytes.
	 */
	public byte[] getContent() {
		return this.baos.toByteArray();
	}

	/**
	 * <p>
	 * getInstanceWarnings
	 * </p>
	 * 
	 * @return an array of {@link java.lang.String} objects.
	 */
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
	 * Returns the wrapped (original) <code>Reader</code> passed in the
	 * constructor. Any calls made to the returned stream will not be tracked by
	 * <code>DiagnosticReader</code>, so this method should be used with care,
	 * and <code>close()</code> and <code>read()</code> methods should'nt be
	 * called on the returned <code>Reader</code>. Instead these methods should
	 * be called on <code>DiagnosticReader</code> that simply forwards them to
	 * the underlying stream.
	 * </p>
	 * 
	 * @return The original <code>Reader</code> passed in the constructor
	 */
	public T getWrappedReader() {
		@SuppressWarnings("unchecked")
		final T result = (T) super.in;
		return result;
	}

	/**
	 * <p>
	 * isMethodCalledAfterClose check if some operation on the current reader
	 * was attempted after the method close() was invoked.
	 * </p>
	 * 
	 * @return a boolean: true if some operation was invoked after close().
	 */
	public boolean isMethodCalledAfterClose() {
		return this.methodCalledAfterClose;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @throws IOException
	 */
	@Override
	public synchronized void mark(final int readlimit) throws IOException {
		checkCloseInvoked("mark");
		super.mark(readlimit);
	}

	/** {@inheritDoc} */
	@Override
	public boolean markSupported() {
		checkCloseInvoked("markSupported");
		return super.markSupported();
	}

	/** {@inheritDoc} */
	@Override
	public int read() throws IOException {
		checkCloseInvoked("read()");
		return super.read();
	}

	/** {@inheritDoc} */
	@Override
	public int read(final char[] b) throws IOException {
		checkCloseInvoked("read(byte[])");
		return super.read(b);
	}

	/** {@inheritDoc} */
	@Override
	public int read(final char[] b, final int off, final int len)
			throws IOException {
		checkCloseInvoked("read(byte[],int,int)");
		return super.read(b, off, len);
	}

	/** {@inheritDoc} */
	@Override
	public synchronized void reset() throws IOException {
		checkCloseInvoked("reset");
		super.reset();
	}

	/** {@inheritDoc} */
	@Override
	public long skip(final long n) throws IOException {
		checkCloseInvoked("skip");
		return super.skip(n);
	}
}
