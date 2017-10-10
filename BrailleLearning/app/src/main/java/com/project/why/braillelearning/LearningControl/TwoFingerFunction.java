package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.EnumConstant.FingerFunctionType;

/**
 * Created by User on 2017-10-09.
 */

public interface TwoFingerFunction {
    FingerFunctionType getTwoFingerFunctionType(int downX[], int downY[], int upX[], int upY[]);
}
