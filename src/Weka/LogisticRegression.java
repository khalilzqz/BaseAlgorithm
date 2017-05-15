package Weka;

import java.io.File;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.Logistic;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;

public class LogisticRegression {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		ArffLoader atf = new ArffLoader(); // Reads a source that is in arff
											// (attribute relation file format)
											// format.

		File inputFile = new File("D:/Classify/diagnosis_part1.arff");// ����ѵ���ļ�
		atf.setFile(inputFile);
		Instances instancesTrain = atf.getDataSet(); // �õ���ʽ����ѵ������

		// ����ļ�����������Զ������������ ��������һ��һ���������ȹ��˵�һ��
		String[] removeOptions = new String[2];
		removeOptions[0] = "-R"; // "range"
		removeOptions[1] = "8"; // 8th attributeȥ����8 ��
		Remove remove1 = new Remove(); // new instance of filter
		remove1.setOptions(removeOptions); // set options
		remove1.setInputFormat(instancesTrain);
		Instances newInstancesTrain1 = Filter.useFilter(instancesTrain, remove1); // �õ��µ�����

		newInstancesTrain1.setClassIndex(newInstancesTrain1.numAttributes() - 1);// �������λ��

		inputFile = new File("D:/Classify/diagnosis_part2.arff");// ��������ļ�
		atf.setFile(inputFile);
		Instances instancesTest = atf.getDataSet(); // �õ���ʽ���Ĳ�������
		remove1.setInputFormat(instancesTest);
		Instances newInstancesTest1 = Filter.useFilter(instancesTest, remove1);// �õ��µĲ�������

		newInstancesTest1.setClassIndex(newInstancesTest1.numAttributes() - 1); // ���÷������������кţ���һ��Ϊ0�ţ���instancesTest.numAttributes()����ȡ����������

		Logistic m_classifier = new Logistic();// Logistic���Խ���һ���߼��ع������
		String options[] = new String[4];// ѵ����������
		options[0] = "-R";// cost�����е�Ԥ����� Ӱ��cost�����в�����ģ���ı���
		options[1] = "1E-5";// ��Ϊ1E-5
		options[2] = "-M";// ����������
		options[3] = "10";// ����������10��
		m_classifier.setOptions(options);

		m_classifier.buildClassifier(newInstancesTrain1); // ѵ��

		Evaluation eval = new Evaluation(newInstancesTrain1); // ����������
		eval.evaluateModel(m_classifier, newInstancesTest1);// �ò������ݼ�������m_classifier
		System.out.println("Logistic Regression on Evaluating Inflammation of urinary bladder");
		// System.out.println(eval.toSummaryString("=== Summary ===\n",false));
		// //�����Ϣ
		System.out.println(eval.toMatrixString("=== Confusion Matrix ===\n"));// Confusion
																				// Matrix

		// �Եڶ������� Nephritis of renal pelvis origin �����߼��ع�
		Remove remove2 = new Remove(); // new instance of filter
		removeOptions[0] = "-R"; // "range"
		removeOptions[1] = "7"; // 7th attribute
		remove2.setOptions(removeOptions);
		remove2.setInputFormat(instancesTrain);
		Instances newInstancesTrain2 = Filter.useFilter(instancesTrain, remove2); // �õ��µ�����

		newInstancesTrain2.setClassIndex(newInstancesTrain2.numAttributes() - 1);// �������λ��

		// System.out.println(newInstancesTrain2.numAttributes()+"ssss");

		remove2.setInputFormat(instancesTest);
		Instances newInstancesTest2 = Filter.useFilter(instancesTest, remove2);
		newInstancesTest2.setClassIndex(newInstancesTest2.numAttributes() - 1); // ���÷������������кţ���һ��Ϊ0�ţ���instancesTest.numAttributes()����ȡ����������

		Logistic m_classifier2 = new Logistic();// Logistic���Խ���һ���߼��ع������
		m_classifier2.setOptions(options);

		m_classifier2.buildClassifier(newInstancesTrain2); // ѵ��

		Evaluation eval2 = new Evaluation(newInstancesTrain2); // ����������
		eval2.evaluateModel(m_classifier2, newInstancesTest2);// �ò������ݼ�������m_classifier
		System.out.println("Logistic Regression on Evaluating Nephritis of renal pelvis origin");
		// System.out.println(eval2.toSummaryString("=== Summary ===\n",false));
		// //�����Ϣ
		System.out.println(eval2.toMatrixString("=== Confusion Matrix ===\n"));// Confusion
																				// Matrix
	}

}
