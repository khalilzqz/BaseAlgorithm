package instance;

import java.util.HashMap;
import java.util.Map;

public class FeatureIndex {

	private final String[] featureNames;
	private final Map<String, Integer> featureIdx;

	public FeatureIndex(String[] featureNames) {
		this.featureNames = featureNames;
		featureIdx = new HashMap<>();
		for (int i = 0; i < featureNames.length; i++) {
			featureIdx.put(featureNames[i], i);
		}
	}

	public int idx(String feature) {
		return featureIdx.get(feature);
	}

	public String name(int idx) {
		return featureNames[idx];
	}

	public double featureValue(Instance instance, String name) {
		return instance.x[idx(name)];
	}

	public int size() {
		return featureNames.length;
	}
}
