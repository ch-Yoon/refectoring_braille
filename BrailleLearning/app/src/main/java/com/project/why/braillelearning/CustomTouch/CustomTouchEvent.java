package com.project.why.braillelearning.CustomTouch;

import android.content.Context;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityManager;

import com.project.why.braillelearning.Accessibility.AccessibilityEventListener;
import com.project.why.braillelearning.Accessibility.AccessibilityEventSingleton;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.LearningControl.FingerCoordinate;

import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.ACCESSIBILITY_SERVICE;

/**
 * Created by hyuck on 2018-01-18.
 * touch 판독 모듈 class
 * 해당 클래스에서 손가락 1개, 2개, 3개를 구분하고, touch 좌표값을 CustomTouchEventListener를 통해 연결된 class로 송신한다.
 * AccessibilityEventSingleton에서 넘어온 double tab도 송신한다.
 */

public class CustomTouchEvent implements CustomTouchConnectListener, AccessibilityEventListener {
    protected int ONE_FINGER = FingerFunctionType.ONE_FINGER.getNumber(); // 손가락 1개
    protected int TWO_FINGER = FingerFunctionType.TWO_FINGER.getNumber(); // 손가락 2개
    protected int THREE_FINGER = FingerFunctionType.THREE_FINGER.getNumber(); // 손가락 3개
    protected FingerCoordinate fingerCoordinate;
    protected boolean multiFinger = false; // 멀티터치 체크 변수
    protected boolean functionLock = false;
    protected boolean blindMode = false;
    protected boolean hoverError = false;
    private long actionDownTime = 0;
    private long actionUpTime = 0;
    private TimerTask touchTimerTask;
    private Timer touchTimer;
    private AccessibilityManager am;
    protected AccessibilityEventSingleton accessibilityEventSingleton;
    private Context context;
    private CustomTouchEventListener customTouchEventListener;

    public CustomTouchEvent(Context context, CustomTouchEventListener customTouchEventListener){
        this.context = context;
        fingerCoordinate = new FingerCoordinate(THREE_FINGER);
        am = (AccessibilityManager) context.getSystemService(ACCESSIBILITY_SERVICE);
        this.customTouchEventListener = customTouchEventListener;
    }

    @Override
    public void onResume() {
        setBlindPerson();
    }

    @Override
    public void onPause() {
        initializeBlindPerson();
    }


    /**
     * AccessibilityEventSingleton로부터 수신한 double tab event를 customTouchEventListener로 전달한다.
     * @param event ACTION_DOWN 혹은 ACTION_UP 이벤트
     */
    @Override
    public void pushDoubleTab(MotionEvent event) {
        if(customTouchEventListener != null){
            touchEvent(event);
        }
    }


    /**
     * 현재 클래스와 accessibilityEventSingleton을 연결하는 함수
     */
    private void setBlindPerson(){
        blindMode = am.isTouchExplorationEnabled();
        if(blindMode == true) {
            if(accessibilityEventSingleton == null) {
                accessibilityEventSingleton = AccessibilityEventSingleton.getInstance();
                accessibilityEventSingleton.setContext(context);
                accessibilityEventSingleton.setAccessibilityEventListener(this);
                checkBlindPerson();
            }
        }
    }


    /**
     * accessibilityEventSingleton을 초기화 하는 함수
     */
    private void initializeBlindPerson(){
        if(accessibilityEventSingleton != null) {
            accessibilityEventSingleton.initialize();
            accessibilityEventSingleton = null;
        }
    }


    /**
     * 현재 앱이 talkback이 활성화 되어 있는지 확인하고, 활성화 되어 있다면 accessibilityEventSingleton을 연결한다.
     * 연결되어 있다면, 접근성 권한을 확인한다.
     */
    private void checkBlindPerson(){
        blindMode = am.isTouchExplorationEnabled();
        if(blindMode == true) {
            if(accessibilityEventSingleton != null)
                accessibilityEventSingleton.checkPermissions();
            else
                setBlindPerson();
        }
    }

