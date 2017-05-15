package Adaboost;

import java.util.List;

public interface Classifier {

    public void train(List<Instance> trainingSet);

    public int predict(List<Double> features);

}
