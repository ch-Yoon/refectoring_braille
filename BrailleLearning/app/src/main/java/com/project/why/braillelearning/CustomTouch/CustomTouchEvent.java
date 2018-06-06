package com.project.why.braillelearning.CustomTouch;

import android.content.Context;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityManager;
import com.project.why.braillelearning.Accessibility.AccessibilityEventListener;
import com.project.why.braillelearning.Accessibility.AccessibilityEventSingleton;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.TouchLock;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningControl.FingerCoordinate;
import com.project.why.braillelearning.LearningControl.MultiFinger;
import com.project.why.braillelearning.LearningView.ActivityManagerSingleton;
import com.project.why.braillelearning.MediaPlayer.MediaPlayerStopCallbackListener;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;
import com.project.why.braillelearning.R;
import java.util.Timer;
import java.util.TimerTask;
import static android.content.Context.ACCESSIBILITY_SERVICE;


/**
 * Created by hyuck on 2018-01-18.
 * touch 판독 모듈 class
 * 해당 클래스에서 손가락 1개, 2개, 3개 이상을 구분하고, touch 좌표값을 이용하여 제스처를 분석한다.
 * 분석된 제스처와 AccessibilityEventSingleton으로부터 수신된 double tab은 CustomTouchEventListener를 통해 연결된 class로 송신한다.
 */
public class CustomTouchEvent implements CustomTouchConnectListener, AccessibilityEventListener{
    protected int ONE_FINGER = FingerFunctionType.ONE_FINGER.getNumber(); // 손가락 1개
    protected int TWO_FINGER = FingerFunctionType.TWO_FINGER.getNumber(); // 손가락 2개
    protected FingerCoordinate fingerCoordinate;
    protected boolean multiFinger = false; // 멀티터치 체크 변수
    protected boolean blindMode = false;
    protected boolean hoverError = false;
    private long actionDownTime = 0;
    private long actionUpTime = 0;
    private int touchCount = 0;
    private TimerTask blindTimerTask, basicTimerTask, specialFunctionTimerTask;
    private Timer blindTimer, basicTimer, specialFunctionTimer;
    private AccessibilityManager am;
    protected AccessibilityEventSingleton accessibilityEventSingleton;
    private Context context;
    protected CustomTouchEventListener customTouchEventListener;
    private MediaSoundManager mediaSoundManager;
    private boolean maxFingerExceed = false;
    private MultiFinger multiFingerModule;
    private ActivityManagerSingleton activityManager;
    private int specialFunctionTime = 0;
    protected TouchLock lockType = TouchLock.UNLOCK;

    public CustomTouchEvent(Context context, CustomTouchEventListener customTouchEventListener){
        this.context = context;
        fingerCoordinate = new FingerCoordinate(TWO_FINGER);
        am = (AccessibilityManager) context.getSystemService(ACCESSIBILITY_SERVICE);
        this.customTouchEventListener = customTouchEventListener;
        mediaSoundManager = new MediaSoundManager(context);
        multiFingerModule = new MultiFinger(context);
        activityManager = ActivityManagerSingleton.getInstance();
    }


    /**
     * app이 재개될 경우, 접근성 service 연결
     */
    @Override
    public void onResume() {
        setBlindPerson();
    }


    /**
     * app이 onPause상태에 놓일 경우
     * 접근성 service 초기화
     * mediaSoundManager 연결 해제
     * 특수기능 thread 초기화
     */
    @Override
    public void onPause() {
        initializeBlindPerson();
        mediaSoundManager.setMediaPlayerStopCallbackListener(null);
        specialFunctionWaitThreadStop();
        setTouchLock(TouchLock.UNLOCK);
    }


