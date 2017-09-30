package com.project.why.braillelearning.LearningControl;

import com.project.why.braillelearning.LearningView.Observers;

/**
 * Created by hyuck on 2017-09-25.
 */

public interface FingerFunction {
    void addObservers(Observers observers);
    void nodifyObservers();

    void oneFingerFunction();
    void twoFingerFunction();
    void threeFingerFunction();
}
