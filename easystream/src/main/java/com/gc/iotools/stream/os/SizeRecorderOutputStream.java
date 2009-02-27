package com.gc.iotools.stream.os;

/*
 * Copyright (c) 2008, Davide Simonetti
 * All rights reserved.
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided that the following 
 * conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Davide Simonetti nor the names of its contributors may
 *    be used to endorse or promote products derived from this software without 
 *    specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY 
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR 
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER 
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
 */
import java.io.IOException;
import java.io.OutputStream;

/**
 * <p>
 * This class counts the number of bytes written to the
 * <code>OutputStream</code> passed in the constructor.
 * </p>
 * 
 * TODO: junits
 * 
 * @author dvd.smnt
 * @since 1.0.6
 */
public class SizeRecorderOutputStream extends OutputStream {

	private boolean closeCalled;
	private final OutputStream innerOs;
	private long size = 0;

	/**
	 * Creates a new <code>SizeRecorderOutputStream</code> with the given
	 * destination stream.
	 * 
	 * @param destination
	 *            Destination stream where data are written.
	 */
	public SizeRecorderOutputStream(final OutputStream destination) {
		this.innerOs = destination;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() throws IOException {
		if (!this.closeCalled) {
			this.closeCalled = true;
			this.innerOs.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void flush() throws IOException {
		this.innerOs.flush();
	}

	/**
	 * Returns the number of bytes written until now.
	 * 
	 * @return return the number of bytes written until now.
	 */
	public long getSize() {
		return this.size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(final byte[] b) throws IOException {
		this.innerOs.write(b);
		this.size += b.length;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(final byte[] b, final int off, final int len)
			throws IOException {
		this.innerOs.write(b, off, len);
		this.size += len;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void write(final int b) throws IOException {
		this.innerOs.write(b);
		this.size++;
	}

}