    /**
     * 손가락 1개, 2개, 3개를 구별하는 touchEvent
     * 일반 버전과 시각장애인 버전을 구별하여 event를 분석한다.
     * @param event
     */
    @Override
    public void touchEvent(MotionEvent event){
        checkBlindPerson();

        int pointer_Count = event.getPointerCount(); // 현재 발생된 터치 event의 수
        if(pointer_Count > THREE_FINGER)
            pointer_Count = THREE_FINGER;

        if(blindMode == false) {
            if (pointer_Count == ONE_FINGER)
                basicOneFingerTouch(event);
            else if (pointer_Count > ONE_FINGER) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_POINTER_DOWN: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                        multiFinger = true;
                        fingerCoordinate.setDownCoordinate(event, pointer_Count);
                        break;
                    case MotionEvent.ACTION_POINTER_UP: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                        if(functionLock == false) {
                            fingerCoordinate.setUpCoordinate(event, pointer_Count);

                            if (pointer_Count == TWO_FINGER)
                                customTouchEventListener.onTwoFingerFunction(fingerCoordinate);
                            else if (pointer_Count == THREE_FINGER)
                                customTouchEventListener.onThreeFingerFunction(fingerCoordinate);

                            functionLock = true;
                        }
                        break;
                }
            }
        } else {
            if (pointer_Count == ONE_FINGER)
                blindOneFIngerTouch(event);
            else if(pointer_Count > ONE_FINGER){
                switch (event.getAction()  & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_POINTER_DOWN: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                        multiFinger = true;

                        fingerCoordinate.setDownCoordinate(event, pointer_Count);
                        break;
                    case MotionEvent.ACTION_POINTER_UP: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                        if(functionLock == false) {
                            fingerCoordinate.setUpCoordinate(event, pointer_Count);

                            if (pointer_Count == TWO_FINGER)
                                customTouchEventListener.onTwoFingerFunction(fingerCoordinate);
                            else if (pointer_Count == THREE_FINGER)
                                customTouchEventListener.onThreeFingerFunction(fingerCoordinate);

                            functionLock = true;
                        }
                        break;
                }
            }
        }
    }

    /**
     * 일반버전의 손가락 1개 함수
     * 학습메뉴마다 손가락 1개의 이벤트가 다르기때문에, 해당 함수는 상속받는 클래스에서 재정의한다.
     * @param event
     */
    protected void basicOneFingerTouch(MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // 손가락 1개를 화면에 터치하였을 때 발생되는 Event
                multiFinger = false;
                functionLock = false;
                fingerCoordinate.setDownCoordinate(event, ONE_FINGER);
                customTouchEventListener.onStopSound();
                break;
            case MotionEvent.ACTION_UP: // 손가락 1개를 화면에서 떨어트렸을 때 발생되는 Event
                if (multiFinger == false) {
                    fingerCoordinate.setUpCoordinate(event, ONE_FINGER);
                    customTouchEventListener.onOneFingerFunction(fingerCoordinate);
                }
                break;
        }
    }


    /**
     * 시각장애인버전의 손가락 1개 함수
     * 학습메뉴마다 손가락 1개의 이벤트가 다르기때문에, 해당 함수는 상속받는 클래스에서 재정의한다.
     * @param event
     */
    protected  void blindOneFIngerTouch(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER:
                multiFinger = false;
                functionLock = false;
                hoverError = false;

                accessibilityEventSingleton.setFocusState(true);
                customTouchEventListener.onFocusRefresh();
                break;
            case MotionEvent.ACTION_DOWN: // 손가락 1개를 화면에 터치하였을 때 발생되는 Event
                multiFinger = false;
                functionLock = false;
                hoverError = false;

                accessibilityEventSingleton.touchCheckStop();
                threadStop();
                customTouchEventListener.onStopSound();
                actionDownTime = event.getEventTime();
                fingerCoordinate.setHoverDownCoordinate(event, ONE_FINGER + 1);
                break;
            case MotionEvent.ACTION_MOVE:
                if(hoverError == false) {
                    hoverError = fingerCoordinate.checkHoverError(event);
                    if(hoverError == false)
                        fingerCoordinate.setHoverUpCoordinate(event, ONE_FINGER + 1);
                } else
                    break;

                break;
            case MotionEvent.ACTION_UP:
                actionUpTime = event.getEventTime();

                if(multiFinger == false) {
                    if (actionDownTime == actionUpTime) {
                        multiFinger = false;
                        threadMaking();
                    } else {
                        multiFinger = true;
                        if (hoverError == false) {
                            fingerCoordinate.setHoverUpCoordinate(event, ONE_FINGER + 1);
                            customTouchEventListener.onTwoFingerFunction(fingerCoordinate);
                        } else {
                            customTouchEventListener.onError();
                            hoverError = false;
                        }
                    }
                }
                break;
        }
    }


    /**
     * 시각장애인 버전의 touch event를 식별하기 위한 thread
     * 손가락 1개로 화면을 두번 연속 탭 할 경우, ACTION_DOWN -> ACTION_UP이 호출된다.
     * 손가락 3개를 화면에 얹을경우, ACTION_DOWN -> ACTION_UP -> ACTION_DOWN -> ACTION_POINTER_DOWN(1) -> ACTION_POINTER_DOWN(2)이 호출된다.
     * 이때, 손가락 1개와 손가락 3개 모두 ACTOIN_DOWN -> ACTION_UP이라는 touch event가 중복된다.
     * 이에따라, ACTION_DOWN -> ACTION_UP으로 인한 double tab판정이 날 경우, ACTION_UP 이후 일정시간동안 대기를 하고, 추가적인 touch 이벤트가 없다면 double tap으로 간주한다.
     */
    private synchronized void threadMaking(){
        if(touchTimerTask == null) {
            touchTimerTask = new TimerTask() {
                @Override
                public void run() {
                    customTouchEventListener.onOneFingerFunction(fingerCoordinate);
                    threadStop();
                    initActionTime();
                }
            };
            touchTimer = new Timer();
            touchTimer.schedule(touchTimerTask, 100);
        }
    }

    private void threadStop(){
        if(touchTimerTask != null) {
            touchTimerTask.cancel();
            touchTimerTask = null;
        }

        if(touchTimer != null){
            touchTimer.cancel();
            touchTimer = null;
        }


        initActionTime();
    }

    private void initActionTime(){
        actionDownTime = 0;
        actionUpTime = 0;
    }
}
