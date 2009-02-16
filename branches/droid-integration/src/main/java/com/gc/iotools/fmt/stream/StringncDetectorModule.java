package com.gc.iotools.fmt.stream;

/*
 * Copyright (c) 2008, Davide Simonetti.  All rights reserved.
 * 
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
import com.gc.iotools.fmt.base.FormatId;

public class StringncDetectorModule implements DefiniteLengthModule {
	private String byteSequence = null;
	private int detectLength = -1;
	private FormatId detectedFormat;

	public boolean detect(final byte[] readedBytes) {
		boolean result;
		String readString = new String(readedBytes);
		String ucaseStr = readString.toUpperCase();
		if (this.detectLength == this.byteSequence.length()) {
			result = byteSequence.equals(ucaseStr);
		} else {
			result = ucaseStr.contains(byteSequence);
		}
		return result;
	}

	public FormatId getDetectedFormat() {
		if (this.detectedFormat == null) {
			throw new IllegalStateException(
					"getDetectFormat called before init");
		}
		return this.detectedFormat;
	}

	public int getDetectLength() {
		if (this.byteSequence == null) {
			throw new IllegalStateException(
					"getDetectLength called before init");
		}
		return this.detectLength;
	}

	public void init(final FormatId fenum, final String param) {
		final int sepPos = param.indexOf(':');
		this.byteSequence = param.substring(sepPos + 1).toUpperCase();
		if (sepPos > 0) {
			final String detectLString = param.substring(0, sepPos);
			this.detectLength = Integer.parseInt(detectLString);
		}
		if (this.detectLength <= 0) {
			this.detectLength = this.byteSequence.length();
		}

		this.detectedFormat = fenum;
	}
	@Override
	public String toString() {
		return "StringNCModule [" + detectedFormat + "] len[" + detectLength
				+ "] strIgnoreCase [" + byteSequence + "]";
	}
}
