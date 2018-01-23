package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.LearningModel.Dot;

/**
 * Created by User on 2017-10-09.
 */

/**
 * 손가락 1개 event를 처리하기 위한 interface
 */
public interface SingleFingerFunction {
    Dot[][] oneFingerFunction(Dot[][] brailleMatrix, FingerCoordinate fingerCoordinate);
    void init();
}
