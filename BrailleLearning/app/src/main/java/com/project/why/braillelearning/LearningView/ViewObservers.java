package com.project.why.braillelearning.LearningView;

import android.view.View;

import com.project.why.braillelearning.LearningModel.BrailleData;

/**
 * Created by hyuck on 2017-09-26.
 */

/**
 * 점자 data를 관찰하기 위한 observer interface
 */
public interface ViewObservers {
    void nodifyBraille(BrailleData brailleData);
    View getView();
    void onPause();
}
