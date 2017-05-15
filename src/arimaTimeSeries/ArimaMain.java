package arimaTimeSeries;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ArimaMain {

	public static void main(String args[]) throws FileNotFoundException {
		Scanner ino = null;
		Scanner ina = null;
		String sourcefile = "D:/timeseries/test.txt";
		String sourcefileadd = "D:/timeseries/add.txt";
		// Êä³ö´ÎÊý
		int count = 10;
		try {
			FileWriter fw = new FileWriter(sourcefileadd);
			while (count-- != 0) {
				ArrayList<Double> arraylist = new ArrayList<Double>();
				ino = new Scanner(new File(sourcefile));
				ina = new Scanner(new File(sourcefileadd));
				while (ino.hasNext()) {
					arraylist.add(Double.parseDouble(ino.next()));
				}
				while (ina.hasNext()) {
					arraylist.add(Double.parseDouble(ina.next()));
				}
				double[] dataArray = new double[arraylist.size() - 1];
				for (int i = 0; i < arraylist.size() - 1; i++)
					dataArray[i] = arraylist.get(i);

				// System.out.println(arraylist.size());

				ARIMA arima = new ARIMA(dataArray);

				int[] model = arima.getARIMAmodel();
				System.out.println("Best model is [p,q]=" + "[" + model[0] + " " + model[1] + "]");
				System.out.println("Predict value=" + arima.aftDeal(arima.predictValue(model[0], model[1])));
				fw.write(arima.aftDeal(arima.predictValue(model[0], model[1])) + "\n");
				System.out.println("Predict error="
						+ (arima.aftDeal(arima.predictValue(model[0], model[1])) - arraylist.get(arraylist.size() - 1))
								/ arraylist.get(arraylist.size() - 1) * 100
						+ "%");

				// System.out.println("##"+(arima.aftDeal(arima.predictValue(model[0],model[1]))));
				// String[] str = (String[])list1.toArray(new String[0]);
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			ino.close();
			ina.close();
		}
	}
}
