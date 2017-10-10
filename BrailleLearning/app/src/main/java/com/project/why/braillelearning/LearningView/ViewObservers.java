package com.project.why.braillelearning.LearningView;

import android.view.View;

import com.project.why.braillelearning.LearningModel.BasicLearningData;

/**
 * Created by hyuck on 2017-09-26.
 */

public interface ViewObservers {
    void initCircle(float bigCircle, float miniCircle);
    void nodifyBraille(BasicLearningData data);
    View getView();
}
