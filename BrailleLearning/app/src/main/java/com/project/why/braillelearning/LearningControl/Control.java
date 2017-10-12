package com.project.why.braillelearning.LearningControl;

import android.view.MotionEvent;

import com.project.why.braillelearning.LearningView.ViewObservers;

/**
 * Created by hyuck on 2017-09-25.
 */

public interface Control {
    void addObservers(ViewObservers observers);
    void initObservers();
    void nodifyObservers();
    void pause();
    boolean touchEvent(MotionEvent event);
}
