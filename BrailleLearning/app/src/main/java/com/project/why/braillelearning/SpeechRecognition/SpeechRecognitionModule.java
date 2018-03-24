package com.project.why.braillelearning.SpeechRecognition;

import android.content.Context;
import android.os.Bundle;
import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.project.why.braillelearning.MediaPlayer.MediaPlayerStopCallbackListener;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;
import com.project.why.braillelearning.R;
import java.util.ArrayList;

/**
 * Created by hyuck on 2017-11-16.
 */

/**
 * 음성인식 모듈 class
 */
public class SpeechRecognitionModule implements SpeechRecognizeListener, MediaPlayerStopCallbackListener {
    private SpeechRecognitionListener listener;
    private SpeechRecognizerClient client;
    private MediaSoundManager mediaSoundManager;
    private boolean stop = false;

    public SpeechRecognitionModule(Context context, SpeechRecognitionListener listener){
        SpeechRecognizerManager.getInstance().initializeLibrary(context); // SDK 초기화
        this.listener = listener;
        mediaSoundManager = new MediaSoundManager(context);

    }

    /**
     * 화면이 pause가 될때 연결되어 있는 callbacklistener를 해제
     * 음성인식이 실행중이라면 음성인식 중단
     */
    public void pause(){
        mediaSoundManager.initialMediaPlayerStopCallbackListener();
        stop();
    }

    /**
     * 음성인식 시작 함수
     * AsyncTask를 생성하여 처리
     */
    public void start(){
        try {
            synchronized (this) {
                if (client == null && (mediaSoundManager.getMediaPlaying() == false)) {
                    mediaSoundManager.setMediaPlayerStopCallbackListener(this);
                    mediaSoundManager.start(R.raw.speechrecognition_start);
                }
            }
        } catch (Exception e){
            listener.speechRecogntionResult(null);
        }
    }


    /**
     * 선생님과의 대화 메뉴를 위한 음성인식 시작 함수
     * 가이드 음성 출력 후 음성 종료 후 음성인식 시작
     * @param text : 사용자가 음성인식한 글자
     */
    public void start(String text){
        try {
            synchronized (this) {
                if (client == null && (mediaSoundManager.getMediaPlaying() == false)) {
                    mediaSoundManager.setMediaPlayerStopCallbackListener(this);
                    text += ",confirm";
                    mediaSoundManager.start(text);
                }
            }
        } catch (Exception e){
            listener.speechRecogntionResult(null);
        }
    }


    /**
     * 음성인식 시작
     */
    private void startSpeechRecognition(){
        SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().
                setServiceType(SpeechRecognizerClient.SERVICE_TYPE_WORD);

        client = builder.build();
        client.setSpeechRecognizeListener(this);
        client.startRecording(false);
    }


    /**
     * 음성인식 중지
     */
    public void stop(){
        stop = true;
        if(client != null)
            client.stopRecording();
    }


    @Override
    public void mediaPlayerStop() {
        stop = false;
        startSpeechRecognition();
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


    /**
     * 음성인식 error 발생시 호출되는 함수
     * @param errorCode
     * @param errorMsg
     */
    @Override
    public void onError(int errorCode, String errorMsg) {
        client = null;
        if(stop == false)
            listener.speechRecogntionResult(null);
        else
            stop = false;
    }


    @Override
    public void onPartialResult(String partialResult) {
    }


    /**
     * 음성인식 결과 전달 함수
     * @param results : 음성인식 결과
     */
    @Override
    public void onResults(Bundle results) {
        client = null;
        if(stop == false) {
            ArrayList<String> sttArray = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
            if(sttArray.size() == 0)
                listener.speechRecogntionResult(null);
            else
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
}


