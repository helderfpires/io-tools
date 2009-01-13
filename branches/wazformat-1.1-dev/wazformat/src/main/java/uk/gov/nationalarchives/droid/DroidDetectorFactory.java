package uk.gov.nationalarchives.droid;

import com.gc.iotools.fmt.base.Detector;
import com.gc.iotools.fmt.base.FormatEnum;

public final class DroidDetectorFactory<T extends FormatEnum> {

	public void addConfiguration(final String configFile) {

	}

	public Detector<T>[] getConfiguredDetectors(final T[] requestedFormats) {
		return null;
	}
}
