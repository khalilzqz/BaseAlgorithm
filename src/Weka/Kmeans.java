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
			// ������������
			File file = new File("D:/Weka/DataForMining/wekatest.txt");
			ArffLoader loader = new ArffLoader();
			loader.setFile(file);
			ins = loader.getDataSet();

			// ��ʼ�������� �������㷨��
			KM = new SimpleKMeans();
			KM.setNumClusters(5); // ���þ���Ҫ�õ����������
			KM.buildClusterer(ins); // ��ʼ���о���
			System.out.println(KM.preserveInstancesOrderTipText());
			// ��ӡ������
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