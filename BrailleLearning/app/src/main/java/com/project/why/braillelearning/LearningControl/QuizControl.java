package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.EnumConstant.TouchLock;
import com.project.why.braillelearning.LearningModel.QuizBrailleData;
import com.project.why.braillelearning.Permission.PermissionCheckCallbackListener;
import com.project.why.braillelearning.Permission.PermissionCheckModule;
import com.project.why.braillelearning.R;
import com.project.why.braillelearning.SpeechRecognition.SpeechRecognitionListener;
import com.project.why.braillelearning.SpeechRecognition.SpeechRecognitionModule;
import java.util.ArrayList;

/**
 * Created by hyuck on 2017-11-17.
 */

/**
 * 퀴즈 메뉴를 위한 class
 */
public class QuizControl extends BasicControl implements SpeechRecognitionListener {
    private SpeechRecognitionModule speechRecognitionMoudle;
    private ArrayList<QuizBrailleData> quizBrailleDataArrayList = new ArrayList<>();
    private QuizBrailleData quizData;
    private boolean progress = false;
    private int previousPageNumber = 0;
    boolean result = false;

    QuizControl(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType, ControlListener controlListener){
        super(context, jsonFileName, databaseFileName, brailleLearningType, controlListener);
        speechRecognitionMoudle = new SpeechRecognitionModule(context, this);
        setRandomQuizBrailleData();
    }

    /**
     * 일시정지 되었을 때 함수
     */
    @Override
    public void onPause() {
        customTouchConnectListener.setTouchLock(TouchLock.UNLOCK);
        mediaSoundManager.stop();
        speechRecognitionMoudle.pause();
        pauseTouchEvent();
    }

    /**
     * 퀴즈를 위한 data set 함수
     * json에서 불러온 braille Data array에서 랜덤으로 3개의 값을 꺼낸 뒤, quiz진행에 맞는 data로 가공
     */
    private void setRandomQuizBrailleData(){
        final int quizCount = 3;
        int randomArray[] = new int[quizCount]; //3문제
        for(int i=0 ; i<randomArray.length ; i++)
            randomArray[i] = -1;

        int index = 0;
        while(index < quizCount){
            int random = (int) (Math.random() * brailleDataArrayList.size());

            boolean result = true;

            for(int i=0 ; i<randomArray.length ; i++){
                if(randomArray[i] == random)
                    result = false;
            }

            if(result == true)
                randomArray[index++] = random;
        }

        for(int i=0 ; i<randomArray.length ; i++){
            QuizBrailleData quizBrailleData = new QuizBrailleData(brailleDataArrayList.get(randomArray[i]), i);
            quizBrailleDataArrayList.add(quizBrailleData);
        }
    }


    /**
     * data를 새로고침하는 함수.
     * pageNumber에 따라 점자 data를 선택함
     */
    @Override
    protected void refreshData(){
        if(previousPageNumber != pageNumber){
            previousPageNumber = pageNumber;
            progress = false;
        }

        if(0 <= pageNumber && pageNumber < quizBrailleDataArrayList.size()){
            quizData = quizBrailleDataArrayList.get(pageNumber);
            data = quizData;
            if(quizData != null) {
                if(progress == false)
                    mediaSoundManager.start(quizData.getQuizRawId());
                else
                    mediaSoundManager.start(result, pageNumber, quizData.getRawId());
            }
        }
    }


    /**
     * viewObserver에게 데이터를 알림
     */
    @Override
    public void nodifyObservers() {
        refreshData();
        if(data != null){
            String letterName;
            if(progress == false)
                letterName = quizData.getQuizLettername();
            else
                letterName = quizData.getLetterName();
            viewObservers.nodifyBraille(letterName, quizData.getBrailleMatrix());
        }
    }


    /**
     * 손가락 1개 함수 재정의
     * @param fingerCoordinate : 좌표값
     */
    @Override
    public void onOneFingerMoveFunction(FingerCoordinate fingerCoordinate) {
        if (data != null)
            oneFingerFunction.oneFingerFunction(data.getBrailleMatrix(), fingerCoordinate);
    }


    /**
     * 음성인식에 대한 callback method
     * 음성인식에 대한 ArrayList가 전달됨
     * 음성인식 결과 수신 완료 후 제스처 기능 lock 해제
     */
    @Override
    public void speechRecogntionResult(ArrayList<String> text) {
        progress = true;
        if(text != null) {
            result = checkAnswer(text);
            nodifyObservers();
        } else {
            mediaSoundManager.start(R.raw.speechrecognition_fail);
            mediaSoundManager.start(R.raw.retry);
        }
        customTouchConnectListener.setTouchLock(TouchLock.UNLOCK);
    }


    /**
     * 정답 확인 함수
     * @param text : 음성인식 결과 ArrayList
     * @return : true(정답), false(오답)
     */
    private boolean checkAnswer(ArrayList<String> text){
        boolean result = false;
        for(int i=0 ; i<text.size() ; i++){
            String quizAnswer = quizData.getAssistanceLetterName();
            if(text.get(i) == quizAnswer || text.get(i).equals(quizAnswer))
                result = true;
        }

        return result;
    }


    /**
     * 음성인식 특수기능 함수
     * 제스처 기능을 무시하도록 lock을 건 뒤 음성인식 실행
     */
    @Override
    public void onSpeechRecognition() {
        int checkPermissionResult = permissionCheckModule.checkPermission();
        if(checkPermissionResult == PermissionCheckModule.PERMISSION_NOT_ALLOWED){
            customTouchConnectListener.setTouchLock(TouchLock.PERMISSION_CHECK_LOCK);
            permissionCheckModule.startPermissionGuide(checkPermissionResult);
            permissionCheckModule.setPermissionCheckCallbackListener(new PermissionCheckCallbackListener() {
                @Override
                public void permissionCancel() {
                    customTouchConnectListener.setTouchLock(TouchLock.UNLOCK);
                    permissionCheckModule.cancelPermissionGuide();
                    refreshData();
                }
            });
        } else {
            customTouchConnectListener.setTouchLock(TouchLock.SPEECH_RECOGNITION_LOCK);
            speechRecognitionMoudle.start();
        }
    }


    /**
     * 권한 설정에 동의하였을 경우
     */
    @Override
    public void onPermissionUseAgree() {
        permissionCheckModule.permissionSettingMove();
    }


    /**
     * 권한 설정에 동의하지 않았을 경우
     */
    @Override
    public void onPermissionUseDisagree() {
        permissionCheckModule.cancelPermissionGuide();
        refreshData();
    }
}
