package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.BrailleDataManager;
import com.project.why.braillelearning.LearningModel.GettingBraille;
import com.project.why.braillelearning.LearningView.ViewObservers;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-25.
 */


/**
 * 점자 학습을 위한 Control class
 * 점자 학습을 위한 모든 action event들을 현재 클래스에서 담당함
 *
 * 손가락 1개 : 점자 읽기 기능
 * 손가락 2개 : 화면 이동
 * 손가락 3개 : 음성인식, 나만의 단어장 저장 및 삭제
 */

public class BrailleControl implements Control {
    private final int ONE_FINGER = FingerFunctionType.ONE_FINGER.getNumber(); // 손가락 1개
    private final int TWO_FINGER = FingerFunctionType.TWO_FINGER.getNumber(); // 손가락 2개
    private final int THREE_FINGER = FingerFunctionType.THREE_FINGER.getNumber(); // 손가락 3개
    private boolean multiTouch = false;
    private boolean functionLock = false;
    private MediaSoundManager mediaSoundManager;
    private ViewObservers viewObservers;
    private ArrayList<BrailleData> brailleDataArrayList;
    private BrailleData data;

    private int pageNumber = 0;
    private FingerCoordinate fingerCoordinate; // touch된 손가락들의 좌표 저장 class
    private SingleFingerFunction oneFingerFunction;
    private MultiFingerFunction twoFingerFunction;
    private MultiFingerFunction threeFIngerFunction;
    private SpeechRecognition speechRecognitionFunction; // 음성인식 모듈
    private FingerFunctionType type = FingerFunctionType.NONE;

    BrailleControl(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType) {
        mediaSoundManager = new MediaSoundManager(context);
        brailleDataArrayList = getBrailleDataArray(context, jsonFileName, databaseFileName, brailleLearningType);
        fingerCoordinate = new FingerCoordinate(THREE_FINGER);
        oneFingerFunction = getSingleFingerFunction(context, brailleLearningType);
        twoFingerFunction = new MultiFinger(context, FingerFunctionType.TWO_FINGER);
        threeFIngerFunction = new MultiFinger(context, FingerFunctionType.THREE_FINGER);
        speechRecognitionFunction = getSpeechRecognitionFunction(context, brailleLearningType);
    }

    /**
     * 손가락 1개에 대한 이벤트 모듈 얻는 함수
     * @param context : 현재 Acitivity에 대한 context
     * @param brailleLearningType : 진행하려는 점자 학습의 type
     * @return : 손가락 1개에 대한 이벤트 모듈 리턴
     */
    public SingleFingerFunction getSingleFingerFunction(Context context, BrailleLearningType brailleLearningType){
        SingleFIngerFactory singleFIngerFactory = new SingleFIngerFactory(context, brailleLearningType);
        return singleFIngerFactory.getSingleFingerMoudle();
    }

    /**
     * 음성인식이 활용되는 학습 모듈을 얻는 함수
     * @param context : 현재 Activity에 대한 context
     * @param brailleLearningType : 진행하려는 점자 학습의 type
     * @return : 음성인식이 활용되는 학습 모듈 리턴 (점자 번역, 퀴즈, 선생님과의 대화)
     */
    public SpeechRecognition getSpeechRecognitionFunction(Context context, BrailleLearningType brailleLearningType) {
        SpeechRecognitionFactory speechRecognitionFactory = new SpeechRecognitionFactory(context, brailleLearningType);
        return speechRecognitionFactory.getSpeechRecognitionModule();
    }

    /**
     * 점자 학습에 필요한 점자 data를 얻는 함수
     * @param context : 현재 Activity에 대한 context
     * @param jsonFileName : Json File로 구성되어 있는 메뉴의 경우, 해당 file의 이름을 갖고 있는 변수
     * @param databaseFileName : SQLite database에 저장되어 있는 메뉴의 경우, database의 이름을 갖고 있는 변수
     * @param brailleLearningType : 진행하려는 점자 학습의 type
     * @return : 점자 data를 갖고 있는 BrailleData 리턴
     */
    public ArrayList<BrailleData> getBrailleDataArray(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType) {
        BrailleDataManager brailleDataManager = new BrailleDataManager(context, jsonFileName, databaseFileName, brailleLearningType);
        GettingBraille gettingBraille = brailleDataManager.getBrailleArrayList();

        if(gettingBraille != null)
            return gettingBraille.getBrailleDataArray();
        else
            return new ArrayList<>();
    }


    /**
     * 점자를 화면에 그리는 view를 현재 class의 Observer로 등록
     * @param observers : 점자를 화면에 그리는 view
     */
    @Override
    public void addObservers(ViewObservers observers) {
        Log.d("BrailleControl","addObservers");
        viewObservers = observers;
    }


