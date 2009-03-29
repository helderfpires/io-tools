package com.gc.iotools.fmt.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ResettableFileInputStream extends ResettableInputStream {

	public ResettableFileInputStream(File file) throws FileNotFoundException {
		this.file = file;
		this.currentStream = new FileInputStream(file);
	}

	private InputStream currentStream;
	private final File file;
	
	@Override
	public void resetToBeginning() throws IOException {
		this.currentStream.close();
		this.currentStream = new FileInputStream(this.file);
	}

	@Override
	public int available() throws IOException {
		return this.currentStream.available();
	}

	@Override
	public void close() throws IOException {
		this.currentStream.close();
	}

	@Override
	public void mark(int readlimit) {
		this.currentStream.mark(readlimit);
	}

	@Override
	public boolean markSupported() {
		return this.currentStream.markSupported();
	}

	@Override
	public int read() throws IOException {
		return this.currentStream.read();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return this.currentStream.read(b, off, len);
	}

	@Override
	public int read(byte[] b) throws IOException {
		return this.currentStream.read(b);
	}

	@Override
	public void reset() throws IOException {
		this.currentStream.reset();
	}

	@Override
	public long skip(long n) throws IOException {
		return this.currentStream.skip(n);
	}

	

}
