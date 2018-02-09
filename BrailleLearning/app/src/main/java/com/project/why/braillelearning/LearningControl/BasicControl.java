package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import com.project.why.braillelearning.CustomTouch.CustomLearningTouchEvent;
import com.project.why.braillelearning.CustomTouch.CustomLearningTouchListener;
import com.project.why.braillelearning.CustomTouch.CustomTouchConnectListener;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.BrailleDataManager;
import com.project.why.braillelearning.LearningModel.GettingBraille;
import com.project.why.braillelearning.LearningModel.SpecialFunctionData;
import com.project.why.braillelearning.LearningModel.SpecialFunctionManager;
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

public class BasicControl implements Control, CustomLearningTouchListener, SpecialFunctionListener {
    private int TWO_FINGER = FingerFunctionType.TWO_FINGER.getNumber(); // 손가락 2개
    protected ControlListener controlListener;
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
    protected CustomTouchConnectListener customTouchConnectListener;
    protected int specialFunctionIndex = 0;
    private SpecialFunctionManager specialFunctionManager;

    BasicControl(Context context,Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType, ControlListener controlListener){
        this.context = context;
        brailleDataArrayList = getBrailleDataArray(context, jsonFileName, databaseFileName, brailleLearningType);
        dbManager = new DBManager(context, databaseFileName);
        mediaSoundManager = new MediaSoundManager(context);
        oneFingerFunction = getSingleFingerFunction(context, brailleLearningType);
        multiFingerFunction = new MultiFinger(context);
        fingerCoordinate = new FingerCoordinate(TWO_FINGER);
        this.controlListener = controlListener;
        specialFunctionManager = new SpecialFunctionManager(brailleLearningType, this);
        initTouchEvent();
    }


