package com.project.why.braillelearning.LearningControl;

/**
 * Created by hyuck on 2018-02-06.
 */

/**
 * 특수기능 interface listener
 */
public interface SpecialFunctionListener {
    void onRefresh();
    void onSpeechRecognition();
    void onSaveMynote();
    void onDeleteMynote();
}
