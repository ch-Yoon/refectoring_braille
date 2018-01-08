package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;
import com.project.why.braillelearning.R;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-11-16.
 */

public class SpeechRecognitionMoudle implements SpeechRecognizeListener {
    private SpeechRecognitionListener listener;
    private SpeechRecognizerClient client;
    private MediaSoundManager mediaSoundManager;
    private boolean taskCheck = false;
    private boolean stop = false;

    public SpeechRecognitionMoudle(Context context, SpeechRecognitionListener listener){
        SpeechRecognizerManager.getInstance().initializeLibrary(context); // SDK 초기화
        this.listener = listener;
        mediaSoundManager = new MediaSoundManager(context);
        stop = false;
    }

    public void start(){
        try {
            if(client == null){
                if(taskCheck == false){
                    mediaSoundManager.start(R.raw.speechrecognition_start);
                    CheckMediaTask checkMediaTask = new CheckMediaTask();
                    checkMediaTask.execute();
                }
            }
        } catch (Exception e){
            Log.d("test", "error : "+e.getMessage());
            mediaSoundManager.start("speechrecognition_fail");
        }
    }

    public void start(String text){
        try {
            if(client == null){
                if(taskCheck == false){
                    text += ",confirm";
                    mediaSoundManager.start(text);
                    CheckMediaTask checkMediaTask = new CheckMediaTask();
                    checkMediaTask.execute();
                }
            }
        } catch (Exception e){
            Log.d("test", "error : "+e.getMessage());
            mediaSoundManager.start("speechrecognition_fail");
        }
    }

    private void startSpeechRecognition(){
        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WORD);

        client = builder.build();
        client.setSpeechRecognizeListener(this);
        client.startRecording(false);
    }

    public void stop(){
        stop = true;
        if(client != null)
            client.stopRecording();
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
        Log.d("test", "error : "+errorMsg);
        client = null;
        if(stop == false)
            mediaSoundManager.start("speechrecognition_fail");
        else
            stop = false;
        listener.speechRecogntionResult(null);
    }

    @Override
    public void onPartialResult(String partialResult) {

    }

    @Override
    public void onResults(Bundle results) {
        Log.d("test","onResult");
        client = null;
        if(stop == false) {
            ArrayList<String> sttArray = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
            listener.speechRecogntionResult(sttArray);
        } else
            stop = false;
    }

    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {

    }

    private class CheckMediaTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            final int MAX_COUNT = 200; // 최대 10초
            int count = 0;


            while(true){
                if(count <= MAX_COUNT) {
                    if((mediaSoundManager.getMediaQueueSize() == 0) && (mediaSoundManager.checkTTSPlaying() == false)){
                        if(mediaSoundManager.getMediaPlaying() == false)
                            return true;
                    } else {
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
            if(result == true) {
                Log.d("test","startRecogntion");
                startSpeechRecognition();
            } else
                onError(0,"SpeechRecognition error");
            taskCheck = false;
        }
    }
}


