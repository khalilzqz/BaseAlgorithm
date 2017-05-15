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
		//不能有numical变量
		File inputFile = new File("D:/DataForMining/Weka/diagnosis_part2.arff");// 训练语料文件
		ArffLoader atf = new ArffLoader();
		atf.setFile(inputFile);
		Instances insTrain = atf.getDataSet(); // 读入训练文件

		inputFile = new File("D:/DataForMining/Weka/diagnosis_part2.arff");// 测试语料文件
		atf.setFile(inputFile);
		Instances insTest = atf.getDataSet(); // 读入测试文件
		
		insTrain.setClassIndex(0);
		insTest.setClassIndex(0);

		BayesNet bn = new BayesNet();

		bn.setDebug(false);// 控制打印信息（无额外信息输出）
		
		bn.buildClassifier(insTrain);
		Evaluation eval = new Evaluation(insTrain);
		eval.evaluateModel(bn, insTest);
		System.out.println("平均绝对误差：" + eval.meanAbsoluteError());// 越小越好
		System.out.println("均方根误差：" + eval.rootMeanSquaredError());// 越小越好
		System.err.println("是否准确的参考值：" + eval.meanAbsoluteError());// 越小越好

		double sum = insTest.numInstances();// 获取预测实例的总数
		for (int i = 0; i < sum; i++) {//
			// <---输出预测数据--->
			System.out.println(insTest.instance(i).value(0) + " : " + bn.classifyInstance(insTest.instance(i)));
		}
	}
}
