package Weka;

import java.io.File;
import java.util.Random;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;

public class DecisionTreeC45 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Classifier classifier1;

		String modelPath1 = "D:/DataForMining/taolu/dataB28/C45.model";
		try {
			File inputFile = new File("D:/DataForMining/taolu/dataB28/taolu2ryesOrno.train");
			ArffLoader atf = new ArffLoader();
			atf.setFile(inputFile);
			Instances instancesTrain = atf.getDataSet(); // ����ѵ���ļ�

			inputFile = new File("D:/DataForMining/taolu/dataB28/taolu2ryesOrno.test");
			atf.setFile(inputFile);
			Instances instancesTest = atf.getDataSet(); // ��������ļ�
			
			// ��ʹ������֮ǰһ��Ҫ��������instances��classIndex��������ʹ��instances�����ǻ��׳��쳣
			instancesTrain.setClassIndex(0);
			instancesTest.setClassIndex(0);

			// ��ʼ��������
			// ����ʹ����һ���ض��ķ���������ѡ���뽫�ض���������class���Ʒ���forName����,�����͹�����һ���򵥵ķ�����
			classifier1 = (Classifier) Class.forName("weka.classifiers.trees.J48").newInstance();
			// ѵ��
			classifier1.buildClassifier(instancesTrain);
			// ����ͼ��ط�����ģ�Ͳ���
			SerializationHelper.write(modelPath1, classifier1);

			double sum = instancesTest.numInstances();
			float right = 0.0f;
			for (int i = 0; i < sum; i++)// ���Է�����
			{
				if (classifier1.classifyInstance(instancesTest.instance(i)) == instancesTest.instance(i).classValue())// ���Ԥ��ֵ�ʹ�ֵ��ȣ����������еķ������ṩ����Ϊ��ȷ�𰸣�����������壩
				{
					right++;// ��ȷֵ��1
				}
				System.out.println(instancesTest.instance(i).toString() + "|" + instancesTest.instance(i).classValue()
						+ "|" + classifier1.classifyInstance(instancesTest.instance(i)));
			}
			System.out.println("J48 classification precision:" + (right / sum));

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
