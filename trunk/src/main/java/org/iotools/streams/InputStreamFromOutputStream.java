package org.iotools.streams;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;

/**
 * <p>
 * This class simplifies the use of a pipe, hiding threading issues.
 * </p>
 * <p>
 * The data who is produced inside the function produce() is written to an
 * OutputStream and can be readed back from from this class.
 * </p>
 * 
 * <pre>
 * final String dataId=//id of some data.
 * final InputStreamFromOutputStream isos = new InputStreamFromOutputStream() {
 *   &#064;Override
 *   public void produce(final OutputStream dataSink) throws Exception {
 *      //call your application function who produces the data here
 *      produceMydata(dataId,dataSink)
 *   }
 * };
 * try {
 *  //now you can read from the InputStream the data that was written to the 
 *  //OutputStream
 * 	byte[] readed=IOUtils.toByteArray(isos);
 * } catch (final IOException e) {
 * 	Thread.sleep(1000);
 * 	assertEquals(&quot;Active Trheads&quot;, 0, InputStreamFromOutputStream
 * 			.getActiveThreads());
 * }
 * </pre>
 * 
 * @author Davide Simonetti
 * 
 */
public abstract class InputStreamFromOutputStream extends InputStream {

	private final class ExecutingThread implements Runnable {

		IOException exception = null;

		private final Logger logger = Logger
				.getLogger(InputStreamFromOutputStream.ExecutingThread.class);

		private String name = null;

		private OutputStream outputStream = null;

		ExecutingThread(final String threadName, final OutputStream ostream) {
			this.outputStream = ostream;
			this.name = threadName;
		}

		public void run() {
			InputStreamFromOutputStream.nthread++;
			logTooManyThreads();
			final String threadName = getName();
			InputStreamFromOutputStream.ACTIVE_THREAD_NAMES.add(threadName);
			this.logger.debug("Numero di thread " + getName()
					+ " correntemente attivi["
					+ InputStreamFromOutputStream.nthread + "]");
			try {
				produce(this.outputStream);
			} catch (final Throwable e) {
				this.logger.error("Errore durante l'elaborazione.", e);
				this.exception = new IOException(
						"Errore nel thread di elaborazione di ["
								+ getClass().getName() + "]");
				this.exception.initCause(e);
			} finally {
				closeStream();
				InputStreamFromOutputStream.nthread--;
				InputStreamFromOutputStream.ACTIVE_THREAD_NAMES
						.remove(threadName);
				this.logger.debug("thread [" + getName() + "] chiuso");
			}
		}

		private void closeStream() {
			try {
				this.outputStream.close();
			} catch (final IOException e) {
				if ((e.getMessage() != null)
						&& (e.getMessage().indexOf("closed") > 0)) {
					this.logger.debug("Stream gia' chiuso");
				} else {
					this.logger.error("Errore di IO tentando di chiudere "
							+ "l'OutputStream. " + "Probabile deadlock", e);
				}
			} catch (final Throwable t) {
				this.logger.error(
						"Errore tentando di chiudere l'OutputStream. "
								+ "Probabile deadlock", t);
			}
		}

		private void logTooManyThreads() {
			this.logger.debug("Thread [" + getName() + "] avviato.");
			if (InputStreamFromOutputStream.nthread > InputStreamFromOutputStream.THREAD_MAX) {
				this.logger.warn("Attenzione numero di thread ["
						+ getClass().getName() + "] =["
						+ InputStreamFromOutputStream.nthread
						+ "] dovrebbe essere <"
						+ InputStreamFromOutputStream.THREAD_MAX + "].");
			}
			if (InputStreamFromOutputStream.nthread == InputStreamFromOutputStream.THREAD_MAX) {
				this.logger.warn("Attenzione numero di thread ["
						+ getClass().getName() + "] =["
						+ InputStreamFromOutputStream.nthread
						+ "] dovrebbe essere <"
						+ InputStreamFromOutputStream.THREAD_MAX
						+ "]. Thread attivi:"
						+ Arrays.toString(getActiveThreadNames()));
			}
		}

