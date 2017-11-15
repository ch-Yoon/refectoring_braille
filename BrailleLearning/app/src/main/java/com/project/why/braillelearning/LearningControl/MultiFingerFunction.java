package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.EnumConstant.FingerFunctionType;

/**
 * Created by hyuck on 2017-10-31.
 */

/**
 * multi touch를 위한 interface
 */
public interface MultiFingerFunction {
    FingerFunctionType fingerFunctionType(FingerCoordinate fingerCoordinate);
}
