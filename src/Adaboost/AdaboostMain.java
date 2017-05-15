package Adaboost;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.apache.commons.cli.*;

public class AdaboostMain {

	public static void main(String[] args) {

		// For dataset:
		// "page-blocks" use m = 75
		// "glass" use m = 15
		// "pen-digits" use m = 100
		// "yeast" use m = 100
		// "optdigits" use m = 16
		// f The name of the data file
		// m Max number of different values per feature
		// n Number of NBC and DTC
		// p The percentage of the data set to be used for training
		// d The maximum depth for the DTCs [Default: 0 which means maximum
		// possible depth]
		// k Keep losers
		// 最后一位是label
		String filename = "D:/DataForMining/adaboost/part-r-00000.filter";
		int maxDifferentValuesPerFeature = 50;
		String[] classifiersCount = { "10", "0" };
		int dtcMaxDepth = 2;
		boolean keepLosers = false;
		String PERCENTAGE = "80";

		int nbcCount = 0;
		int dtcCount = 0;
		if (classifiersCount.length == 2) {
			nbcCount = Integer.parseInt(classifiersCount[0]);
			dtcCount = Integer.parseInt(classifiersCount[1]);
		} else {
			throw new IllegalArgumentException("Invalid number of NBCs or DTCs");
		}

		double trainTestRatio = Double.parseDouble(PERCENTAGE) / 100.0d;

		Injector injector = Guice.createInjector(new AdaboostModule());
		AppController learningController = injector.getInstance(AppController.class);
		learningController.start(filename, maxDifferentValuesPerFeature, trainTestRatio, nbcCount, dtcCount,
				dtcMaxDepth, keepLosers);
	}

}
