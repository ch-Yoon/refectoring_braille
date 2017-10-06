package com.project.why.braillelearning.LearningControl;

import android.view.MotionEvent;

import com.project.why.braillelearning.LearningView.ViewObservers;

/**
 * Created by hyuck on 2017-09-25.
 */

public interface FingerFunction {
    void addObservers(ViewObservers observers);
    void initObservers();
    void nodifyObservers();
    boolean oneFinegerFunction();
    boolean twoFingerFunction();
    boolean threeFingerFunction();
    boolean touchEvent(MotionEvent event);
}