    /**
     * data를 새로고침하는 함수.
     * pageNumber에 따라 점자 data를 선택함
     */
    public void refreshData(){
        Log.d("BrailleControl","refreshData");
        if(brailleDataArrayList != null) {
            if (!brailleDataArrayList.isEmpty() && pageNumber < brailleDataArrayList.size()) {
                data = brailleDataArrayList.get(pageNumber);
                mediaSoundManager.start(data.getRawId());
                type = FingerFunctionType.NONE;
            } else {
                if(data != null){
                    mediaSoundManager.start(data.getRawId());
                    type = FingerFunctionType.NONE;
                }
            }
        }
    }

    /**
     * 현재 data를 새로고침 한 뒤, Observer에게 데이터를 알리는 함수
     */
    @Override
    public void nodifyObservers() {
        refreshData();
        viewObservers.nodifyBraille(data);
    }


    /**
     * 손가락 1개, 2개, 3개에 대한 event를 처리하는 touchEvent
     * @param event : touchEvent를 관리하는 변수
     * @return : true(점자 학습 종료), false(변화 없음)
     */
    @Override
    public boolean touchEvent(MotionEvent event) {
        int pointer_Count = event.getPointerCount(); // 현재 발생된 터치 event의 수

        if(pointer_Count > THREE_FINGER) // 발생된 터치 이벤트가 3개를 초과하여도 3개까지만 인식
            pointer_Count = THREE_FINGER;

        if(pointer_Count == ONE_FINGER) {
            switch(event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP: // 손가락 1개를 화면에서 떨어트렸을 때 발생되는 Event
                    oneFingerFunction.init();
                    multiTouch = false;
                    functionLock = false;
                    break;
                default:
                    if(multiTouch == false) {
                        fingerCoordinate.setDownCoordinate(event, pointer_Count);
                        oneFingerFunction.oneFingerFunction(data, fingerCoordinate);
                    }
                    break;
            }
        } else if(pointer_Count > ONE_FINGER) {
            multiTouch = true;
            switch(event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    fingerCoordinate.setDownCoordinate(event, pointer_Count);
                    break;
                case MotionEvent.ACTION_POINTER_UP: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    fingerCoordinate.setUpCoordinate(event, pointer_Count);
                    if(functionLock == false) {
                        functionLock = true;
                        if (pointer_Count == TWO_FINGER)
                            return twoFingerFunction();
                        else if (pointer_Count == THREE_FINGER)
                            return threeFingerFunction();
                    }
                    break;
            }
        }

        return false;
    }

    /**
     * 손가락 2개에 대한 event 함수
     * BACK(상위메뉴), RIGHT(오른쪽 화면), LEFT(왼쪽 화면), REFRESH(다시듣기)
     * @return true(학습 종료), false(변화 없음)
     */
    public boolean twoFingerFunction() {
        type = twoFingerFunction.fingerFunctionType(fingerCoordinate);
        switch (type) {
            case BACK:
                return true;
            case RIGHT:
                if (pageNumber + 1 < brailleDataArrayList.size()) {
                    pageNumber++;
                    nodifyObservers();
                    return false;
                } else
                    return true;
            case LEFT:
                if (0 <= pageNumber - 1) {
                    pageNumber--;
                    nodifyObservers();
                }
                return false;
            case REFRESH:
                nodifyObservers();
                return false;
            default:
                return false;
        }
    }

    /**
     * 손가락 3개에 대한 event 함수
     * SPEECH(음성인식), MYNOTE(나만의 단어장 저장 및 삭제)
     * @return true(학습 종료), false(변화 없음)
     */
    public boolean threeFingerFunction() {
        type = threeFIngerFunction.fingerFunctionType(fingerCoordinate);

        switch (type) {
            case SPEECH: // 점자번역 퀴즈 선생님과의 대화
                if (speechRecognitionFunction != null)
                    speechRecognitionFunction.startSpecialFunction(speechRecognitionCallbackMethod);
                return false;
            case MYNOTE: // 추가 삭제
                return false;
            default:
                return false;
        }
    }

    /**
     * 음성인식에 대한 callback listener
     * 음성인식으로 인해 가공되어진 braille data가 전달되어짐
     */
    private CallBack speechRecognitionCallbackMethod = new CallBack() {
        @Override
        public void objectCallBackMethod(Object obj) {
            data = (BrailleData) obj;
            nodifyObservers();
        }
    };


    /**
     * 일시정지 되었을 때 함수
     */
    @Override
    public void onPause() {
        mediaSoundManager.stop();
    }
}
