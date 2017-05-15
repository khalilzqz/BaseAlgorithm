package lightGBM;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class UseageTest {
	// your model path
	private static String modelPath = "LightGBM_model.txt";

	@Test
	public void test() throws FileNotFoundException, IOException {
		String path = UseageTest.class.getClassLoader().getResource(modelPath).getPath();
		// your model path
		path = URLDecoder.decode(path, "utf8");

		Boosting boosting = Boosting.createBoosting(path);
		// predict config, just like predict.conf in LightGBM
		Map<String, String> map = new HashMap<String, String>();
		OverallConfig config = new OverallConfig();
		config.set(map);
		Predictor predictor = new Predictor(boosting, config.getIoConfig().isPredictRawScore(),
				config.getIoConfig().isPredictLeafIndex());

		// your data to predict
		int[] indices = { 2, 6, 9 };
		double[] values = { 0.2, 0.4, 0.7 };

		SparseVector v = new SparseVector(values, indices);
		List<Double> predicts = predictor.predict(v);
		System.out.println("predict values " + predicts.toString());

	}

}
