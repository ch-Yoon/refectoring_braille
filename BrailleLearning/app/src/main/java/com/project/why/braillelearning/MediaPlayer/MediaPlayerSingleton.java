package com.project.why.braillelearning.MediaPlayer;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.kakao.sdk.newtoneapi.TextToSpeechClient;
import com.kakao.sdk.newtoneapi.TextToSpeechListener;
import com.kakao.sdk.newtoneapi.TextToSpeechManager;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
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
    private MediaPlayer mediaPlayer;
    private Queue<Integer> queue = new LinkedList<>();
    private int shieldId; // 제스처 음성들이 종료되지 않도록 방어하는 변수

    public static MediaPlayerSingleton getInstance(Context context) {
        ourInstance.setContext(context);
        TextToSpeechManager.getInstance().initializeLibrary(context);
        return ourInstance;
    }

    private MediaPlayerSingleton(){}

    private void setContext(Context context){
        this.context = context;
    }

    /**
     * 일반적인 음성 file 출력 함수
     * @param soundIdQueue : MediaSoundManager로 부터 Queue를 받음.
     */
    public void start(Queue<Integer> soundIdQueue){
        queue.addAll(soundIdQueue);
        checkMediaPlayer();
    }

    /**
     * tts 출력을 위한 함수
     * @param soundIdQueue : mediaSoundManager로 부터 음성 queue를 전달받음
     * @param ttsText : tts출력을 위한 text 변수
     * kakao tts 모듈을 통해 text를 출력한 뒤, tts 출력이 완료되고 나면 queue에 저장되어 있는 음성 file을 출력
     */
    public void start(Queue<Integer> soundIdQueue, String ttsText){
        queue.addAll(soundIdQueue);
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
        Log.d("media","queue size : "+ queue.size());
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
            int id = queue.poll();
            shieldId = id; // 제스처 음성출력 방어변수
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
        }
    }

    public MediaPlayer getMediaPlayer(){
        return mediaPlayer;
    }

    public int getShieldId(){
        return shieldId;
    }

    public void initializeAll(){
        if(!checkGestureShield()) {
            initializeQueue();
            initializeMediaPlayer();
        }
    }

    public void initializeShield(){
        shieldId = 0;
    }

    public void initializeQueue(){
        queue.clear();
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
    }

    /**
     * 제스처 함수는 멈추지 않게 하기 위한 방어 함수
     * 직전 음성출력 file이 제스처 음성일 경우 해당 파일은 메모리를 당장 해제하지 않음
     * @return
     */
    private boolean checkGestureShield(){
        if(queue.peek() == null) {
            int gestureId[] = {R.raw.enter, R.raw.back, R.raw.right, R.raw.left};
            for (int i = 0; i < gestureId.length; i++) {
                if (shieldId == gestureId[i])
                    return true;
            }
        }

        return false;
    }

    @Override
    public void onFinished() {
        ttsClient = null;
        checkMediaPlayer();
    }

    @Override
    public void onError(int code, String message) {
        ttsClient = null;

    }
}