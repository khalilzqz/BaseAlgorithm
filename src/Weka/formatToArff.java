package Weka;

import java.io.*;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/**
 * Created by Administrator on 2016/10/20.
 */
public class formatToArff {
	public static void main(String[] args) throws Exception {
		// load CSV
		CSVLoader loader = new CSVLoader();
		loader.setSource(new File("D:/DataForMining/Lot/yesNo"));
		Instances data = loader.getDataSet();

		// save ARFF
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File("D:/DataForMining/Lot/YesOrNo"));
		// Êä³öprint
		// saver.setDestination(new File("D:/DataForMining/Weka/test.arff"));
		saver.writeBatch();
	}

}