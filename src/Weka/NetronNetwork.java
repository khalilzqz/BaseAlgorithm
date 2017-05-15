package Weka;

import java.io.File;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class NetronNetwork {
	public static void main(String[] args) throws Exception {
		File inputFile = new File("D:/DataForMining/Weka/LotTrain");// ѵ�������ļ�
		ArffLoader atf = new ArffLoader();
		atf.setFile(inputFile);
		Instances insTrain = atf.getDataSet(); // ����ѵ���ļ�

		inputFile = new File("D:/DataForMining/Weka/LotTest");// ���������ļ�
		atf.setFile(inputFile);
		Instances insTest = atf.getDataSet(); // ��������ļ�

		insTrain.setClassIndex(0);
		insTest.setClassIndex(0);

		MultilayerPerceptron mp = new MultilayerPerceptron();
		// �Ƿ����ͼ�ν���
		mp.setGUI(false);
		// ���������е����Ӻ�����
		mp.setAutoBuild(true);
		// ���ƴ�ӡ��Ϣ
		mp.setDebug(false);
		// ���Ϊtrue�ή��ѧϰ����
		mp.setDecay(false);
		// ��Ԥ��������ûӰ��
		mp.setHiddenLayers("a");
		// Weights�����µ�����,��Ԥ����Ӱ��ܴ�
		mp.setLearningRate(0.3);
		// ������weightsʱ���õĶ���
		mp.setMomentum(0.8);
		// �����Ż���������
		mp.setNormalizeAttributes(true);
		// ���Ԥ�������ֵ�Ϳ���������������
		mp.setNormalizeNumericClass(true);
		// ����Ҫ��AutoBuildΪtrue�������½������÷���Ĭ�ϼ���
		mp.setReset(false);
		// �������������Ԥ����Ӱ���
		mp.setSeed(0);
		// �����Ĵ���,��һ��Ӱ�죬���ǲ���
		mp.setTrainingTime(300);
		// ��֤�ٷֱȣ�Ӱ���
		mp.setValidationSetSize(20);
		// ����û��Ӱ��
		mp.setValidationThreshold(50);
		// �����������
		mp.setNominalToBinaryFilter(true);
		
		mp.buildClassifier(insTrain);
		Evaluation eval = new Evaluation(insTrain);
		eval.evaluateModel(mp, insTest);
		System.out.println("ƽ��������" + eval.meanAbsoluteError());// ԽСԽ��
		System.out.println("��������" + eval.rootMeanSquaredError());// ԽСԽ��
		System.out.println("�����ϵ��:" + eval.correlationCoefficient());// Խ�ӽ�1Խ��
		System.out.println("��������" + eval.rootMeanSquaredError());// ԽСԽ��
		System.err.println("�Ƿ�׼ȷ�Ĳο�ֵ��" + eval.meanAbsoluteError());// ԽСԽ��

		String model = mp.toString();

		double sum = insTest.numInstances();// ��ȡԤ��ʵ��������
		for (int i = 0; i < sum; i++) {
			// <---���Ԥ������--->
			System.out.println(insTest.instance(i).value(0) + " : " + mp.classifyInstance(insTest.instance(i)));
		}
		System.err.println("ErrorRate��" + eval.errorRate());// ?
	}
}
