package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.EnumConstant.TouchLock;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.Module.BrailleTranslationModule;
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
 * 점자 번역 메뉴를 위한 모듈
 */
public class TranslationControl extends BasicControl implements SpeechRecognitionListener {
    private BrailleTranslationModule brailleTranslationModule;
    private SpeechRecognitionModule speechRecognitionMoudle;

    TranslationControl(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType, ControlListener controlListener){
        super(context, jsonFileName, databaseFileName, brailleLearningType, controlListener);
        brailleTranslationModule = new BrailleTranslationModule(context);
        speechRecognitionMoudle = new SpeechRecognitionModule(context, this);
    }


    /**
     * data를 새로고침하는 함수.
     * pageNumber에 따라 점자 data를 선택함
     */
    @Override
    protected void refreshData(){
        if(0 <= pageNumber && pageNumber < brailleDataArrayList.size()){
            data = brailleDataArrayList.get(pageNumber);
            if(data != null)
                mediaSoundManager.start(data.getRawId());
        } else
            mediaSoundManager.start(R.raw.translation_guide);
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
     * 음성인식 특수기능 함수
     * 제스처 기능을 막기위해 lock을 건 뒤, 음성인식 실행
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
     * 나만의 단어장 특수기능 함수
     * 현재 화면의 점자정보를 dbManger로 전달하여 db에 저장
     * 화면에 점자가 존재하지 않는다면 안내멘트 출력
     */
    @Override
    public void onSaveMynote() {
        if (data != null)
            dbManager.saveMyNote(data.getLetterName(), data.getStrBrailleMatrix(), data.getAssistanceLetterName(), data.getRawId());
        else
            mediaSoundManager.start(R.raw.impossble_save);
    }


    /**
     * 음성인식에 대한 callback method
     * 음성인식에 대한 ArrayList가 전달됨
     */
    @Override
    public void speechRecogntionResult(ArrayList<String> text) {
        if(text != null)
            startBrailleTranslation(text.get(0));
        else {
            mediaSoundManager.start(R.raw.speechrecognition_fail);
            mediaSoundManager.start(R.raw.retry);
        }
        customTouchConnectListener.setTouchLock(TouchLock.UNLOCK);
    }


    /**
     * 점자 번역을 위한 함수
     * 전달된 글자를 번역함
     * @param letterName : 음성인식 후 전달된 글자
     */
    private void startBrailleTranslation(String letterName){
        BrailleData translationBrailleData = brailleTranslationModule.translation(letterName);
        if(translationBrailleData != null) {
            brailleDataArrayList.add(translationBrailleData);
            if(pageNumber < brailleDataArrayList.size()-1)
                pageNumber = brailleDataArrayList.size() - 1;
            nodifyObservers();
        }
        else
            mediaSoundManager.start("brailletranslation_fail");
    }


    /**
     * 학습화면 종료 함수
     */
    @Override
    public void exit(){
        speechRecognitionMoudle.stop();
        controlListener.exit();
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
