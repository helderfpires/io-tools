package com.gc.iotools.fmt.detect.wzf.custom;

/*
 * Copyright (c) 2008, 2014 Gabriele Contini. All rights reserved. Redistribution
 * and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met: * Redistributions
 * of source code must retain the above copyright notice, this list of
 * conditions and the following disclaimer. * Redistributions in binary form
 * must reproduce the above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other materials provided
 * with the distribution. * Neither the name of Gabriele Contini nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission. THIS SOFTWARE IS
 * PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.stream.Location;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLReporter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.detect.wzf.DefiniteLengthModule;

/**
 * Detect a file in XML
 * 
 * @author dvd.smnt
 * @since Nov 8, 2008
 */
public final class XmlModule implements DefiniteLengthModule {
	private final class MyReporter implements XMLReporter {

		MyReporter() {
			// to avoid synthetic method
		}

		public void report(final String message, final String errorType,
				final Object relatedInformation, final Location location)
				throws XMLStreamException {
			LOGGER.debug(location.toString());
		}

	}

	private static final Logger LOGGER = LoggerFactory
			.getLogger(XmlModule.class);

	private static final int XML_GUESS_SIZE = 8192;

	public boolean detect(final byte[] readBytes) {
		final XMLInputFactory factory = XMLInputFactory.newInstance();
		factory.setProperty(XMLInputFactory.IS_VALIDATING, Boolean.FALSE);
		factory.setXMLReporter(new MyReporter());

		final InputStream in = new ByteArrayInputStream(readBytes);
		boolean xmlDetected = false;
		long currentEvent = -1;
		try {
			final XMLEventReader parser = factory.createXMLEventReader(in);
			while (parser.hasNext()) {
				currentEvent++;
				final XMLEvent event = parser.nextEvent();
				XmlModule.LOGGER.debug("Found XML event ["
						+ event.getEventType() + "]");
			}
			xmlDetected = true;
			XmlModule.LOGGER.debug("XML detected (EOF reach)");
		} catch (final XMLStreamException e) {
			final Location location = e.getLocation();
			final String message = e.getMessage();
			final boolean locationCondition = location != null
					&& location.getCharacterOffset() == XmlModule.XML_GUESS_SIZE;
			if (locationCondition
					|| (message != null
							&& (message.contains("must start and end") || (e
									.getMessage().indexOf("end of stream") >= 0)) && readBytes.length == XML_GUESS_SIZE)) {
				xmlDetected = evaluateException(currentEvent);
			} else {
				XmlModule.LOGGER.debug("XML not detected " + e);
			}
		}
		return xmlDetected;
	}

	public FormatId getDetectedFormat() {
		return new FormatId(FormatEnum.XML, null);
	}

	public int getDetectLength() {
		return XmlModule.XML_GUESS_SIZE;
	}

	public void init(final FormatId fenum, final String param) {
		// TODO Auto-generated method stub

	}

	private boolean evaluateException(final long currentElem) {
		boolean tenum = false;
		if (currentElem != -1) {
			tenum = true;
			XmlModule.LOGGER.debug("XML (partial parsing) [" + currentElem
					+ "]");
		} else {
			XmlModule.LOGGER.debug("No xml found in first ["
					+ XmlModule.XML_GUESS_SIZE + "] bytes");
		}
		return tenum;
	}

}
