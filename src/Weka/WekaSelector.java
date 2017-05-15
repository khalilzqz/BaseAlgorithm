package Weka;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.core.Instances;
import weka.core.converters.ArffLoader;

public class WekaSelector {
	private ArffLoader loader;
	private Instances dataSet;
	private File arffFile;
	private int sizeOfDataset;
	private int numOfOldAttributes;
	private int numOfNewAttributes;
	private int classIndex;
	private int[] selectedAttributes;

	public WekaSelector(File file) throws IOException {
		loader = new ArffLoader();
		arffFile = file;
		loader.setFile(arffFile);
		dataSet = loader.getDataSet();
		sizeOfDataset = dataSet.numInstances();
		numOfOldAttributes = dataSet.numAttributes();
		classIndex = numOfOldAttributes - 1;
		dataSet.setClassIndex(classIndex);
	}

	public void select() throws Exception {
		ASEvaluation evaluator = new CfsSubsetEval();
		ASSearch search = new BestFirst();
		AttributeSelection eval = null;

		eval = new AttributeSelection();
		eval.setEvaluator(evaluator);
		eval.setSearch(search);

		eval.SelectAttributes(dataSet);
		numOfNewAttributes = eval.numberAttributesSelected();
		selectedAttributes = eval.selectedAttributes();
		System.out.println("result is " + eval.toResultsString());
		/*
		 * Random random = new Random(seed); dataSet.randomize(random); if
		 * (dataSet.attribute(classIndex).isNominal()) {
		 * dataSet.stratify(numFolds); } for (int fold = 0; fold < numFolds;
		 * fold++) { Instances train = dataSet.trainCV(numFolds, fold, random);
		 * eval.selectAttributesCVSplit(train); } System.out.println(
		 * "result is "+eval.CVResultsString());
		 */
		System.out.println("old number of Attributes is " + numOfOldAttributes);
		System.out.println("new number of Attributes is " + numOfNewAttributes);
		for (int i = 0; i < selectedAttributes.length; i++) {
			System.out.println(selectedAttributes[i]);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		File file = new File("D:/DataForMining/Weka/LotTest");
		try {
			WekaSelector ws = new WekaSelector(file);
			ws.select();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
