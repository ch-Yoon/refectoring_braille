package com.project.why.braillelearning.MediaPlayer;

import android.content.Context;
import android.media.MediaPlayer;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;
import com.project.why.braillelearning.R;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by hyuck on 2017-11-02.
 */

/**
 * 자체 talkBack을 위한 mediaPlayer Singleton class
 * TTS 모듈을 통해 점자 번역 음성을 출력
 * 음성 file을 Queue안에 넣어 순차적으로 출력하는 구조.
 */
public class MediaPlayerSingleton implements TextToSpeechListener {
    private static final MediaPlayerSingleton ourInstance = new MediaPlayerSingleton();
    private TextToSpeechClient ttsClient;
    private Context context;
    private MediaPlayer mediaPlayer, focusMediaPlayer;
    private Queue<Integer> queue = new LinkedList<>();
    private Queue<Integer> tempQueue = new LinkedList<>();
    private int shieldId; // 특정 음성들이 종료되지 않도록 방어하는 변수
    private boolean mediaPlaying = false;
    private boolean menuInfoPlaying = false;
    private MediaPlayerStopCallbackListener mediaPlayerStopCallbackListener;

    public static MediaPlayerSingleton getInstance(Context context) {
        ourInstance.setContext(context);
        TextToSpeechManager.getInstance().initializeLibrary(context);
        SpeechRecognizerManager.getInstance().initializeLibrary(context); // SDK 초기화 kakao api에러.. 음성합성만을 이용하려고 해도 음성인식을 초기화 해야함
        return ourInstance;
    }

    private MediaPlayerSingleton(){}

    private void setContext(Context context){
        this.context = context;
    }

    public void setMediaPlayerStopCallbackListener(MediaPlayerStopCallbackListener listener){
        this.mediaPlayerStopCallbackListener = listener;
    }

    public void initialMediaPlayerStopCallbackListener(){
        this.mediaPlayerStopCallbackListener = null;
    }

    /**
     * 일반적인 음성 file 출력 함수
     * @param soundIdQueue : MediaSoundManager로 부터 Queue를 받음.
     */
    public void start(Queue<Integer> soundIdQueue){
        queue.addAll(soundIdQueue);
        checkMediaPlayer();
    }

    public void focusSoundStart(){
        if(focusMediaPlayer == null) {
            focusMediaPlayer = MediaPlayer.create(context, R.raw.focus_sound);
            focusMediaPlayer.start();
            focusMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    initializeFocusMediaPlayer();
                }
            });
        }
    }

    /**
     * mediaplayer 메모리 해제 함수
     */
    public void initializeFocusMediaPlayer(){
        if (focusMediaPlayer != null) {
            if (focusMediaPlayer.isPlaying()) {
                focusMediaPlayer.stop();
                focusMediaPlayer.release();
                focusMediaPlayer = null;
            } else {
                focusMediaPlayer.release();
                focusMediaPlayer = null;
            }
        }
    }


    /**
     * tts 출력을 위한 함수
     * @param soundIdQueue : mediaSoundManager로 부터 음성 queue를 전달받음
     * @param ttsText : tts출력을 위한 text 변수
     * kakao tts 모듈을 통해 text를 출력한 뒤, tts 출력이 완료되고 나면 queue에 저장되어 있는 음성 file을 출력
     */
    public void start(Queue<Integer> soundIdQueue, String ttsText){
        tempQueue.clear();
        tempQueue.addAll(soundIdQueue);

        if (ttsClient != null && ttsClient.isPlaying())
            ttsClient.stop();

        String speechMode = TextToSpeechClient.NEWTONE_TALK_2;
        String voiceType = TextToSpeechClient.VOICE_MAN_READ_CALM;
        double speechSpeed = 1.00;

        ttsClient = new TextToSpeechClient.Builder()
                .setSpeechMode(speechMode)
                .setSpeechSpeed(speechSpeed)
                .setSpeechVoice(voiceType)
                .setListener(this)
                .build();

        ttsClient.play(ttsText);
    }


    /**
     * 다수의 곳에서 접근할 때, race contidion을 방지하기 위한 동기화 함수
     */
    private synchronized void checkMediaPlayer(){
        if(mediaPlayer != null){
            if(!mediaPlayer.isPlaying()){
                initializeMediaPlayer();
                startMediaPlayer();
            }
        } else
            startMediaPlayer();
    }


    /**
     * queue에 저장되어 있는 음성들을 순차적으로 출력함
     */
    private void startMediaPlayer(){
        if(queue.peek() != null){
            mediaPlaying = true;
            int id = queue.poll();
            shieldId = id; // 음성출력 방어변수
            checkMenuInfoMedia(id);
            mediaPlayer = MediaPlayer.create(context, id);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    initializeMediaPlayer();
                    initializeShield();
                    checkMediaPlayer();
                }
            });
        } else {
            mediaPlaying = false;
            if(getMediaPlaying() == false){
                if(mediaPlayerStopCallbackListener != null) {
                    mediaPlayerStopCallbackListener.mediaPlayerStop();
                    initialMediaPlayerStopCallbackListener();
                }
            }
        }
    }


    /**
     * 메뉴 가이드 음성이 출력중인지 확인하는 함수
     * @param id : 현재 출력중인 음성 파일 id
     */
    private void checkMenuInfoMedia(int id){
        int menuInfo_Id[] = new int[]{R.raw.tutorial_info_basic, R.raw.tutorial_info_blind, R.raw.basic_info, R.raw.master_info, R.raw.translation_info,
                R.raw.quiz_info, R.raw.mynote_info, R.raw.teacher_mode, R.raw.student_mode};
        for(int i=0; i<menuInfo_Id.length ; i++){
            if(id == menuInfo_Id[i])
                menuInfoPlaying = true;
        }
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public int getShieldId(){
        return shieldId;
    }

    public void initializeAll(){
        if(!checkMediaShield())
            initializeMediaPlayer();
        initializeQueue();
    }

    public void initializeShield(){
        shieldId = 0;
    }

    public void initializeQueue(){
        tempQueue.clear();
        queue.clear();
        mediaPlaying = false;
    }

    /**
     * mediaplayer 메모리 해제 함수
     */
    public void initializeMediaPlayer(){
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            } else {
                mediaPlayer.release();
                mediaPlayer = null;
            }
        }

        menuInfoPlaying = false;
        mediaPlaying = false;
    }

    /**
     * 특정 음성이 멈추지 않게 하기 위한 방어 함수
     * 다음 음성이 없으면서, 직전 음성출력 file이 방어하기로 설정된 음성일 경우 해당 파일은 메모리를 당장 해제하지 않음
     * @return
     */
    private boolean checkMediaShield(){
        if(queue.peek() == null) {
            int gestureId[] = {R.raw.enter, R.raw.back, R.raw.right, R.raw.left, R.raw.empty, R.raw.alldelete, R.raw.speechrecognition_start};
            for (int i = 0; i < gestureId.length; i++) {
                if (shieldId == gestureId[i])
                    return true;
            }
        }

        return false;
    }

    @Override
    public void onFinished() {
        queue.clear();
        queue.addAll(tempQueue);
        checkMediaPlayer();
        ttsClient = null;
    }

    public boolean getMenuInfoPlaying(){
        return menuInfoPlaying;
    }

    @Override
    public void onError(int code, String message) {
        ttsClient = null;
    }


    public boolean getMediaPlaying(){
        if(queue.size() == 0 && mediaPlayer == null && ttsClient == null && mediaPlaying == false)
            return false;
        else
            return true;
    }

    public boolean checkTTSPlay(){
        if(ttsClient != null)
            return true;
        else
            return false;
    }
}
