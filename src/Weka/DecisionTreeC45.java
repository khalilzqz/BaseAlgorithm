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
			Instances instancesTrain = atf.getDataSet(); // 读入训练文件

			inputFile = new File("D:/DataForMining/taolu/dataB28/taolu2ryesOrno.test");
			atf.setFile(inputFile);
			Instances instancesTest = atf.getDataSet(); // 读入测试文件
			
			// 在使用样本之前一定要首先设置instances的classIndex，否则在使用instances对象是会抛出异常
			instancesTrain.setClassIndex(0);
			instancesTest.setClassIndex(0);

			// 初始化分类器
			// 具体使用哪一种特定的分类器可以选择，请将特定分类器的class名称放入forName函数,这样就构建了一个简单的分类器
			classifier1 = (Classifier) Class.forName("weka.classifiers.trees.J48").newInstance();
			// 训练
			classifier1.buildClassifier(instancesTrain);
			// 保存和加载分类器模型参数
			SerializationHelper.write(modelPath1, classifier1);

			double sum = instancesTest.numInstances();
			float right = 0.0f;
			for (int i = 0; i < sum; i++)// 测试分类结果
			{
				if (classifier1.classifyInstance(instancesTest.instance(i)) == instancesTest.instance(i).classValue())// 如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）
				{
					right++;// 正确值加1
				}
				System.out.println(instancesTest.instance(i).toString() + "|" + instancesTest.instance(i).classValue()
						+ "|" + classifier1.classifyInstance(instancesTest.instance(i)));
			}
			System.out.println("J48 classification precision:" + (right / sum));

			// 评价系统
			Evaluation eval = new Evaluation(instancesTrain);
			eval.evaluateModel(classifier1, instancesTest);
			System.out.println(eval.errorRate());

			// 十交叉自验证
			Classifier classifier2 = (Classifier) weka.core.SerializationHelper.read(modelPath1);
			Evaluation eval10 = new Evaluation(instancesTrain);
			eval10.crossValidateModel(classifier2, instancesTrain, 10, new Random(1));
			System.out.println(eval.errorRate());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
