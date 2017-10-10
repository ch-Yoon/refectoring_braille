package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.LearningModel.BasicLearningData;

/**
 * Created by User on 2017-10-09.
 */

public interface OneFingerFunction {
    void initCoordinate();
    void oneFingerFunction(BasicLearningData data, float downX, float downY);
}
