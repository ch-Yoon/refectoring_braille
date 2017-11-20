package com.project.why.braillelearning.LearningControl;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-11-16.
 */

public interface SpeechRecognitionListener {
    void speechRecogntionResult(ArrayList<String> text);
}
