package com.project.why.braillelearning.LearningControl;

import android.view.MotionEvent;

import com.project.why.braillelearning.LearningView.ViewObservers;

/**
 * Created by hyuck on 2017-09-25.
 */

/**
 * 점자 학습 control interface
 * observer를 등록하고, touchEvent를 통해 손가락 1개, 2개, 3개에 대한 event를 관리
 */
public interface Control {
    void addObservers(ViewObservers observers);
    void nodifyObservers();
    void onPause();
    void touchEvent(MotionEvent event);
}
