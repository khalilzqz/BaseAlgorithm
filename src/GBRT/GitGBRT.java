package GBRT;

import java.util.List;

import instance.CsvReader;
import instance.FeatureIndex;
import instance.Instance;
import model.GbdtClassifier;
import model.GbdtParams.GbdtParamsBuilder;
import utils.MathUtils;
import utils.Pair;

public class GitGBRT {
	public static void main(String[] args) throws Exception {
		// ¡ÌGitπ§≥ÃGBRT¥¯UI
		Pair<FeatureIndex, List<Instance>> p = new CsvReader("D:/DataForMining/taolu/dataB28/taolu2rxian.train", ",").read();
		FeatureIndex featureIndex = p.first;
		List<Instance> samples = p.second;

		Pair<FeatureIndex, List<Instance>> pTest = new CsvReader("D:/DataForMining/taolu/dataB28/taolu2rxian.test", ",").read();
		List<Instance> samplesTest = pTest.second;

		GbdtClassifier model = new GbdtClassifier(
				new GbdtParamsBuilder(featureIndex).setTreeNum(10).setDepth(5).setLeafMinNum(5).setLearningRate(0.5)
						.setLoss("log").setCriterion("mse").setSplitter("sort").setThreadNum(4));

		model.fit(samples);

		int countTotal = 0;
		int countRight = 0;
		for (Instance i : samplesTest) {
			int truePredict = -1;
			model.predict(i);
			System.out.println(i.toString() + "|" + i.label + "|" + model.predict(i) + "\n");

			// corret
			if (model.predict(i) > 0.55) {
				truePredict = 1;
				countTotal++;
			} else if (model.predict(i) < 0.45) {
				truePredict = 0;
				countTotal++;
			}
			if (truePredict == i.label) {
				countRight++;
			}
		}
		System.out.println("Correctness:" + Float.valueOf(countRight) / countTotal);
		System.out.println("===============");
		// AUC
		System.out.println(MathUtils.auc(samplesTest, model));
	}
}
