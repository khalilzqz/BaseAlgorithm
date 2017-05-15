package Logistic_Regression;

import java.io.IOException;
import java.util.List;


public class Main {
	
	static String input = "D:/DataForMining/Logistic_Regression/spambase";
	
	public static void main(String[] args) throws IOException {
		
			tryfolds(input);

	}
	
	public static void tryfolds(String datasetName) throws IOException{
		int fold10 = 10;
		System.out.println("------"+datasetName+"-----");
		double[] mean = new double[fold10];
		for (int i = 1;i<=fold10;i++){
			int nooffeatures = DataReading.decideboundsandCreateData(datasetName);
			List<Record> traininginstances,testinstances;
			traininginstances = DataReading.readDataSet(datasetName+"_train",0);
			Logistic_Regression LR = new Logistic_Regression(nooffeatures);
			if(datasetName.contains("breastcancer")){
	    		LR.setIter(1000);
	    		LR.setRate(0.0001);
	    	}else if(datasetName.contains("diabetes")){
	    		LR.setIter(1000);
	    		LR.setRate(0.00003);
	    	} else if(datasetName.contains("spambase")){
	    		LR.setIter(4000);
	    		LR.setRate(0.0001);
	    	}
			LR.train(traininginstances,i);
			testinstances = DataReading.readDataSet(datasetName+"_test",0);
			double count = 0;
			for (int j=0; j<testinstances.size(); j++) {
                double[] x = testinstances.get(j).getX();
                double predicted = LR.classify(x);
                int pred=0;
                int label = testinstances.get(j).getLabel();
                if (predicted>=0.5){
                	pred = 1;
                }
                if (pred == label){
                	count++;
                }
			}
			mean[i-1] =  count/testinstances.size()*100;
			//System.out.println(count/testinstances.size()*100);
		}
		System.out.println("Mean : "+Util.mean(mean));
		System.out.println("Standard Deviation : "+Util.standardDeviation(mean));
		
	}
}
