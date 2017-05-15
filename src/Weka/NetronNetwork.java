package Weka;

import java.io.File;

import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class NetronNetwork {
	public static void main(String[] args) throws Exception {
		File inputFile = new File("D:/DataForMining/Weka/LotTrain");// 训练语料文件
		ArffLoader atf = new ArffLoader();
		atf.setFile(inputFile);
		Instances insTrain = atf.getDataSet(); // 读入训练文件

		inputFile = new File("D:/DataForMining/Weka/LotTest");// 测试语料文件
		atf.setFile(inputFile);
		Instances insTest = atf.getDataSet(); // 读入测试文件

		insTrain.setClassIndex(0);
		insTest.setClassIndex(0);

		MultilayerPerceptron mp = new MultilayerPerceptron();
		// 是否进行图形交互
		mp.setGUI(false);
		// 设置网络中的连接和隐层
		mp.setAutoBuild(true);
		// 控制打印信息
		mp.setDebug(false);
		// 如果为true会降低学习速率
		mp.setDecay(false);
		// 对预测结果几乎没影响
		mp.setHiddenLayers("a");
		// Weights被更新的数量,对预测结果影响很大
		mp.setLearningRate(0.3);
		// 当更新weights时设置的动量
		mp.setMomentum(0.8);
		// 可以优化网络性能
		mp.setNormalizeAttributes(true);
		// 如果预测的是数值型可以提高网络的性能
		mp.setNormalizeNumericClass(true);
		// 必须要在AutoBuild为true的条件下进行设置否则默认即可
		mp.setReset(false);
		// 随机种子数，对预测结果影响大
		mp.setSeed(0);
		// 迭代的次数,有一定影响，但是不大
		mp.setTrainingTime(300);
		// 验证百分比，影响大
		mp.setValidationSetSize(20);
		// 几乎没用影响
		mp.setValidationThreshold(50);
		// 可以提高性能
		mp.setNominalToBinaryFilter(true);
		
		mp.buildClassifier(insTrain);
		Evaluation eval = new Evaluation(insTrain);
		eval.evaluateModel(mp, insTest);
		System.out.println("平均绝对误差：" + eval.meanAbsoluteError());// 越小越好
		System.out.println("均方根误差：" + eval.rootMeanSquaredError());// 越小越好
		System.out.println("相关性系数:" + eval.correlationCoefficient());// 越接近1越好
		System.out.println("根均方误差：" + eval.rootMeanSquaredError());// 越小越好
		System.err.println("是否准确的参考值：" + eval.meanAbsoluteError());// 越小越好

		String model = mp.toString();

		double sum = insTest.numInstances();// 获取预测实例的总数
		for (int i = 0; i < sum; i++) {
			// <---输出预测数据--->
			System.out.println(insTest.instance(i).value(0) + " : " + mp.classifyInstance(insTest.instance(i)));
		}
		System.err.println("ErrorRate：" + eval.errorRate());// ?
	}
}
