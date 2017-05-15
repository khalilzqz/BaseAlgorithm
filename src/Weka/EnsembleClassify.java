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
		Instances instancesTrain = atf.getDataSet(); // ����ѵ���ļ�
		atf.setFile(testFile);
		Instances instancesTest = atf.getDataSet(); // ��������ļ�

		// ��ʹ������֮ǰһ��Ҫ��������instances��classIndex��������ʹ��instances�����ǻ��׳��쳣
		instancesTrain.setClassIndex(instancesTrain.numAttributes() - 1);
		instancesTest.setClassIndex(instancesTest.numAttributes() - 1);
		// ��ʼ��������
		// ����ʹ����һ���ض��ķ���������ѡ���뽫�ض���������class���Ʒ���forName����,�����͹�����һ���򵥵ķ�����
		Classifier j48 = (Classifier) Class.forName("weka.classifiers.trees.J48").newInstance();
		Classifier naiveBayes = (Classifier) Class.forName("weka.classifiers.bayes.NaiveBayes").newInstance();
		Classifier ZeroR = (Classifier) Class.forName("weka.classifiers.rules.ZeroR").newInstance();
		// ѵ��
		// classifier1.buildClassifier(instancesTrain);
		// 2.1 ���ü��ɷ�����
		Classifier[] cfsArray = new Classifier[3];
		cfsArray[0] = j48;
		cfsArray[1] = naiveBayes;
		cfsArray[2] = ZeroR;

		/**
		 * 2.2 ���Ƽ��ɷ������ľ��߷�ʽ AVERAGE_RULE PRODUCT_RULE MAJORITY_VOTING_RULE
		 * MIN_RULE MAX_RULE MEDIAN_RULE ���Ǿ���Ĺ�����ʽ���ο�weka��˵���ĵ���
		 * ͨ�������ѡ����Ƕ���ͶƱ�ľ��߹���
		 */
		Vote ensemble = new Vote();
		SelectedTag tag = new SelectedTag(Vote.MAJORITY_VOTING_RULE, Vote.TAGS_RULES);
		ensemble.setCombinationRule(tag);
		ensemble.setClassifiers(cfsArray);
		// �������������
		ensemble.setSeed(2);
		// ѵ��ensemble������
		ensemble.buildClassifier(instancesTrain);
		/**
		 * 5. �ӹ����л�ȡʹ��Evaluation�������������Է�������ѧϰЧ��
		 */
		FileWriter fw = new FileWriter("D:/DataForMining/Weka/WekaOutput.txt");
		// ����ϵͳ
		Evaluation testingEvaluation = new Evaluation(instancesTrain);
		testingEvaluation.evaluateModel(ensemble, instancesTest);
		System.out.println(testingEvaluation.errorRate());

		double sum = instancesTrain.numInstances();
		int length = instancesTest.numInstances();
		for (int i = 0; i < length; i++) {
			// ͨ�������������ÿ�������������Է�������Ч��
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
		System.out.println("����������ȷ�ʣ�" + (1 - testingEvaluation.errorRate()));

		fw.write("����������ȷ�ʣ�" + (1 - testingEvaluation.errorRate()) + "\n");

		fw.close();

	}
}
