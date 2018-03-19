package com.project.why.braillelearning.SpeechRecognition;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-11-16.
 */

/**
 * 음성인식 모듈을 연결하기 위한 interface
 */
public interface SpeechRecognitionListener {
    void speechRecogntionResult(ArrayList<String> text);
}
