package model;

import java.util.List;

import instance.Instance;

public interface Model {

	void fit(List<Instance> samples) throws Exception;

	/**
	 * @param sample
	 *            not modified
	 */
	double predict(Instance sample);
}
