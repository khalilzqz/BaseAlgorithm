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
			File TrainFile = new File("D:/DataForMining/taolu/dataB28/taolu2ryesOrno.train");// 训练语料文件
			ArffLoader atf = new ArffLoader();
			atf.setFile(TrainFile);
			Instances instancesTrain = atf.getDataSet(); // 读入训练文件

			File TestFile = new File("D:/DataForMining/taolu/dataB28/taolu2ryesOrno.test");// 测试语料文件
			ArffLoader atft = new ArffLoader();
			atft.setFile(TestFile);
			Instances instancesTest = atft.getDataSet(); // 读入测试文件

			// 在使用样本之前一定要首先设置instances的classIndex，否则在使用instances对象是会抛出异常
			instancesTrain.setClassIndex(instancesTrain.numAttributes() - 1);
			instancesTest.setClassIndex(instancesTest.numAttributes() - 1);

			// 初始化分类器
			// 具体使用哪一种特定的分类器可以选择，请将特定分类器的class名称放入forName函数,这样就构建了一个简单的分类器
			// Zero
			Classifier classifier1 = (Classifier) Class.forName("weka.classifiers.bayes.NaiveBayes").newInstance();
			// Logistic classifier1 = new Logistic();
			classifier1.buildClassifier(instancesTrain);

			// 保存和加载分类器模型参数
			SerializationHelper.write(modelPath1, classifier1);

			// 评价系统
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
			System.out.println("分类器的正确率：" + (1 - testingEvaluation.errorRate()));
			// 十交叉自验证
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