    /**
     * 현재 클래스와 accessibilityEventSingleton을 연결하는 함수
     */
    private void setBlindPerson(){
        blindMode = am.isTouchExplorationEnabled();
        if(blindMode == true) {
            if (accessibilityEventSingleton == null) {
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
            accessibilityEventSingleton.onPause();
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
            if(accessibilityEventSingleton != null) {
                if(Global.blindPermissionCheck == false)
                    accessibilityEventSingleton.checkPermissions();
            }
            else
                setBlindPerson();
        }
    }


    /**
     * 다른 class에서 음성인식을 실행할 경우, 제스처를 막기 위해 존재하는 함수
     * lockType에 따라 무시되는 제스처가 다르다.
     * @param lockType : UNLOCK, SPEECH_RECOGNITION_LOCK, SPECIAL_FUNCTION_LOCK, PERMISSION_CHECK_LOCK, MENU_GUIDE_LOCK
     */
    @Override
    public void setTouchLock(TouchLock lockType) {
        this.lockType = lockType;
    }


    /**
     * AccessibilityEventSingleton로부터 수신된 double tab event를 touch event에 전달.
     * @param event ACTION_DOWN 혹은 ACTION_UP 이벤트
     */
    @Override
    public void pushDoubleTab(MotionEvent event) {
        touchEvent(event);
    }


    /**
     * 손가락 1개, 2개, 3개 이상을 분석하는 메소드
     * 일반 버전과 시각장애인 버전을 구별하여 event를 분석한다.
     * @param event : touch event
     */
    @Override
    public void touchEvent(MotionEvent event){
        checkBlind(event);

        if(maxFingerExceed == true)
            checkMaxFingerExceedInit(event);

        if(maxFingerExceed == false) {
            int pointer_Count = event.getPointerCount(); // 현재 발생된 터치 event의 수
            if (pointer_Count <= TWO_FINGER) {
                if (blindMode == false) {
                    if (pointer_Count == ONE_FINGER)
                        basicOneFingerTouch(event);
                    else {
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_POINTER_DOWN : // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                                twoFingerDown(event, pointer_Count);
                                break;
                            case MotionEvent.ACTION_POINTER_UP : // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                                twoFingerUp(event, pointer_Count);
                                break;
                        }
                    }
                } else {
                    if (pointer_Count == ONE_FINGER)
                        blindOneFIngerTouch(event);
                    else {
                        switch (event.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_POINTER_DOWN: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                                twoFingerDown(event, pointer_Count);
                                break;
                            case MotionEvent.ACTION_POINTER_UP: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                                twoFingerUp(event, pointer_Count);
                                break;
                        }
                    }
                }
            } else {
                maxFingerExceed = true; //손가락이 3개이상 터치되었을 때
                if(lockType == TouchLock.UNLOCK)
                    mediaSoundManager.start(R.raw.jesture_info);
            }
        }
    }


