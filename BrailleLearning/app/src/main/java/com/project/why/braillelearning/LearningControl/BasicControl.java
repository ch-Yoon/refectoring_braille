package com.project.why.braillelearning.LearningControl;

import android.content.Context;
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
import com.project.why.braillelearning.MynoteDB.DBManager;
import com.project.why.braillelearning.R;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-11-16.
 */

/**
 * 점자 학습을 위한 Control class
 * 점자 학습을 위한 모든 action event들을 현재 클래스에서 담당함
 * 각 손가락 기능들은 학습 방법에 따라 재정의하여 구현 수정 가능
 * 손가락 1개 : 점자 읽기 기능
 * 손가락 2개 : 화면 이동 및 다시 듣기
 * 손가락 3개 : 나만의 단어장 삭제
 */

public class BasicControl implements Control{
    private final int ONE_FINGER = 1;
    private final int TWO_FINGER = 2;
    private final int THREE_FINGER = 3;
    private ControlListener controlListener;
    private boolean multiTouch = false;
    private boolean touchLock = false;
    protected ViewObservers viewObservers;
    protected MediaSoundManager mediaSoundManager;
    protected FingerCoordinate fingerCoordinate;
    protected BrailleData data;
    protected SingleFingerFunction oneFingerFunction;
    protected MultiFinger multiFingerFunction;
    protected ArrayList<BrailleData> brailleDataArrayList;
    protected DBManager dbManager;
    protected int pageNumber = 0 ;
    protected Context context;

    BasicControl(Context context,Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType, ControlListener controlListener){
        this.context = context;
        brailleDataArrayList = getBrailleDataArray(context, jsonFileName, databaseFileName, brailleLearningType);
        dbManager = new DBManager(context, databaseFileName);
        mediaSoundManager = new MediaSoundManager(context);
        oneFingerFunction = getSingleFingerFunction(context, brailleLearningType);
        multiFingerFunction = new MultiFinger(context);
        fingerCoordinate = new FingerCoordinate(FingerFunctionType.THREE_FINGER.getNumber());
        this.controlListener = controlListener;
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
     * 손가락 1개에 대한 이벤트 모듈 얻는 함수
     * @param context : 현재 Acitivity에 대한 context
     * @param brailleLearningType : 진행하려는 점자 학습의 type
     * @return : 손가락 1개에 대한 이벤트 모듈 리턴
     */
    public SingleFingerFunction getSingleFingerFunction(Context context, BrailleLearningType brailleLearningType){
        SingleFIngerFactory singleFIngerFactory = new SingleFIngerFactory(context, brailleLearningType);
        return singleFIngerFactory.getSingleFingerMoudle();
    }

    @Override
    public void addObservers(ViewObservers observers) {
        this.viewObservers = observers;
    }

    /**
     * data를 새로고침하는 함수.
     * pageNumber에 따라 점자 data를 선택함
     */
    public void refreshData(){
        if(0 <= pageNumber && pageNumber < brailleDataArrayList.size()){
            data = brailleDataArrayList.get(pageNumber);
            if(data != null)
                mediaSoundManager.start(data.getRawId());
        }
    }

    /**
     * viewObserver에게 데이터를 알림
     */
    @Override
    public void nodifyObservers() {
        refreshData();
        if(data != null)
            viewObservers.nodifyBraille(data.getLetterName(), data.getBrailleMatrix());
    }

    /**
     * 일시정지 되었을 때 함수
     */
    @Override
    public void onPause() {
        mediaSoundManager.stop();
    }

    /**
     * 손가락 1개, 2개, 3개에 대한 event를 처리하는 touchEvent
     * @param event : touchEvent를 관리하는 변수
     */
    @Override
    public void touchEvent(MotionEvent event) {
        int pointer_Count = event.getPointerCount(); // 현재 발생된 터치 event의 수

        if(pointer_Count > THREE_FINGER) // 발생된 터치 이벤트가 3개를 초과하여도 3개까지만 인식
            pointer_Count = THREE_FINGER;

        if(pointer_Count == ONE_FINGER) {
            switch(event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP: // 손가락 1개를 화면에서 떨어트렸을 때 발생되는 Event
                    multiTouch = false;
                    fingerCoordinate.initialize();
                    oneFingerFunction.init();
                    break;
                default:
                    if(multiTouch == false) {
                        fingerCoordinate.setDownCoordinate(event, pointer_Count);
                        oneFingerFunction();
                    }
                    break;
            }
        } else if(pointer_Count > ONE_FINGER) {
            multiTouch = true;
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    touchLock = false;
                    fingerCoordinate.setDownCoordinate(event, pointer_Count);
                    break;
                case MotionEvent.ACTION_POINTER_UP: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    if(touchLock == false) {
                        touchLock = true;
                        fingerCoordinate.setUpCoordinate(event, pointer_Count);

                        if (pointer_Count == TWO_FINGER)
                            twoFingerFunction();
                        else if (pointer_Count == THREE_FINGER)
                            threeFingerFunction();
                    }
            }
        }
    }

    /**
     * 손가락 1개에 대한 event 함수
     * 점자 행렬의 좌푯값 전달
     */
    public void oneFingerFunction() {
        if(data != null)
            oneFingerFunction.oneFingerFunction(data.getBrailleMatrix(), fingerCoordinate);
        else
            oneFingerFunction.oneFingerFunction(null, fingerCoordinate);
    }

    /**
     * 손가락 2개에 대한 event 함수
     * BACK(상위메뉴), RIGHT(오른쪽 화면), LEFT(왼쪽 화면), REFRESH(다시듣기)
     */
    public void twoFingerFunction() {
        FingerFunctionType type = multiFingerFunction.getFingerFunctionType(fingerCoordinate);
        switch (type) {
            case BACK:
                exit();
                break;
            case RIGHT:
                if (pageNumber < brailleDataArrayList.size()-1) {
                    pageNumber++;
                    nodifyObservers();
                }
                else {
                    mediaSoundManager.allStop();
                    mediaSoundManager.start(R.raw.last_page);
                }
                break;
            case LEFT:
                if (0 < pageNumber) {
                    pageNumber--;
                    nodifyObservers();
                }
                else {
                    mediaSoundManager.allStop();
                    mediaSoundManager.start(R.raw.first_page);
                }
                break;
            case REFRESH:
                nodifyObservers();
                break;
        }
    }

    /**
     * 손가락 3개에 대한 event 함수
     * SPEECH(음성인식), MYNOTE(나만의 단어장 저장 및 삭제)
     */
    public void threeFingerFunction() {
        FingerFunctionType type = multiFingerFunction.getFingerFunctionType(fingerCoordinate);
        switch (type) {
            case SPEECH:
                mediaSoundManager.start(R.raw.impossble_function);
                break;
            case MYNOTE:
                dbManager.saveMyNote(data.getLetterName(), data.getStrBrailleMatrix(), data.getAssistanceLetterName(), data.getRawId());
                break;
        }
    }

    /**
     * 학습화면 종료 함수
     */
    public void exit(){
        controlListener.exit();
    }
}