		final String getName() {
			return this.name;
		}
	}

	private static final List<String> ACTIVE_THREAD_NAMES = Collections
			.synchronizedList(new ArrayList<String>());

	private static Executor EXECUTOR = new ThreadPoolExecutor(10, 20, 5,
			TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(500));

	private static final Log LOG = LogFactory
			.getLog(InputStreamFromOutputStream.class);

	private static int nthread = 0;

	public static int THREAD_MAX = 80;

	public static final String[] getActiveThreadNames() {
		final String[] result;
		synchronized (InputStreamFromOutputStream.ACTIVE_THREAD_NAMES) {
			result = InputStreamFromOutputStream.ACTIVE_THREAD_NAMES
					.toArray(new String[InputStreamFromOutputStream.ACTIVE_THREAD_NAMES
							.size()]);
		}
		return result;
	}

	public static final int getActiveThreads() {
		return InputStreamFromOutputStream.nthread;
	}

	private static Executor getExecutor(final ThreadModel tmodel) {
		switch (tmodel) {
		case THREAD_PER_INSTANCE:
		case STATIC_THREAD_POOL:
		default:
		}
		return null;
	}

	private boolean closeCalled = false;

	private final ExecutingThread executingThread;

	private final PipedInputStream pipedIS;

	private final Executor instanceExecutor;

	/**
	 * <p>
	 * It creates an InputStreamFromOutputStream with a THREAD_PER_INSTANCE
	 * threading strategy
	 * </p>
	 * 
	 * @see ThreadModel.THREAD_PER_INSTANCE
	 */
	public InputStreamFromOutputStream() {
		this(ThreadModel.THREAD_PER_INSTANCE);
	}

	public InputStreamFromOutputStream(final String tname,
			final Executor executor) {
		final String callerId = tname + getCaller();
		PipedOutputStream pipedOS = null;
		try {
			this.pipedIS = new PipedInputStream();
			pipedOS = new PipedOutputStream(this.pipedIS);
		} catch (final IOException e) {
			throw new RuntimeException("Error during pipe creaton", e);
		}
		this.executingThread = new ExecutingThread(callerId, pipedOS);
		final String tName = this.executingThread.getName();
		this.instanceExecutor = executor;
		executor.execute(this.executingThread);
		InputStreamFromOutputStream.LOG.debug("thread [" + tName
				+ "] queued for start.");
	}

	public InputStreamFromOutputStream(final ThreadModel tmodel) {
		this("", getExecutor(tmodel));
	}

	@Override
	public final void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			this.pipedIS.close();
		}
	}

	@Override
	public final int read() throws IOException {
		final int result = this.pipedIS.read();
		if ((result < 0) && (this.executingThread.exception != null)) {
			throw this.executingThread.exception;
		}
		return result;
	}

	@Override
	public final int read(final byte[] b) throws IOException {
		final int result = this.pipedIS.read(b);
		if ((result < 0) && (this.executingThread.exception != null)) {
			throw this.executingThread.exception;
		}
		return result;
	}

	@Override
	public final int read(final byte[] b, final int off, final int len)
			throws IOException {
		final int result = this.pipedIS.read(b, off, len);
		if ((result < 0) && (this.executingThread.exception != null)) {
			throw this.executingThread.exception;
		}
		return result;

	}

	private String getCaller() {
		final Exception exception = new Exception();
		final StackTraceElement[] stes = exception.getStackTrace();
		final StackTraceElement caller = stes[3];
		final String result = getClass().getName().substring(
				getClass().getPackage().getName().length() + 1)
				+ "callBy:" + caller.toString();
		InputStreamFromOutputStream.LOG.debug("OpenedBy [" + result + "]");
		return result;
	}

	/**
	 * <p>
	 * This method must be implemented to produce the data that must be read
	 * from the external InputStream.
	 * </p>
	 * <p>
	 * Special care must be paid passing arguments to this method because it
	 * executes in another thread.
	 * </p>
	 * 
	 * @param dataSink
	 * @throws Exception
	 */
	protected abstract void produce(final OutputStream dataSink)
			throws Exception;

}
