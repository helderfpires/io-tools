package sandbox;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

import com.gc.iotools.stream.is.InputStreamFromOutputStream;

public class ReadAheadInputStream extends InputStreamFromOutputStream<Void> {
	private final InputStream source;

	public ReadAheadInputStream(InputStream source) {
		this.source = source;
	}


	@Override
	protected Void produce(OutputStream sink) throws Exception {
		IOUtils.copy(source, sink);
		return null;
	}

}
