package com.gc.iotools.fmt.deflen;

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
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.bea.xml.stream.MXParserFactory;
import com.gc.iotools.fmt.base.FormatEnum;

/**
 * Detect a file in XML
 * 
 * @author dvd.smnt
 * @since Nov 8, 2008
 */
final class XmlDetector extends AbstractFormatDetector {

	private final class MyReporter implements XMLReporter {

		MyReporter() {
			// to avoid synthetic method
		}

		public void report(final String message, final String errorType,
				final Object relatedInformation, final Location location)
				throws XMLStreamException {
			LOGGER.debug(location);
		}

	}

	private static final int XML_GUESS_SIZE = 8192;

	static final Log LOGGER = LogFactory.getLog(XmlDetector.class);

	public XmlDetector() {
		super(XmlDetector.XML_GUESS_SIZE, FormatEnum.XML);
	}

	public boolean detect(final byte[] readedBytes) {
		final XMLInputFactory factory = XMLInputFactory.newInstance(
				MXParserFactory.class.getName(), XmlDetector.class
						.getClassLoader());
		factory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
		factory.setXMLReporter(new MyReporter());

		final InputStream in = new ByteArrayInputStream(readedBytes);
		boolean xmlDetected = false;
		long currentEvent = -1;
		try {
			final XMLEventReader parser = factory.createXMLEventReader(in);
			while (parser.hasNext()) {
				currentEvent++;
				final XMLEvent event = parser.nextEvent();
				XmlDetector.LOGGER.debug("Found XML event ["
						+ event.getEventType() + "]");
			}
			xmlDetected = true;
			XmlDetector.LOGGER.debug("XML detected (EOF reach)");
		} catch (final XMLStreamException e) {
			if ((e.getMessage() != null)
					&& (e.getMessage().indexOf("end of stream") >= 0)
					&& (readedBytes.length == XmlDetector.XML_GUESS_SIZE)) {
				xmlDetected = evaluateException(currentEvent);
			} else {
				XmlDetector.LOGGER.debug("XML not detected " + e);
			}
		}
		return xmlDetected;
	}

	private boolean evaluateException(final long currentElem) {
		boolean tenum = false;
		if (currentElem != -1) {
			tenum = true;
			XmlDetector.LOGGER.debug("XML (partial parsing) [" + currentElem
					+ "]");
		} else {
			XmlDetector.LOGGER.debug("No xml found in first ["
					+ XmlDetector.XML_GUESS_SIZE + "] bytes");
		}
		return tenum;
	}

}
