package com.project.why.braillelearning.SpecialFunction;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.SpecialFunctionType;
import com.project.why.braillelearning.LearningModel.SpecialFunctionData;
import com.project.why.braillelearning.R;

import java.util.ArrayList;

/**
 * Created by hyuck on 2018-02-06.
 */


/**
 * 특수기능 정보를 관리하는 manager class
 * 학습 타입에 맞는 특수기능 정보들을 arraylist에 보관한다.
 * specialFunctionListener로 연결된 class로부터 수신한 값을 기반으로 특수기능 실행명령을 전달
 */
public class SpecialFunctionManager {
    private ArrayList<SpecialFunctionData> specialFunctionDataArrayList = new ArrayList<>();
    private SpecialFunctionListener specialFunctionListener;

    public SpecialFunctionManager(BrailleLearningType brailleLearningType, SpecialFunctionListener specialFunctionListener){
        setSpecialFunctionDataArrayList(brailleLearningType);
        this.specialFunctionListener = specialFunctionListener;
    }

    private void setSpecialFunctionDataArrayList(BrailleLearningType brailleLearningType){
        switch(brailleLearningType){
            case MENU:
                addArrayList(R.drawable.refresh, R.raw.refreshfunction, SpecialFunctionType.REFRESH);
                break;
            case MENUINFO:
                addArrayList(R.drawable.refresh, R.raw.refreshfunction, SpecialFunctionType.REFRESH);
                break;
            case BASIC:
                addArrayList(R.drawable.refresh, R.raw.refreshfunction, SpecialFunctionType.REFRESH);
                addArrayList(R.drawable.save, R.raw.savefunction, SpecialFunctionType.SAVE);
                break;
            case MASTER:
                addArrayList(R.drawable.refresh, R.raw.refreshfunction, SpecialFunctionType.REFRESH);
                addArrayList(R.drawable.save, R.raw.savefunction, SpecialFunctionType.SAVE);
                break;
            case TRANSLATION:
                addArrayList(R.drawable.refresh, R.raw.refreshfunction, SpecialFunctionType.REFRESH);
                addArrayList(R.drawable.save, R.raw.savefunction, SpecialFunctionType.SAVE);
                addArrayList(R.drawable.speechfunction, R.raw.speechfunction, SpecialFunctionType.SPEECH);
                break;
            case QUIZ:
                addArrayList(R.drawable.refresh, R.raw.refreshfunction, SpecialFunctionType.REFRESH);
                addArrayList(R.drawable.save, R.raw.savefunction, SpecialFunctionType.SAVE);
                addArrayList(R.drawable.speechfunction, R.raw.speechfunction, SpecialFunctionType.SPEECH);
                break;
            case MYNOTE:
                addArrayList(R.drawable.refresh, R.raw.refreshfunction, SpecialFunctionType.REFRESH);
                addArrayList(R.drawable.delete, R.raw.deletefunction, SpecialFunctionType.DELETE);
                break;
            case TEACHER:
                addArrayList(R.drawable.refresh, R.raw.refreshfunction, SpecialFunctionType.REFRESH);
                addArrayList(R.drawable.speechfunction, R.raw.speechfunction, SpecialFunctionType.SPEECH);
                break;
            case STUDENT:
                addArrayList(R.drawable.refresh, R.raw.refreshfunction, SpecialFunctionType.REFRESH);
                addArrayList(R.drawable.save, R.raw.savefunction, SpecialFunctionType.SAVE);
                addArrayList(R.drawable.speechfunction, R.raw.speechfunction, SpecialFunctionType.SPEECH);
                break;
        }
    }

    private void addArrayList(int drawableId, int rawId, SpecialFunctionType specialFunctionType){
        SpecialFunctionData data = new SpecialFunctionData(drawableId, rawId, specialFunctionType);
        specialFunctionDataArrayList.add(data);
    }

    public int getSize(){
        return specialFunctionDataArrayList.size();
    }

    public int getDrawableId(int index){
        return specialFunctionDataArrayList.get(index).getDrawableId();
    }

    public int getSoundId(int index){
        return specialFunctionDataArrayList.get(index).getSoundId();
    }


    /**
     * specialFunctionListener로 연결된 class로부터 수신한 값을 기반으로 특수기능 실행명령을 전달
     * @param index
     */
    public void checkFunction(int index){
        SpecialFunctionType type = specialFunctionDataArrayList.get(index).getSpecialFunctionType();
        switch (type){
            case REFRESH:
                specialFunctionListener.onRefresh();
                break;
            case SAVE:
                specialFunctionListener.onSaveMynote();
                break;
            case DELETE:
                specialFunctionListener.onDeleteMynote();
                break;
            case SPEECH:
                specialFunctionListener.onSpeechRecognition();
                break;
        }
    }
}
