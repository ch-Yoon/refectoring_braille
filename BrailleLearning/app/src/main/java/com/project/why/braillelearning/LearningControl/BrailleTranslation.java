package com.project.why.braillelearning.LearningControl;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.LearningModel.Dot;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;
import com.project.why.braillelearning.Module.BrailleTranslationModule;

import java.util.ArrayList;


/**
 * Created by hyuck on 2017-10-16.
 */

public class BrailleTranslation implements SpeechRecognition, SpeechRecognizeListener{
    private BrailleTranslationModule brailleTranslationLearning;
    private SpeechRecognizerClient client;
    private MediaSoundManager mediaSoundManager;
    private CallBack callBackMethod;

    BrailleTranslation(Context context){
        mediaSoundManager = new MediaSoundManager(context);
        brailleTranslationLearning = new BrailleTranslationModule(context);
        SpeechRecognizerManager.getInstance().initializeLibrary(context); // SDK 초기화
    }

    @Override
    public void startSpecialFunction(CallBack callBackMethod) {
        this.callBackMethod = callBackMethod;
        try {
            CheckMediaTask checkMediaTask = new CheckMediaTask();
            checkMediaTask.execute();
        } catch (Exception e){
            onError(0,"SpeechRecognition error");
        }
    }

    public void startSpeechRecognition(){
        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WORD);

        client = builder.build();
        client.setSpeechRecognizeListener(this);
        client.startRecording(false);
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
        mediaSoundManager.start("speechrecognition_fail");
    }

    @Override
    public void onPartialResult(String partialResult) {
    }

    @Override
    public void onResults(Bundle results) {
        client = null;

        ArrayList<String> sttArray = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
        String letterName = sttArray.get(0);
        startBrailleTranslation(letterName);
    }

    public void startBrailleTranslation(String letterName){
        BrailleData translationBrailleData = brailleTranslationLearning.translation(letterName);

        if(translationBrailleData != null)
            callBackMethod.objectCallBackMethod(translationBrailleData);
        else
            mediaSoundManager.start("brailletranslation_fail");
    }

    @Override
    public void onAudioLevel(float audioLevel) {
    }

    @Override
    public void onFinished() {
        Log.d("stt","stt finish");
        client = null;
    }

    private class CheckMediaTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final int MAX_COUNT = 100;
            int count = 0;

            while(true){
                if(count <= MAX_COUNT) {
                    if (mediaSoundManager.getMediaPlayer() == null)
                        return true;
                    else {
                        try {
                            count++;
                            Thread.sleep(50);
                        } catch (Exception e) {
                            Log.d("checkMediaThread", "Thread sleep error");
                            return false;
                        }
                    }
                } else
                    return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(result == true)
                startSpeechRecognition();
            else
                onError(0,"SpeechRecognition error");
            Log.d("asyncTask",result+"");
        }
    }
}







