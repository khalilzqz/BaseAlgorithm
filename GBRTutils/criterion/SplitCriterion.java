package criterion;

import java.util.List;

import instance.Instance;

public interface SplitCriterion {

	double reset(List<Instance> samples);

	void splitWithThreshold(int feature, double threshold);

	/**
	 * move first num samples from right to left
	 * 
	 * @param num
	 * @return is anything moved
	 */
	boolean moveLeft(int num);

	/**
	 * current pos
	 */
	int rightBegIdx();

	/**
	 * @return total impurity
	 */
	double impurity();

	int leftSize();

	int rightSize();
}
