package com.gc.iotools.fmt.detect.wzf;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini.  All rights reserved.
 * 
 * Redistribution and use in source and binary forms, 
 * with or without modification, are permitted provided that the following 
 * conditions are met:
 *  * Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *  * Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *  * Neither the name of Gabriele Contini nor the names of its contributors may
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
import java.util.regex.Pattern;

import com.gc.iotools.fmt.base.FormatId;

class RegexpDetectorModule implements DefiniteLengthModule {
	private FormatId detectedFormat = null;
	private Pattern pattern = null;
	private int detectLength = 1;

	public boolean detect(final byte[] readBytes) {
		final String read = new String(readBytes);
		return this.pattern.matcher(read).matches();
	}

	public FormatId getDetectedFormat() {
		return this.detectedFormat;
	}

	public int getDetectLength() {
		return this.detectLength;
	}

	public void init(final FormatId fenum, final String param) {
		final int sepPos = param.indexOf(':');
		final String patternStr = param.substring(sepPos + 1);
		this.pattern = Pattern.compile(patternStr);
		this.detectedFormat = fenum;
		final String detectLString = param.substring(0, sepPos);
		this.detectLength = Integer.parseInt(detectLString);
	}

}
