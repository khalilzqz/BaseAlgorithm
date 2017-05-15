package Weka;

import java.io.File;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

public class NaiveBayes {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String modelPath1 = "D:/DataForMining/taolu/taolu2.model";
		try {
			File TrainFile = new File("D:/DataForMining/taolu/dataB28/taolu2ryesOrno.train");// ѵ�������ļ�
			ArffLoader atf = new ArffLoader();
			atf.setFile(TrainFile);
			Instances instancesTrain = atf.getDataSet(); // ����ѵ���ļ�

			File TestFile = new File("D:/DataForMining/taolu/dataB28/taolu2ryesOrno.test");// ���������ļ�
			ArffLoader atft = new ArffLoader();
			atft.setFile(TestFile);
			Instances instancesTest = atft.getDataSet(); // ��������ļ�

			// ��ʹ������֮ǰһ��Ҫ��������instances��classIndex��������ʹ��instances�����ǻ��׳��쳣
			instancesTrain.setClassIndex(instancesTrain.numAttributes() - 1);
			instancesTest.setClassIndex(instancesTest.numAttributes() - 1);

			// ��ʼ��������
			// ����ʹ����һ���ض��ķ���������ѡ���뽫�ض���������class���Ʒ���forName����,�����͹�����һ���򵥵ķ�����
			// Zero
			Classifier classifier1 = (Classifier) Class.forName("weka.classifiers.bayes.NaiveBayes").newInstance();
			// Logistic classifier1 = new Logistic();
			classifier1.buildClassifier(instancesTrain);

			// ����ͼ��ط�����ģ�Ͳ���
			SerializationHelper.write(modelPath1, classifier1);

			// ����ϵͳ
			Evaluation testingEvaluation = new Evaluation(instancesTrain);
			testingEvaluation.evaluateModel(classifier1, instancesTest);
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
			// ʮ��������֤
			// Classifier classifier2 = (Classifier)
			// weka.core.SerializationHelper.read(modelPath1);
			// Evaluation eval10 = new Evaluation(instancesTrain);
			// eval10.crossValidateModel(classifier2, instancesTrain, 10, new
			// Random(1));
			// System.out.println(eval.errorRate());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
