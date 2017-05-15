package lightGBM;

import java.util.List;

/**
 * @author lyg5623
 */
public abstract class PredictFunction {
    public abstract List<Double> predict(SparseVector vector);
}
