package Weka;

import java.io.File;
import java.io.FileWriter;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.meta.Vote;
import weka.classifiers.rules.ZeroR;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader;

public class EnsembleClassify {

	public static void main(String[] args) throws Exception {
		// LibSVM classifier = new LibSVM();
		File trainFile = new File("D:/DataForMining/taolu/dataB28/taolu2ryesOrno.train");
		File testFile = new File("D:/DataForMining/taolu/dataB28/taolu2ryesOrno.test");

		ArffLoader atf = new ArffLoader();
		atf.setFile(trainFile);
		Instances instancesTrain = atf.getDataSet(); // 读入训练文件
		atf.setFile(testFile);
		Instances instancesTest = atf.getDataSet(); // 读入测试文件

		// 在使用样本之前一定要首先设置instances的classIndex，否则在使用instances对象是会抛出异常
		instancesTrain.setClassIndex(instancesTrain.numAttributes() - 1);
		instancesTest.setClassIndex(instancesTest.numAttributes() - 1);
		// 初始化分类器
		// 具体使用哪一种特定的分类器可以选择，请将特定分类器的class名称放入forName函数,这样就构建了一个简单的分类器
		Classifier j48 = (Classifier) Class.forName("weka.classifiers.trees.J48").newInstance();
		Classifier naiveBayes = (Classifier) Class.forName("weka.classifiers.bayes.NaiveBayes").newInstance();
		Classifier ZeroR = (Classifier) Class.forName("weka.classifiers.rules.ZeroR").newInstance();
		// 训练
		// classifier1.buildClassifier(instancesTrain);
		// 2.1 设置集成分类器
		Classifier[] cfsArray = new Classifier[3];
		cfsArray[0] = j48;
		cfsArray[1] = naiveBayes;
		cfsArray[2] = ZeroR;

		/**
		 * 2.2 定制集成分类器的决策方式 AVERAGE_RULE PRODUCT_RULE MAJORITY_VOTING_RULE
		 * MIN_RULE MAX_RULE MEDIAN_RULE 它们具体的工作方式，参考weka的说明文档。
		 * 通常情况下选择的是多数投票的决策规则
		 */
		Vote ensemble = new Vote();
		SelectedTag tag = new SelectedTag(Vote.MAJORITY_VOTING_RULE, Vote.TAGS_RULES);
		ensemble.setCombinationRule(tag);
		ensemble.setClassifiers(cfsArray);
		// 设置随机数种子
		ensemble.setSeed(2);
		// 训练ensemble分类器
		ensemble.buildClassifier(instancesTrain);
		/**
		 * 5. 从工厂中获取使用Evaluation，测试样本测试分类器的学习效果
		 */
		FileWriter fw = new FileWriter("D:/DataForMining/Weka/WekaOutput.txt");
		// 评价系统
		Evaluation testingEvaluation = new Evaluation(instancesTrain);
		testingEvaluation.evaluateModel(ensemble, instancesTest);
		System.out.println(testingEvaluation.errorRate());

		double sum = instancesTrain.numInstances();
		int length = instancesTest.numInstances();
		for (int i = 0; i < length; i++) {
			// 通过这个方法来用每个测试样本测试分类器的效果
			testingEvaluation.evaluateModelOnceAndRecordPrediction(ensemble, instancesTest.instance(i));
		}
		double[][] confusionMatrix = testingEvaluation.confusionMatrix();
		for (int i = 0; i < confusionMatrix.length; i++) {
			double[] ds = confusionMatrix[i];
			for (int j = 0; j < ds.length; j++) {
				System.out.print(ds[j]);
			}
			System.out.println();
		}
		System.out.println("=======================");
		System.out.println(testingEvaluation.toSummaryString());
		System.out.println("=======================");
		System.out.println(testingEvaluation.toMatrixString());
		System.out.println("=======================");
		System.out.println(testingEvaluation.toClassDetailsString());
		System.out.println("=======================");
		System.out.println(testingEvaluation.toCumulativeMarginDistributionString());
		System.out.println("=======================");
		System.out.println(testingEvaluation.fMeasure(1) + " " + testingEvaluation.precision(1) + " "
				+ testingEvaluation.recall(1));
		System.out.println("=======================");
		System.out.println("分类器的正确率：" + (1 - testingEvaluation.errorRate()));

		fw.write("分类器的正确率：" + (1 - testingEvaluation.errorRate()) + "\n");

		fw.close();

	}
}
