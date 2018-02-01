package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.LearningModel.QuizBrailleData;
import com.project.why.braillelearning.R;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-11-17.
 */

/**
 * 퀴즈 메뉴를 위한 class
 */
public class QuizControl extends BasicControl implements SpeechRecognitionListener{
    private SpeechRecognitionMoudle speechRecognitionMoudle;
    private ArrayList<QuizBrailleData> quizBrailleDataArrayList = new ArrayList<>();
    private QuizBrailleData quizData;
    private boolean progress = false;

    QuizControl(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType, ControlListener controlListener){
        super(context, jsonFileName, databaseFileName, brailleLearningType, controlListener);
        speechRecognitionMoudle = new SpeechRecognitionMoudle(context, this);
        setRandomQuizBrailleData();
    }

    /**
     * 일시정지 되었을 때 함수
     */
    @Override
    public void onPause() {
        touchLock = false;
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
        if(0 <= pageNumber && pageNumber < quizBrailleDataArrayList.size()){
            quizData = quizBrailleDataArrayList.get(pageNumber);
            data = quizData;
            if(quizData != null) {
                if(progress == false)
                    mediaSoundManager.start(quizData.getQuizRawId());
            }
        } else
            exit();
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
        if(touchLock == false) {
            if (data != null)
                oneFingerFunction.oneFingerFunction(data.getBrailleMatrix(), fingerCoordinate);
        }
    }


    /**
     * 손가락 3개 함수 재정의
     * SPEECH : 음성인식
     * MYNOTE : 나만의 단어장 저장
     * @param fingerCoordinate : 좌표값
     */
    @Override
    public void onThreeFingerFunction(FingerCoordinate fingerCoordinate) {
        if(touchLock == false) {
            FingerFunctionType type = multiFingerFunction.getFingerFunctionType(fingerCoordinate);
            switch (type) {
                case SPEECH:
                    touchLock = true;
                    progress = true;
                    speechRecognitionMoudle.start();
                    break;
                case MYNOTE:
                    dbManager.saveMyNote(data.getLetterName(), data.getStrBrailleMatrix(), data.getAssistanceLetterName(), data.getRawId());
                    break;
                default:
                    break;
            }
        }
    }


    /**
     * 음성인식에 대한 callback method
     * 음성인식에 대한 ArrayList가 전달됨
     */
    @Override
    public void speechRecogntionResult(ArrayList<String> text) {
        if(text != null) {
            boolean result = checkAnswer(text);
            mediaSoundManager.start(result, pageNumber, quizData.getRawId());
            nodifyObservers();
        } else {
            mediaSoundManager.start(R.raw.speechrecognition_fail);
            mediaSoundManager.start(R.raw.retry);
        }
        progress = false;
        touchLock = false;
    }


    /**
     * 정답 확인 함수
     * @param text : 음성인식 결과 ArrayList
     * @return : true(정답), false(오답)
     */
    private boolean checkAnswer(ArrayList<String> text){
        boolean result = false;
        for(int i=0 ; i<text.size() ; i++){
            String quizAnster = quizData.getAssistanceLetterName();
            if(text.get(i) == quizAnster || text.get(i).equals(quizAnster))
                result = true;
        }

        return result;
    }
}
