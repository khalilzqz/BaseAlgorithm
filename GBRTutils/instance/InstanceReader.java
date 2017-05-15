package instance;

import java.io.IOException;
import java.util.List;

import utils.Pair;

public interface InstanceReader {

    Pair<FeatureIndex, List<Instance>> read() throws IOException;
}
