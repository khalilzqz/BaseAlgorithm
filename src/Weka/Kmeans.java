package Weka;

import java.io.File;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class Kmeans {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Instances ins = null;

		SimpleKMeans KM = null;
		// Canopy CN = null;
		// DistanceFunction disFun = null;

		try {
			// 读入样本数据
			File file = new File("D:/Weka/DataForMining/wekatest.txt");
			ArffLoader loader = new ArffLoader();
			loader.setFile(file);
			ins = loader.getDataSet();

			// 初始化聚类器 （加载算法）
			KM = new SimpleKMeans();
			KM.setNumClusters(5); // 设置聚类要得到的类别数量
			KM.buildClusterer(ins); // 开始进行聚类
			System.out.println(KM.preserveInstancesOrderTipText());
			// 打印聚类结果
			System.out.println(KM.toString());

			// CN = new Canopy();
			// CN.setNumClusters(5);
			// CN.buildClusterer(ins);
			// System.out.println(CN.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}