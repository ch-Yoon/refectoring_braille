package com.project.why.braillelearning.MediaPlayer;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.util.Log;

import com.project.why.braillelearning.EnumConstant.DotType;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.R;

import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

/**
 * Created by hyuck on 2017-11-08.
 */

/**
 * mediaplayer를 사용하기 위한 mediaplayer manager class
 * 이 class를 통해 mediaplayersingleton에 접근함
 */
public class MediaSoundManager {
    private MediaPlayerSingleton mediaPlayerSingleton;
    private Context context;

    public MediaSoundManager(Context context){
        mediaPlayerSingleton = MediaPlayerSingleton.getInstance(context);
        this.context = context;
    }

    /**
     * int형 음성 file id들을 출력하는 함수
     * 전달받은 int형 soundid를 queue에 담아 mediaPlayerSingleton에 전달함
     * @param soundId : int형 soundId
     */
    public void start(int soundId){
        Queue<Integer> soundIdQueue = new LinkedList<>();
        soundIdQueue.add(soundId);
        mediaPlayerSingleton.start(soundIdQueue);
    }

    /**
     * 손가락 1개를 이용하여 점자를 읽을 때, 점자 번호와 구분선, 경고음을 출력하는 함수
     * 점자 번호와 돌출유무를 통해 음성 file을 선택하여 queue에 담은 뒤 mediaPlayerSingleton에 전달함
     * @param dotType : 점자 번호
     * @param target : 점자 돌출 유무
     */
    public void start(int dotType, boolean target){
        int targetSoundId[] = {R.raw.men_1, R.raw.men_2, R.raw.men_3, R.raw.men_4, R.raw.men_5, R.raw.men_6};
        int nonTargetSoundId[] = {R.raw.women_1, R.raw.women_2, R.raw.women_3, R.raw.women_4, R.raw.women_5, R.raw.women_6, R.raw.external_wall, R.raw.devision_line};

        Queue<Integer> queue = new LinkedList<>();
        if(dotType == DotType.DEVISION_LINE.getNumber() || dotType == DotType.EXTERNAL_WALL.getNumber()){
            if(mediaPlayerSingleton.getMediaPlayer() == null){
                stop();
                queue.add(nonTargetSoundId[dotType-1]);
            } else {
                int previousMediaplayerId = mediaPlayerSingleton.getShieldId();
                if(previousMediaplayerId != R.raw.external_wall && previousMediaplayerId != R.raw.devision_line){
                    stop();
                    queue.add(nonTargetSoundId[dotType-1]);
                }
            }
        } else {
            stop();
            if(target == true)
                queue.add(targetSoundId[dotType-1]);
            else
                queue.add(nonTargetSoundId[dotType-1]);
        }

        mediaPlayerSingleton.start(queue);
    }

    /**
     * 음성정보가 String인 file들을 출력하는 함수
     * 점자 학습 data들의 경우, 글자 정보는 raw에서 가져오며, 점자 번호 정보들은 점자 행렬을 가공하여 음성 file을 구성함
     * @param soundId : String형 soundid
     */
    public void start(String soundId){
        Queue<String> tempQueue = new LinkedList<>();
        StringTokenizer st = new StringTokenizer(soundId,",");

        while(st.hasMoreTokens()) {
            tempQueue.add(st.nextToken());
        }

        Queue<Integer> soundIdQueue = new LinkedList<>();

        int rawId = checkRawId(tempQueue.peek());
        if(rawId != 0){
            tempQueue.poll();
            soundIdQueue.add(rawId);
            soundIdQueue.addAll(getTranslationQueue(tempQueue));
            mediaPlayerSingleton.start(soundIdQueue);
            Log.d("test","basicstart");
        } else {
            String ttsText = tempQueue.poll();
            soundIdQueue = getTranslationQueue(tempQueue);
            mediaPlayerSingleton.start(soundIdQueue, ttsText);
            Log.d("test","translationstart");
        }
    }

    /**
     * 제스처 음성출력을 위한 함수
     * @param fingerFunctionType
     */
    public void start(FingerFunctionType fingerFunctionType){
        Queue<Integer> soundIdQueue = getFingerFunctionQueue(fingerFunctionType);
        stop();
        mediaPlayerSingleton.start(soundIdQueue);
    }

