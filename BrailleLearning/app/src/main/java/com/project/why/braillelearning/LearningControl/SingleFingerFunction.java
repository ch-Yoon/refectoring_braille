package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.Dot;

/**
 * Created by User on 2017-10-09.
 */

public interface SingleFingerFunction {
    Dot[][] oneFingerFunction(Dot[][] brailleMatrix, FingerCoordinate fingerCoordinate);
    void init();
}
