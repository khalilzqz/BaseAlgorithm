package RandomForest;

import java.util.ArrayList;
import java.util.HashMap;

//https://github.com/ironmanMA/Random-Forest
public class MainRun {
	public static void main(String[] args) {
		System.out.println("Random-Forest with Categorical support");
		System.out.println("Now Running");
		/*
		 * data has to be separated by either ',' or ' ' only...
		 */
		int categ = 1;
		String traindata, testdata;
		if (categ > 0) {
			traindata = "D:/DataForMining/RandomForest/KDDTest.txt";
			testdata = "D:/DataForMining/RandomForest/KDDTestSmall.txt";
		} else if (categ < 0) {
			traindata = "D:/DataForMining/RandomForest/Data.txt";
			testdata = "D:/DataForMining/RandomForest/Test.txt";
		} else {
			traindata = "D:/DataForMining/RandomForest/TItrain.csv";
			testdata = "D:/DataForMining/RandomForest/TItest.csv";
		}

		DescribeTreesCateg DT = new DescribeTreesCateg(traindata);
		ArrayList<ArrayList<String>> Train = DT.CreateInputCateg(traindata);
		ArrayList<ArrayList<String>> Test = DT.CreateInputCateg(testdata);
		/*
		 * For class-labels
		 */
		HashMap<String, Integer> Classes = new HashMap<String, Integer>();
		for (ArrayList<String> dp : Train) {
			String clas = dp.get(dp.size() - 1);
			if (Classes.containsKey(clas))
				Classes.put(clas, Classes.get(clas) + 1);
			else
				Classes.put(clas, 1);
		}

		// M是特征数，Ms是选择特征数，C是分类数量
		int numTrees = 15;
		int M = Train.get(0).size() - 1;
		int Ms = (int) Math.round(Math.log(M) / Math.log(2) + 1);
		int C = Classes.size();
		RandomForestCateg RFC = new RandomForestCateg(numTrees, M, Ms, C, Train, Test);
		RFC.Start();

	}
}
