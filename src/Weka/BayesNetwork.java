package Weka;

import java.io.File;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.net.BayesNetGenerator;
import weka.classifiers.evaluation.Evaluation;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSource;


public class BayesNetwork {
	public static void main(String[] args) throws Exception {
		//������numical����
		File inputFile = new File("D:/DataForMining/Weka/diagnosis_part2.arff");// ѵ�������ļ�
		ArffLoader atf = new ArffLoader();
		atf.setFile(inputFile);
		Instances insTrain = atf.getDataSet(); // ����ѵ���ļ�

		inputFile = new File("D:/DataForMining/Weka/diagnosis_part2.arff");// ���������ļ�
		atf.setFile(inputFile);
		Instances insTest = atf.getDataSet(); // ��������ļ�
		
		insTrain.setClassIndex(0);
		insTest.setClassIndex(0);

		BayesNet bn = new BayesNet();

		bn.setDebug(false);// ���ƴ�ӡ��Ϣ���޶�����Ϣ�����
		
		bn.buildClassifier(insTrain);
		Evaluation eval = new Evaluation(insTrain);
		eval.evaluateModel(bn, insTest);
		System.out.println("ƽ��������" + eval.meanAbsoluteError());// ԽСԽ��
		System.out.println("��������" + eval.rootMeanSquaredError());// ԽСԽ��
		System.err.println("�Ƿ�׼ȷ�Ĳο�ֵ��" + eval.meanAbsoluteError());// ԽСԽ��

		double sum = insTest.numInstances();// ��ȡԤ��ʵ��������
		for (int i = 0; i < sum; i++) {//
			// <---���Ԥ������--->
			System.out.println(insTest.instance(i).value(0) + " : " + bn.classifyInstance(insTest.instance(i)));
		}
	}
}
