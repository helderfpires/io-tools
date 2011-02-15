package com.gc.iotools.fmt.detect.wzf.custom;

public class FormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4218754841913232579L;

	protected FormatException() {
		super();
	}

	public FormatException(final String message) {
		super(message);
	}

	public FormatException(final String message, final Throwable cause) {
		super(message, cause);
	}


}
