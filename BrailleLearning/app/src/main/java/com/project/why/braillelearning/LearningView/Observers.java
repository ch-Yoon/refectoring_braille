package com.project.why.braillelearning.LearningView;

import android.view.View;

import com.project.why.braillelearning.LearningModel.BrailleData;

/**
 * Created by hyuck on 2017-09-26.
 */

public interface Observers {
    void nodifyBraille(BrailleData brailleData);
    View getView();
}
