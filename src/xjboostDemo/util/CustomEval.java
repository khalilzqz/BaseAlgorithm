package xjboostDemo.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dmlc.xgboost4j.DMatrix;
import org.dmlc.xgboost4j.IEvaluation;
import org.dmlc.xgboost4j.util.XGBoostError;

/**
 * a util evaluation class for examples
 * @author hzx
 */
public class CustomEval implements IEvaluation {
    private static final Log logger = LogFactory.getLog(CustomEval.class);

    String evalMetric = "custom_error";
        
    @Override
    public String getMetric() {
        return evalMetric;
    }

    @Override
    public float eval(float[][] predicts, DMatrix dmat) {
        float error = 0f;
        float[] labels;
        try {
            labels = dmat.getLabel();
        } catch (XGBoostError ex) {
            logger.error(ex);
            return -1f;
        }
        int nrow = predicts.length;
        for(int i=0; i<nrow; i++) {
            if(labels[i]==0f && predicts[i][0]>0.5) {
                error++;
            }
            else if(labels[i]==1f && predicts[i][0]<=0.5) {
                error++;
            }
        }
            
        return error/labels.length;
    }
}
