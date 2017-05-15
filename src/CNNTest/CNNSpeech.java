package CNNTest;

import CNNSource.CNN;
import CNNSource.CNN.LayerBuilder;
import CNNSource.ConcurentRunner;
import CNNSource.DataSet;
import CNNSource.Layer;
import CNNSource.Layer.Size;

public class CNNSpeech {

	private static final String MODEL_NAME = "D:/NN/model.cnn";

	private static final String TRAIN_DATA = "D:/NN/train.format";

	private static final String TEST_DATA = "D:/NN/test.format";

	private static final String TEST_PREDICT = "D:/NN/test.predict";

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
		builder.addLayer(Layer.buildInputLayer(new Size(6, 6)));
		builder.addLayer(Layer.buildConvLayer(2, new Size(3, 3)));
		builder.addLayer(Layer.buildSampLayer(new Size(2, 2)));
		// builder.addLayer(Layer.buildConvLayer(3, new Size(3, 3)));
		// builder.addLayer(Layer.buildSampLayer(new Size(2, 2)));
		builder.addLayer(Layer.buildOutputLayer(2));
		CNN cnn = new CNN(builder, 10);
		// ����ѵ������
		DataSet dataset = DataSet.load(TRAIN_DATA, ",", 36);
		// ��ʼѵ��ģ��
		cnn.train(dataset, 3);
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
