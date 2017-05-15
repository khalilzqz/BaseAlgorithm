package Weka;

import java.io.File;

import weka.core.Instances;
import weka.core.Instance;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.PrincipalComponents;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.Standardize;
import weka.attributeSelection.*;
import weka.core.*;
import weka.core.converters.ConverterUtils.*;
import weka.classifiers.*;
import weka.classifiers.meta.*;
import weka.classifiers.trees.*;
import weka.filters.*;

import java.util.*;

public class AttributeManage {

	/**
	 * takes a dataset as first argument
	 *
	 * @param args
	 *            the commandline arguments
	 * @throws Exception
	 *             if something goes wrong
	 */
	public static void main(String[] args) throws Exception {
		// 读取load data csv/arff
		DataSource source = new DataSource("D:/DataForMining/Weka/TItrain.test1.csv");
		Instances instancesTrain = source.getDataSet();

		// File TrainFile = new File("D:/DataForMining/Weka/TItrain.test1.csv");
		// ArffLoader atf = new ArffLoader();
		// atf.setFile(TrainFile);
		// Instances instancesTrain = atf.getDataSet();

		// 设定类别位置
		// instancesTrain.setClassIndex(0);

		// 输出一列数据
		// System.out.println(instancesTrain.attributeToDoubleArray(2)[0]);

		// 删除数据
		// instancesTrain.deleteAttributeAt(5);
		// 注意序号是删除之后的
		// instancesTrain.deleteAttributeAt(5);

		// 增加attribute
		// Add filter = new Add();
		// filter.setAttributeIndex("last");
		// filter.setAttributeName("NewNumeric");
		// Instances newData = new Instances(instancesTrain);
		// filter.setInputFormat(newData);
		// newData = Filter.useFilter(newData, filter);
		// for (int i = 0; i < newData.numInstances(); i++) {
		// newData.instance(i).setValue(newData.attribute("NewNumeric"),
		// instancesTrain.attributeToDoubleArray(4)[i] * 2);
		// }

		// 标准化
		// Normalize n = new Normalize();
		// n.setInputFormat(instancesTrain);
		// Instances instancesTrain2 = Filter.useFilter(instancesTrain, n);

		// 离散化
		// Discretize discretize = new Discretize();
		// String[] options = new String[6];
		// options[0] = "-B";
		// options[1] = "8";
		// options[2] = "-M";
		// options[3] = "-1.0";
		// options[4] = "-R";
		// options[5] = "6";
		// discretize.setOptions(options);
		// discretize.setInputFormat(instancesTrain);
		// Instances instancesTrain2 = Filter.useFilter(instancesTrain,
		// discretize);

		// 补充缺失ReplaceMissingValues
		Instances newData = new Instances(instancesTrain);
		// ReplaceMissingValues filter = new ReplaceMissingValues();
		// filter.setInputFormat(newData);
		// newData = Filter.useFilter(newData, filter);

		// 标准化Standardize
		newData.deleteAttributeAt(1);
		Standardize filter = new Standardize();
		filter.setInputFormat(newData);
		newData = Filter.useFilter(newData, filter);

		// 保存数据
		// ArffSaver saver = new ArffSaver();
		// saver.setInstances(newData);
		// saver.setFile(new File("D:/DataForMining/Weka/out.test"));
		// saver.writeBatch();

		// 改进其中一列并加入到原来的数据当中
		Add filter1 = new Add();
		filter1.setAttributeIndex("last");
		filter1.setAttributeName("NewNumeric");
		Instances newData1 = new Instances(instancesTrain);
		filter1.setInputFormat(instancesTrain);
		newData1 = Filter.useFilter(instancesTrain, filter1);
		for (int i = 0; i < newData1.numInstances(); i++) {
			newData1.instance(i).setValue(newData1.attribute("NewNumeric"), newData.attributeToDoubleArray(1)[i]);
		}

		System.out.println(newData1);

	}
}