    /**
     * 점자 학습에 필요한 점자 data를 얻는 함수
     * @param context : 현재 Activity에 대한 context
     * @param jsonFileName : Json File로 구성되어 있는 메뉴의 경우, 해당 file의 이름을 갖고 있는 변수
     * @param databaseFileName : SQLite database에 저장되어 있는 메뉴의 경우, database의 이름을 갖고 있는 변수
     * @param brailleLearningType : 진행하려는 점자 학습의 type
     * @return : 점자 data를 갖고 있는 BrailleData 리턴
     */
    private ArrayList<BrailleData> getBrailleDataArray(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType) {
        BrailleDataManager brailleDataManager = new BrailleDataManager(context, jsonFileName, databaseFileName, brailleLearningType);
        GettingBraille gettingBraille = brailleDataManager.
                getBrailleArrayList();

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
    private SingleFingerFunction getSingleFingerFunction(Context context, BrailleLearningType brailleLearningType){
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
    protected void refreshData(){
        if(0 <= pageNumber && pageNumber < brailleDataArrayList.size()){
            data = brailleDataArrayList.get(pageNumber);
            if(data != null)
                mediaSoundManager.start(data.getRawId());
        }
    }


    /**
     * customTouch class선언
     */
    private void initTouchEvent(){
        customTouchConnectListener = new CustomLearningTouchEvent(context, this);
        connectTouchEvent();
    }


    /**
     * customTouch class와 interface로 연결
     */
    private void connectTouchEvent(){
        customTouchConnectListener.onResume();
    }


    /**
     * customTouch class pause
     */
    protected void pauseTouchEvent(){
        customTouchConnectListener.onPause();
    }


    /**
     * viewObserver에게 데이터를 알림
     */
    @Override
    public void nodifyObservers() {
        refreshData();
        connectTouchEvent();
        if(data != null)
            viewObservers.nodifyBraille(data.getLetterName(), data.getBrailleMatrix());
    }


    /**
     * 일시정지 되었을 때 함수
     */
    @Override
    public void onPause() {
        customTouchConnectListener.setTouchLock(false);
        mediaSoundManager.stop();
        pauseTouchEvent();
    }

    @Override
    public void onFocusRefresh() {
    }


    /**
     * mediaSound stop 함수
     */
    @Override
    public void onStopSound() {
        mediaSoundManager.stop();
    }


    /**
     * 제스처 분석 실패시 다시 시도 멘트 출력 함수
     */
    @Override
    public void onError() {
        FingerFunctionType type = FingerFunctionType.NONE;
        mediaSoundManager.start(type);
    }


    /**
     * 손가락 1개를 화면에서 떨어트렸을 경우
     * @param fingerCoordinate : 좌표값
     */
    @Override
    public void onOneFingerFunction(FingerCoordinate fingerCoordinate) {
        oneFingerFunction.init();
    }


    /**
     * 손가락 1개를 화면에 터치하거나 드래그할 경우
     * @param fingerCoordinate : 좌표값
     */
    @Override
    public void onOneFingerMoveFunction(FingerCoordinate fingerCoordinate) {
        if (data != null)
            oneFingerFunction.oneFingerFunction(data.getBrailleMatrix(), fingerCoordinate);
        else
            oneFingerFunction.oneFingerFunction(null, fingerCoordinate);
    }


    /**
     * 손가락 2개를 화면에서 드래그하였을 경우
     * BACK : 뒤로가기
     * RIGHT : 오른쪽 페이지 이동
     * LEFT : 왼쪽 페이지 이동
     * @param fingerFunctionType : 제스처 타입
     */
    @Override
    public void onTwoFingerFunction(FingerFunctionType fingerFunctionType) {
        if(fingerFunctionType == FingerFunctionType.BACK)
            exit();
        else {
            switch (fingerFunctionType) {
                case RIGHT:
                    if (pageNumber < brailleDataArrayList.size() - 1) {
                        pageNumber++;
                        nodifyObservers();
                    } else {
                        mediaSoundManager.allStop();
                        mediaSoundManager.start(R.raw.last_page);
                    }
                    break;
                case LEFT:
                    if (0 < pageNumber) {
                        pageNumber--;
                        nodifyObservers();
                    } else {
                        mediaSoundManager.allStop();
                        mediaSoundManager.start(R.raw.first_page);
                    }
                    break;
            }
        }
    }


    /**
     * 손가락 1개, 2개, 3개에 대한 event를 처리하는 touchEvent
     * @param event : touchEvent를 관리하는 변수
     */
    @Override
    public void touchEvent(MotionEvent event) {
        if(customTouchConnectListener != null)
            customTouchConnectListener.touchEvent(event);
    }

    /**
     * 학습화면 종료 함수
     */
    protected void exit(){
        controlListener.exit();
    }


    /**
     * 특수기능 안내 멘트 및 화면 이미지 set 함수
     */
    @Override
    public void onSpecialFunctionGuide() {
        int size = specialFunctionManager.getSize()-1;
        if(size < specialFunctionIndex)
            specialFunctionIndex = 0;

        int drawableId = specialFunctionManager.getDrawableId(specialFunctionIndex);
        int soundId = specialFunctionManager.getSoundId(specialFunctionIndex++);
        viewObservers.onSpecialFunctionEnable(drawableId);
        mediaSoundManager.start(soundId);
    }


    /**
     * 특수기능 취소시 발생되는 함수
     * 현재 재생중인 음성 중지 -> 특수기능 취소 멘트 출력 -> 화면정보 새로고침
     */
    @Override
    public void onSpecialFunctionDisable() {
        onSpecialFunctionViewOff();
        mediaSoundManager.allStop();
        mediaSoundManager.start(R.raw.specialfunction_cancel);
        nodifyObservers();
    }


    /**
     * 특수기능 이미지 숨기는 함수
     * viewObserver를 통해 이미지 INVISIBLE 후 메모리 해제
     */
    private void onSpecialFunctionViewOff(){
        viewObservers.onSpecialFunctionDisable();
    }


    /**
     * 특수기능 실행 함수
     * 특수기능 이미지 숨긴 뒤 특수기능 실행
     */
    @Override
    public void onStartSpecialFunction() {
        onSpecialFunctionViewOff();
        int specialType = --specialFunctionIndex;
        specialFunctionManager.checkFunction(specialType);
    }


    /**
     * 화면 새로고침 함수
     */
    @Override
    public void onRefresh() {
        nodifyObservers();
    }

    @Override
    public void onSpeechRecognition() {
    }


    /**
     * 나만의 단어장 저장 함수
     * 현재 화면의 점자 정보를 dbManager로 전달
     */
    @Override
    public void onSaveMynote() {
        dbManager.saveMyNote(data.getLetterName(), data.getStrBrailleMatrix(), data.getAssistanceLetterName(), data.getRawId());
    }

    @Override
    public void onDeleteMynote() {
    }
}
