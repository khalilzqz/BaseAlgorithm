package Weka;

import java.io.File;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

public class BasicZeroR {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Classifier classifier1;

		String modelPath1 = "D:/DataForMining/Weka/ZeroR.model";
		try {
			File inputFile = new File("D:/DataForMining/Weka/TItrain.csv");// ѵ�������ļ�
			ArffLoader atf = new ArffLoader();
			atf.setFile(inputFile);
			Instances instancesTrain = atf.getDataSet(); // ����ѵ���ļ�

			inputFile = new File("D:/DataForMining/Weka/TItest.csv");// ���������ļ�
			atf.setFile(inputFile);
			Instances instancesTest = atf.getDataSet(); // ��������ļ�

			// ��ʹ������֮ǰһ��Ҫ��������instances��classIndex��������ʹ��instances�����ǻ��׳��쳣
			instancesTrain.setClassIndex(instancesTrain.numAttributes() - 1);
			instancesTest.setClassIndex(instancesTest.numAttributes() - 1);

			// ��ʼ��������
			// ����ʹ����һ���ض��ķ���������ѡ���뽫�ض���������class���Ʒ���forName����,�����͹�����һ���򵥵ķ�����
			// Zero
			classifier1 = (Classifier) Class.forName("weka.classifiers.rules.ZeroR").newInstance();
			classifier1.buildClassifier(instancesTrain);

			// ����ͼ��ط�����ģ�Ͳ���
			SerializationHelper.write(modelPath1, classifier1);

			// ����ϵͳ
			Evaluation eval = new Evaluation(instancesTrain);
			eval.evaluateModel(classifier1, instancesTest);
			System.out.println(eval.errorRate());

			// ʮ��������֤
			Classifier classifier2 = (Classifier) weka.core.SerializationHelper.read(modelPath1);
			Evaluation eval10 = new Evaluation(instancesTrain);
			eval10.crossValidateModel(classifier2, instancesTrain, 10, new Random(1));
			System.out.println(eval.errorRate());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