    /**
     * 화면이 터치될 때, 현재 화면이 시각장애인 버전인지, 일반 버전인지 확인하는 함수
     * 화면에 터치가 시작될 때 확인한다.
     * @param event : touch event
     */
    private void checkBlind(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER:
                checkBlindPerson();
                break;
            case MotionEvent.ACTION_DOWN:
                checkBlindPerson();
                break;
        }
    }


    /**
     * 손가락이 3개 이상 터치되었을 때, 안내메시지를 위해 touch event에 lock이 걸린것을 풀어주는 메소드
     * ACTION_HOVER_ENTER 혹은 ACTION_DOWN이 발생되었을 때, 즉 최초 터치가 발생되었을 때 풀어준다
     * @param event : touch event
     */
    private void checkMaxFingerExceedInit(MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_HOVER_ENTER:
                maxFingerExceed = false;
            case MotionEvent.ACTION_DOWN:
                maxFingerExceed = false;
                break;
        }
    }


    /**
     * 일반버전 손가락 1개 함수
     * down 될때 doubleTabCheckThread 생성, up 될때 터치횟수 확인.
     * 더블탭으로 판정되었을 때, 특수기능 선택 화면이라면 특수기능 실행
     * 더블탭으로 판정되었을 때, 특수기능 선택 화면이 아니라면 화면 접속 기능 실행
     * @param event : touch event
     */
    protected void basicOneFingerTouch(MotionEvent event){
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                basicDoubleTabCheckThreadMaking();
                touchCount++;
                multiFinger = false;
                if(lockType == TouchLock.UNLOCK)
                    customTouchEventListener.onFocusRefresh();
                break;
            case MotionEvent.ACTION_UP:
                if(multiFinger == false) {
                    touchCount++;
                    if (4 <= touchCount) {
                        basicDoubleTabCheckThreadStop();

                        if(lockType == TouchLock.SPECIAL_FUNCTION_LOCK){
                            specialFunctionWaitThreadStop();
                            customTouchEventListener.onStartSpecialFunction();
                        } else if(lockType == TouchLock.PERMISSION_CHECK_LOCK)
                            customTouchEventListener.onPermissionUseAgree();
                        else
                            customTouchEventListener.onOneFingerFunction(fingerCoordinate);
                    }
                }
                break;
        }
    }


    /**
     * 일반 버전의 더블탭 확인을 위한 시간 스레드를 생성하는 함수
     * 0.5초 안에 해당 스레드가 중지되지 않는다면 double tab으로 판정되지 않는다.
     */
    private synchronized void basicDoubleTabCheckThreadMaking(){
        if(basicTimerTask == null) {
            basicTimerTask = new TimerTask() {
                @Override
                public void run() {
                    basicDoubleTabCheckThreadStop();
                }
            };
            basicTimer = new Timer();
            basicTimer.schedule(basicTimerTask, 500);
        }
    }


    /**
     * 더블 탭 확인을 위한 시간 스레드를 종료하는 함수
     */
    private void basicDoubleTabCheckThreadStop(){
        touchCount = 0;

        if(basicTimerTask != null) {
            basicTimerTask.cancel();
            basicTimerTask = null;
        }

        if(basicTimer != null){
            basicTimer.cancel();
            basicTimer = null;
        }
    }


    /**
     * 시각장애인버전의 손가락 1개 함수
     * ACTION_DOWN의 경우, down time과 up time이 일치한다면 더블 탭, 아니라면 손가락 2개로 인식한다.
     * @param event
     */
    protected  void blindOneFIngerTouch(MotionEvent event){
        switch (event.getAction()) {
            case MotionEvent.ACTION_HOVER_ENTER:
                multiFinger = false;
                hoverError = false;
                blindDoubleTabCheckThreadStop();
                accessibilityEventSingleton.setFocusState(true);
                if(lockType == TouchLock.UNLOCK)
                    customTouchEventListener.onFocusRefresh();
                break;
            case MotionEvent.ACTION_DOWN:
                multiFinger = false;
                hoverError = false;
                blindDoubleTabCheckThreadStop();
                if(lockType == TouchLock.UNLOCK)
                    customTouchEventListener.onStopSound();
                actionDownTime = event.getEventTime();
                fingerCoordinate.setHoverDownCoordinate(event, ONE_FINGER + 1);
                break;
            case MotionEvent.ACTION_MOVE:
                if(hoverError == false) {
                    hoverError = fingerCoordinate.checkHoverError(event);
                    if(hoverError == false)
                        fingerCoordinate.setHoverUpCoordinate(event, ONE_FINGER + 1);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(multiFinger == false) {
                    actionUpTime = event.getEventTime();
                    if (actionDownTime == actionUpTime) {
                        multiFinger = false;
                        blindDoubleTabCheckThreadMaking();
                    } else {
                        if (hoverError == false)
                            twoFingerUp(event, ONE_FINGER + 1);
                        else {
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
    private synchronized void blindDoubleTabCheckThreadMaking(){
        if(blindTimerTask == null) {
            blindTimerTask = new TimerTask() {
                @Override
                public void run() {
                    activityManager.getNowActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(lockType == TouchLock.SPECIAL_FUNCTION_LOCK){
                                activityManager.getNowActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        specialFunctionWaitThreadStop();
                                        customTouchEventListener.onStartSpecialFunction();
                                    }
                                });
                            } else if(lockType == TouchLock.PERMISSION_CHECK_LOCK) {
                                customTouchEventListener.onPermissionUseAgree();
                            } else
                                customTouchEventListener.onOneFingerFunction(fingerCoordinate);

                            blindDoubleTabCheckThreadStop();
                            initActionTime();
                        }
                    });
                }
            };
            blindTimer = new Timer();
            blindTimer.schedule(blindTimerTask, 200);
        }
    }


    /**
     * 시각장애인 버전의 blind event를 식별하기 위한 thread를 중지하는 함수
     * thread 중지 후 모두 초기화
     */
    private void blindDoubleTabCheckThreadStop(){
        if(blindTimerTask != null) {
            blindTimerTask.cancel();
            blindTimerTask = null;
        }

        if(blindTimer != null){
            blindTimer.cancel();
            blindTimer = null;
        }

        initActionTime();
    }


    /**
     * ACTION_DOWN과 ACTION_UP의 이벤트 발생 시간을 초기화 시키는 함수
     */
    private void initActionTime(){
        actionDownTime = 0;
        actionUpTime = 0;
    }


    /**
     * 손가락 2개 touch down 분석 함수
     * 시각장애인 버전과 일반 버전을 분리하여 분석한다.
     * @param event : touch event
     * @param pointer_Count : 화면에 터치된 손가락 갯수
     */
    private void twoFingerDown(MotionEvent event, int pointer_Count){
        multiFinger = true;

        if(lockType == TouchLock.UNLOCK)
            customTouchEventListener.onStopSound();

        if(blindMode == true)
            blindDoubleTabCheckThreadStop();
        else
            basicDoubleTabCheckThreadStop();

        fingerCoordinate.setDownCoordinate(event, pointer_Count);
    }


    /**
     * 손가락 2개 touch up 분석 함수
     * 시각장애인 버전과 일반 버전을 분리하여 분석한다.
     * 시각장애인 버전의 경우, 손가락 2개를 화면에 얹였을 때, ACTION_DOWN이 호출되면서 손가락 1개가 화면에 터치되었다고 인식됨
     * @param event : touch event
     * @param pointer_Count : 화면에 터치된 손가락의 갯수
     */
    protected void twoFingerUp(MotionEvent event, int pointer_Count){
        if(pointer_Count == ONE_FINGER)
            fingerCoordinate.setUpCoordinate(event, pointer_Count);
        else
            fingerCoordinate.setHoverUpCoordinate(event, pointer_Count);

        FingerFunctionType type;
        if(lockType != TouchLock.UNLOCK)
            type = multiFingerModule.getFingerFunctionType(fingerCoordinate, true);
        else
            type = multiFingerModule.getFingerFunctionType(fingerCoordinate, false);

        if(type == FingerFunctionType.BACK) {
            if(lockType == TouchLock.SPECIAL_FUNCTION_LOCK){
                specialFunctionWaitThreadStop();
                customTouchEventListener.onSpecialFunctionDisable();
            } else if(lockType == TouchLock.PERMISSION_CHECK_LOCK){
                setTouchLock(TouchLock.UNLOCK);
                customTouchEventListener.onPermissionUseDisagree();
            } else
                customTouchEventListener.onTwoFingerFunction(type);
        } else {
            if(type == FingerFunctionType.SPECIAL){
                if(lockType == TouchLock.UNLOCK || lockType == TouchLock.SPECIAL_FUNCTION_LOCK)
                    onSpecialFunctionReady();
            } else {
                if(lockType == TouchLock.UNLOCK)
                    customTouchEventListener.onTwoFingerFunction(type);
            }
        }
    }


    /**
     * 특수기능 사용을 위해 준비상태에 돌입하는 함수
     * 해당 함수가 발동되면, 화면에 특수기능 사용을 위한 view와 음성 안내 멘트가 출력된다.
     * 특수기능 사용을 위한 안내멘트가 종료되면 안내멘트 종료를 알기 위한 listener를 mediaSoundManager에 등록한다.
     * mediaSoundManager로부터 음성 file 중지 callback 함수가 불리면 특수기능 대기시간 3초를 위한 wait thread를 호출한다.
     */
    private void onSpecialFunctionReady(){
        specialFunctionWaitThreadStop();
        setTouchLock(TouchLock.SPECIAL_FUNCTION_LOCK);
        customTouchEventListener.onSpecialFunctionGuide();
        mediaSoundManager.setMediaPlayerStopCallbackListener(new MediaPlayerStopCallbackListener() {
            @Override
            public void mediaPlayerStop() {
                specialFunctionWaitThreadStart();
            }
        });
    }


    /**
     * 3초간의 대기 시간 안에 더블 탭이 발동되면 특수기능을 실행한다.
     * 3초간의 대기 시간 안에 더블 탭이 발동되지 않으면 특수기능은 실행하지 않는다.
     */
    private void specialFunctionWaitThreadStart(){
        if(lockType == TouchLock.SPECIAL_FUNCTION_LOCK) {
            if (specialFunctionTimerTask == null) {
                specialFunctionTimerTask = new TimerTask() {
                    @Override
                    public void run() {
                        activityManager.getNowActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if(mediaSoundManager.getMediaPlaying() == false) {
                                    if (specialFunctionTime == 3) {
                                        specialFunctionTime = 0;
                                        specialFunctionWaitThreadStop();
                                        customTouchEventListener.onSpecialFunctionDisable();
                                    } else {
                                        specialFunctionTime++;
                                        mediaSoundManager.start(R.raw.clock);
                                    }
                                }
                            }
                        });
                    }
                };
                specialFunctionTimer = new Timer();
                specialFunctionTimer.schedule(specialFunctionTimerTask, 0, 1000);
            }
        }
    }


    /**
     * 특수기능 실행을 위해 3초간의 대기를 하는 스레드를 초기화 하는 함수
     */
    private void specialFunctionWaitThreadStop(){
        lockType = TouchLock.UNLOCK;
        specialFunctionTime = 0;


        if(specialFunctionTimerTask != null) {
            specialFunctionTimerTask.cancel();
            specialFunctionTimerTask = null;
        }

        if(specialFunctionTimer != null){
            specialFunctionTimer.cancel();
            specialFunctionTimer = null;
        }

        mediaSoundManager.stop();
    }
}