package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import android.os.Vibrator;
import android.view.MotionEvent;

import com.project.why.braillelearning.EnumConstant.BrailleLength;
import com.project.why.braillelearning.EnumConstant.DotType;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningModel.BasicLearningData;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.BasicLearningCoordinate;
import com.project.why.braillelearning.LearningModel.GettingBraille;
import com.project.why.braillelearning.LearningView.ViewObservers;
import com.project.why.braillelearning.Module.MediaPlayerModule;
import com.project.why.braillelearning.R;

import java.util.ArrayList;

/**
 * Created by hyuck on 2017-09-25.
 */

public class BasicLearningModule implements Control, FingerFunction {
    private final int ONE_FINGER = FingerFunctionType.ONE_FINGER.getNumber(); //손가락 1개
    private final int TWO_FINGER = FingerFunctionType.TWO_FINGER.getNumber(); // 손가락 2개
    private final int THREE_FINGER = FingerFunctionType.THREE_FINGER.getNumber(); // 손가락 3개
    private final int MAX_FINGER = THREE_FINGER;
    private boolean multiTouch = false;
    private MediaPlayerModule mediaPlayerModule;
    private ViewObservers viewObservers;
    private ArrayList<BrailleData> brailleDataArrayList;
    private int pageNumber = 0;
    private BrailleMatrixTranslationModule brailleMatrixTranslationModule;
    private BasicLearningData data;
    private FingerCoordinate fingerCoordinate;
    private OneFingerFunction oneFingerFunction;
    private TwoFingerFunction twoFingerFunction;

    BasicLearningModule(Context context, ArrayList<GettingBraille> brailleArrayList, BrailleLength brailleLength){
        brailleDataArrayList = brailleArrayList.get(0).getBrailleDataArray();
        brailleMatrixTranslationModule = new BrailleMatrixTranslationModule(brailleLength);
        mediaPlayerModule = new MediaPlayerModule(context);
        fingerCoordinate = new FingerCoordinate(MAX_FINGER);
        oneFingerFunction = new BasicOneFinger(context);
        twoFingerFunction = new BasicTwoFinger();
    }

    @Override
    public void addObservers(ViewObservers observers) {
        viewObservers = observers;
        initObservers();
        nodifyObservers();
    }

    @Override
    public void initObservers() {
        float bigCircle = brailleMatrixTranslationModule.getBigCircleRadius();
        float miniCircle = brailleMatrixTranslationModule.getMiniCircleRadius();

        viewObservers.initCircle(bigCircle, miniCircle);
    }

    @Override
    public void nodifyObservers() {
        if(pageNumber < brailleDataArrayList.size()) {
            data = brailleMatrixTranslationModule.translationBrailleMatrix(brailleDataArrayList.get(pageNumber));
            viewObservers.nodifyBraille(data);
        }
    }

    @Override
    public boolean touchEvent(MotionEvent event) {
        int pointer_Count = event.getPointerCount(); // 현재 발생된 터치 event의 수

        if(pointer_Count > THREE_FINGER) // 발생된 터치 이벤트가 3개를 초과하여도 3개까지만 인식
            pointer_Count = THREE_FINGER;

        if(pointer_Count == ONE_FINGER) {
            switch(event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: // 손가락 1개를 화면에 터치하였을 때 발생되는 Event
                    fingerCoordinate.setDownCoordinate(event, pointer_Count);
                    oneFinegerFunction();
                    break;
                case MotionEvent.ACTION_MOVE:
                    fingerCoordinate.setDownCoordinate(event, pointer_Count);
                    oneFinegerFunction();
                    break;
                case MotionEvent.ACTION_UP: // 손가락 1개를 화면에서 떨어트렸을 때 발생되는 Event
                    oneFingerFunction.initCoordinate();
                    multiTouch = false;
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

                    if(pointer_Count == TWO_FINGER){
                        if(twoFingerFunction() == true)
                            return true;
                        else
                            nodifyObservers();
                    } else if(pointer_Count == THREE_FINGER){

                    }
                    break;
            }

        }
        return false;
    }

    @Override
    public boolean oneFinegerFunction() {
        if(multiTouch == false) {
            int downX[] = fingerCoordinate.getDownX();
            int downY[] = fingerCoordinate.getDownY();
            oneFingerFunction.oneFingerFunction(data, downX[0], downY[0]);
        }
        return false;
    }

    @Override
    public boolean twoFingerFunction() {
        int downX[] = fingerCoordinate.getDownX();
        int downY[] = fingerCoordinate.getDownY();
        int upX[] = fingerCoordinate.getUpX();
        int upY[] = fingerCoordinate.getUpY();

        FingerFunctionType type = twoFingerFunction.getTwoFingerFunctionType(downX, downY, upX, upY);

        switch(type){
            case BACK: // 상위 메뉴
                return true;
            case NEXT: // 오른쪽 메뉴
                if(pageNumber+1 < brailleDataArrayList.size()) {
                    pageNumber++;
                    mediaPlayerModule.SoundPlay(type.getNumber(), "initial_giyeok");
                    //mediaPlayerModule.SoundPlay(type.getNumber(), FingerFunctionType.NONE.getNumber());

                    return false;
                } else
                    return true;
            case PREVIOUS: // 왼쪽 메뉴
                if(0 <= pageNumber-1) {
                    pageNumber--;
                    mediaPlayerModule.SoundPlay(type.getNumber(), FingerFunctionType.NONE.getNumber());
                }
                return false;
            case SPECIAL: // 특수기능
                return false;
            default:
                return false;
        }
    }

    @Override
    public boolean threeFingerFunction() {
        return false;
    }
}