    /**
     * String 음성 file이 raw에 존재하는지 check하는 함수.
     * @param soundId : String 음성 file id
     * @return 있으면 id값, 없으면 0 리턴
     */
    private int checkRawId(String soundId){
        String packName = context.getPackageName();
        Resources resources = context.getResources();
        return resources.getIdentifier(soundId, "raw", packName);
    }

    /**
     * 제스처 해당하는 음성 file들을 return하는 함수
     * @param fingerFunctionType : 제스처 type
     * @return : 제스처에 해당되는 음성 file id가 담긴 queue 리턴
     */
    private Queue<Integer> getFingerFunctionQueue(FingerFunctionType fingerFunctionType){
        Queue<Integer> soundIdQueue = new LinkedList<>();
        switch (fingerFunctionType){
            case ENTER:
                soundIdQueue.add(R.raw.enter);
                break;
            case RIGHT:
                soundIdQueue.add(R.raw.right);
                break;
            case LEFT:
                soundIdQueue.add(R.raw.left);
                break;
            case BACK:
                soundIdQueue.add(R.raw.back);
                break;
            case SPEECH:
                soundIdQueue.add(R.raw.speechrecognition_start);
                break;
            case NONE:
                soundIdQueue.add(R.raw.retry);
                break;
        }

        return soundIdQueue;
    }

    /**
     * 점자 행렬에 해당하는 내용들을 음성 file과 매칭하여 queue에 저장한 뒤 리턴하는 함수
     * 초성 기억을 학습하는 화면의 경우, '초성 기억, 한칸, 4점'이라는 내용이 음성으로 출력되어야 함
     * '초성 기억'이라는 음성 file id는 queue의 가장 맨 앞에 저장
     * 점자 행렬을 이용하여 칸 수와, 돌출 점자 번호를 의미하는 음성 file들을 순차적으로 queue에 저장
     * @param tempQueue : 문자열이 분해되어 순차적으로 저장되어 있는 String queue
     * @return 음성 file id들을 매칭하여 저장한 queue 리턴
     */
    private Queue<Integer> getTranslationQueue(Queue<String> tempQueue){
        Queue<Integer> soundIdQueue = new LinkedList<>();
        int queueSize = tempQueue.size();
        for(int i=0 ; i<queueSize ; i++){
            String targetText = tempQueue.poll();
            if(targetText != null) {
                switch (targetText) {
                    case "일":
                        soundIdQueue.add(R.raw.translation_onesell);
                        break;
                    case "이":
                        soundIdQueue.add(R.raw.translation_twosell);
                        break;
                    case "삼":
                        soundIdQueue.add(R.raw.translation_threesell);
                        break;
                    case "사":
                        soundIdQueue.add(R.raw.translation_foursell);
                        break;
                    case "오":
                        soundIdQueue.add(R.raw.translation_fivesell);
                        break;
                    case "육":
                        soundIdQueue.add(R.raw.translation_sixsell);
                        break;
                    case "칠":
                        soundIdQueue.add(R.raw.translation_sevensell);
                        break;
                    case "1":
                        soundIdQueue.add(R.raw.translation_one);
                        break;
                    case "2":
                        soundIdQueue.add(R.raw.translation_two);
                        break;
                    case "3":
                        soundIdQueue.add(R.raw.translation_three);
                        break;
                    case "4":
                        soundIdQueue.add(R.raw.translation_four);
                        break;
                    case "5":
                        soundIdQueue.add(R.raw.translation_five);
                        break;
                    case "6":
                        soundIdQueue.add(R.raw.translation_six);
                        break;
                    case "1점":
                        soundIdQueue.add(R.raw.translation_one_finish);
                        break;
                    case "2점":
                        soundIdQueue.add(R.raw.translation_two_finish);
                        break;
                    case "3점":
                        soundIdQueue.add(R.raw.translation_three_finish);
                        break;
                    case "4점":
                        soundIdQueue.add(R.raw.translation_four_finish);
                        break;
                    case "5점":
                        soundIdQueue.add(R.raw.translation_five_finish);
                        break;
                    case "6점":
                        soundIdQueue.add(R.raw.translation_six_finish);
                        break;
                    default:
                        break;
                }
            }
        }

        return soundIdQueue;
    }

    public void stop(){
        mediaPlayerSingleton.initializeAll();
    }

    public void allStop(){
        mediaPlayerSingleton.initializeMediaPlayer();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayerSingleton.getMediaPlayer();
    }

    public boolean checkTTSPlaying(){
        return mediaPlayerSingleton.checkTTSPlay();
    }
}
