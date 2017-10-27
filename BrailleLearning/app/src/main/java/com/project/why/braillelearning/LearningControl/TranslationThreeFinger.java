package com.project.why.braillelearning.LearningControl;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;

import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.impl.util.PermissionUtils;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.LearningModel.BrailleData;

import java.util.ArrayList;


/**
 * Created by hyuck on 2017-10-16.
 */

public class TranslationThreeFinger implements ThreeFIngerFunction, SpeechRecognizeListener{
    private Activity mActivity;
    private Context mContext;
    private BrailleTranslationLearning brailleTranslationLearning;
    private SpeechRecognizerClient client;

    TranslationThreeFinger(Context context, Activity activity){
        mActivity = activity;
        mContext = context;
        brailleTranslationLearning = new BrailleTranslationLearning(context);
        SpeechRecognizerManager.getInstance().initializeLibrary(context); // SDK 초기화

    }

    @Override
    public BrailleData getThreeFingerFunctionType(int[] downX, int[] downY, int[] upX, int[] upY) {
        if(PermissionUtils.checkAudioRecordPermission(mActivity)) {

            SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                    setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WEB);

            client = builder.build();

            client.setSpeechRecognizeListener(this);
            client.startRecording(true);
        }


//        if(PermissionUtils.checkAudioRecordPermission(this)) {
//
//            SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
//                    setServiceType(serviceType);
//
//            if (serviceType.equals(SpeechRecognizerClient.SERVICE_TYPE_WORD)) {
//                EditText words = (EditText)findViewById(R.id.words_edit);
//                String wordList = words.getText().toString();
//                builder.setUserDictionary(wordList);
//
//                Log.i("SpeechSampleActivity", "word list : " + wordList.replace('\n', ','));
//            }
//
//            client = builder.build();
//
//            client.setSpeechRecognizeListener(this);
//            client.startRecording(true);
//
//            setButtonsStatus(false);
//        }


//        String letterName = "연습";
//        int brailleMatrix[][] = brailleTranslationLearning.translation(letterName);
//        String ttsText = brailleTranslationLearning.getBrailleToText(letterName, brailleMatrix);
//        BrailleData brailleData = new BrailleData(letterName, brailleMatrix, null, ttsText);
//        return brailleData;
        return null;
    }

    @Override
    public FingerFunctionType getType() {
        return FingerFunctionType.TRANSLATION;
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        client = null;
        Log.d("NewTone errorCode : ", errorCode+"");
    }

    @Override
    public void onPartialResult(String partialResult) {

    }

    @Override
    public void onResults(Bundle results) {
        final StringBuilder builder = new StringBuilder();
        Log.i("SpeechSampleActivity", "onResults");

        ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
    }

    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {

    }
}
