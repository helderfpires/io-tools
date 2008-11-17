package org.googlecode.iotools.fmt;

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

import org.googlecode.iotools.fmt.base.FormatEnum;

/**
 * This class shields detections of formats not requested. It's useful when the
 * stream is wrapped multiple times.
 * 
 * @author dvd.smt
 */

final class GuessInputStreamWrapper extends GuessInputStream {
	private final GuessInputStream gis;

	public GuessInputStreamWrapper(final GuessInputStream gis,
			final FormatEnum[] enabledFormats) {
		super(enabledFormats);
		this.gis = gis;
	}

	public int available() throws IOException {
		return this.gis.available();
	}

	public FormatEnum getFormat() {
		FormatEnum fenum = this.gis.getFormat();
		if (!canDetect(fenum)) {
			fenum = FormatEnum.UNKNOWN;
		}
		return fenum;
	}

	public FormatEnum[] getFormats() {
		return this.gis.getFormats();
	}

	public boolean markSupported() {
		return this.gis.markSupported();
	}

	public int read() throws IOException {
		return this.gis.read();
	}

	public int read(final byte[] b) throws IOException {
		return this.gis.read(b);
	}

	public int read(final byte[] b, final int off, final int len)
			throws IOException {
		return this.gis.read(b, off, len);
	}

	public void reset() throws IOException {
		this.gis.reset();
	}

	public long skip(final long n) throws IOException {
		return this.gis.skip(n);
	}

}