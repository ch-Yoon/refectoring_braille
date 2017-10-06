package com.project.why.braillelearning.LearningView;

import android.view.View;

import com.project.why.braillelearning.LearningControl.Data;
import com.project.why.braillelearning.LearningModel.BrailleData;

/**
 * Created by hyuck on 2017-09-26.
 */

public interface ViewObservers {
    void initCircle(float bigCircle, float miniCircle);
    void nodifyBraille(Data data);
    View getView();
}
