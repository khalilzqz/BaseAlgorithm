package CNNTest;

import CNNSource.CNN;
import CNNSource.CNN.LayerBuilder;
import CNNSource.ConcurentRunner;
import CNNSource.DataSet;
import CNNSource.Layer;
import CNNSource.Layer.Size;

public class CNNMnist {

	private static final String MODEL_NAME = "D:/DataForMining/NN/model.cnn";

	private static final String TRAIN_DATA = "D:/DataForMining/NN/lo.r.train";

	private static final String TEST_DATA = "D:/DataForMining/NN/lo.r.test";

	private static final String TEST_PREDICT = "D:/DataForMining/NN/test.predict";

	public static void main(String[] args) {

		System.err.println("ѵ���׶Σ�");
		runTrain();
		System.err.println("���Խ׶Σ�");
		runTest();
		ConcurentRunner.stop();

	}

	public static void runTrain() {
		// ���������νṹ
		LayerBuilder builder = new LayerBuilder();
		builder.addLayer(Layer.buildInputLayer(new Size(28, 28))); // ��������map��СΪ28��28
		builder.addLayer(Layer.buildConvLayer(6, new Size(5, 5))); // ��������map��СΪ24��24,24=28+1-5
		builder.addLayer(Layer.buildSampLayer(new Size(2, 2))); // ���������map��СΪ12��12,12=24/2
		builder.addLayer(Layer.buildConvLayer(12, new Size(5, 5))); // ��������map��СΪ8��8,8=12+1-5
		builder.addLayer(Layer.buildSampLayer(new Size(2, 2))); // ���������map��СΪ4��4,4=8/2
		builder.addLayer(Layer.buildOutputLayer(10));
		CNN cnn = new CNN(builder, 10);
		// ����ѵ������
		DataSet dataset = DataSet.load(TRAIN_DATA, ",", 71);
		// ��ʼѵ��ģ��
		cnn.train(dataset, 5);
		// ����ѵ���õ�ģ��
		cnn.saveModel(MODEL_NAME);
		dataset.clear();
	}

	public static void runTest() {
		// ����ѵ���õ�ģ��
		CNN cnn = CNN.loadModel(MODEL_NAME);
		// ���ز�������
		DataSet testSet = DataSet.load(TEST_DATA, ",", -1);
		// Ԥ����
		cnn.predict(testSet, TEST_PREDICT);
		testSet.clear();
	}

}
