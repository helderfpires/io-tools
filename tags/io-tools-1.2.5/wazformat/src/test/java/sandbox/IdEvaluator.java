package sandbox;

import java.io.IOException;

import com.gc.iotools.fmt.base.DetectionLibrary;
import com.gc.iotools.fmt.base.FormatEnum;
import com.gc.iotools.fmt.base.FormatId;
import com.gc.iotools.fmt.base.ResettableInputStream;

public class IdEvaluator implements DetectionLibrary {

	public FormatId detect(FormatEnum[] enabledFormats,
			ResettableInputStream stream) throws IOException {
		return null;
	}

	public FormatEnum[] getDetectedFormats() {
		// TODO Auto-generated method stub
		return null;
	}

}
