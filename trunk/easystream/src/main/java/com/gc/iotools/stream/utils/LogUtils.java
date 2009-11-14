package com.gc.iotools.stream.utils;

/**
 * Utilities for logging.
 * 
 * @author dvd.smnt
 * @since 1.0.9
 */
public final class LogUtils {
	private LogUtils() {
	}

	/**
	 * Returns the caller of the class passed as an argument. Useful for
	 * logging.
	 * 
	 * @param clazz
	 *            The current class
	 * @return The class name up one level in the stack.
	 */
	public static String getCaller(Class<?> clazz) {
		return getCaller(clazz, 1);
	}

	/**
	 * Returns the caller stack of the class passed as an argument.
	 * 
	 * Useful for logging.
	 * 
	 * @since 1.2.6
	 * @param nframes
	 *            Number of stack frames to log out.
	 * @param clazz
	 *            The current class
	 * @return All the class names, methods and line numbers up nframes level in
	 *         the stack.
	 */
	public static String getCaller(Class<?> me, int nframes) {
		final StackTraceElement[] stes = Thread.currentThread()
				.getStackTrace();
		boolean foundCaller = false;
		boolean foundClazz = false;
		int stackTracePosition = 0;
		for (; (stackTracePosition < stes.length) && !foundCaller; stackTracePosition++) {
			StackTraceElement stackTraceElement = stes[stackTracePosition];
			foundClazz |= me.getName().equals(
					stackTraceElement.getClassName());
			foundCaller |= foundClazz
					&& !me.getName().equals(stackTraceElement.getClassName());
		}
		stackTracePosition--;
		String result;
		if (!foundCaller) {
			result = "class [" + me.getName()
					+ "] not found in caller's stack trace.";
		} else {
			StringBuffer resBuffer = new StringBuffer();
			for (int i = stackTracePosition; (i < stes.length)
					&& (i < (stackTracePosition + nframes)); i++) {
				StackTraceElement stelement = stes[i];
				final String className = stelement.getClassName();
				String simpleClassName = className.substring(className
						.lastIndexOf('.') + 1);
				if (resBuffer.length() > 0) {
					resBuffer.append(" / ");
				}
				resBuffer.append(simpleClassName + "."
						+ stelement.getMethodName() + ":"
						+ stelement.getLineNumber());
			}
			result = resBuffer.toString();
		}
		return result;
	}
}
