package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.LearningModel.BrailleData;

/**
 * Created by hyuck on 2017-10-16.
 */

public interface ThreeFIngerFunction {
    BrailleData getThreeFingerFunctionType(int downX[], int downY[], int upX[], int upY[]);
}
