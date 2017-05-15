package Weka;

import java.io.File;

import weka.associations.Apriori;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;

public class Relation {

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		ArffLoader atf = new ArffLoader();
		File inputFile = new File("D:/DataForMining/Weka/LotTrain");
		atf.setFile(inputFile);
		Instances test = atf.getDataSet();
		// 获取对象的数据
		// test.setClassIndex(0);
		// 创建一个离散实例
		Discretize discretize = new Discretize();
		/*
		 * 离散化处理
		 */

		String[] options = new String[6];
		options[0] = "-B";
		options[1] = "8";
		options[2] = "-M";
		options[3] = "-1.0";
		options[4] = "-R";
		options[5] = "1-last";
		discretize.setOptions(options);
		discretize.setInputFormat(test);
		/*
		 * 获取离散化处理后的数据对象
		 */
		Instances newInstances2 = Filter.useFilter(test, discretize);
		newInstances2.setClassIndex(0);
		System.out.println(newInstances2);
		/*
		 * 创建Apriori实例
		 */
		Apriori apriori2 = new Apriori();
		apriori2.buildAssociations(newInstances2);
		System.out.println(apriori2.toString());

	}

}
