package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.LearningModel.BrailleData;

/**
 * Created by User on 2017-10-09.
 */

public interface SingleFingerFunction {
    void init();
    FingerFunctionType oneFingerFunction(BrailleData data, FingerCoordinate fingerCoordinate);
}
