package com.project.why.braillelearning.Accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import java.util.List;

/**
 * Created by hyuck on 2018-01-16.
 * Activity와 AccessibilityCheckService를 연결해주기 위한 singleton.
 * AccessibilityCheckService로부터 수신받은 touch 값을 분석하여, DoubleTab 판정이 날 경우, Activity로 Event를 push한다.
 */

public class AccessibilityEventSingleton implements AccessibilityCheckListener{
    private static final int TYPE_TOUCH_INTERACTION_START = 1048576; // 사용자가 화면을 터치하기 시작했을 때의 이벤트 상수값
    private static final int TYPE_TOUCH_INTERACTION_END = 2097152; // 사용자가 화면 터치를 종료하였을 떄의 이벤트 상수값
    private static final AccessibilityEventSingleton ourInstance = new AccessibilityEventSingleton();
    private AccessibilityEventListener accessibilityEventListener;
    private int screenTouchCount = 0;
    private Context context;
    private AccessibilityManager accessibilityManager;
    private boolean screenFocusState = false;

    public static AccessibilityEventSingleton getInstance() {
        return ourInstance;
    }

    public void setContext(Context context){
        ourInstance.context = context;
    }

    public void setAccessibilityEventListener(AccessibilityEventListener accessibilityEventListener){
        ourInstance.accessibilityEventListener = accessibilityEventListener;
    }

    private AccessibilityEventSingleton() {
    }


    /**
     * 화면에 Accessibility focus가 잡혔는지 안잡혔는지의 여부를 해당 함수를 통해 설정한다.
     * focus가 잡혀 현재 함수가 호출될 경우, 접근성 service에서 발생되는 pushEvent를 무시한다.
     * @param state : true(focus 잡힘), false(focus 안잡힘)
     */
    public void setFocusState(boolean state){
        screenFocusState = state;
    }


    /**
     * AccessibilityCheckService로부터 push를 받은 함수
     * AccessibilityEvent를 통해 전달받은 값을 분석하여 Double tab 판정을 내린 뒤, Activity로 event를 push한다.
     * 해당 pushEvent는 화면에 accessibility focus가 안잡혔을 경우(HOVER_ENTER가 발생되지 않은경우) 에만 Activity로 event를 push한다.
     * 손가락 1개로 화면을 double tab할 경우, 이벤트 상수값은 1048576 -> 1048576 -> 2097152가 전달된다.
     * 손가락 2개로 화면을 touch할 경우, 이벤트 상수값은 1048576 -> 2097152가 전달된다.
     * 이에 따라, 발생되는 이벤트 상수값으로 손가락 1개를 활용하는 double tab을 구분한다.
     * 1048576, 2097152를 제외한 다른 event가 발생될 경우, 터치 판정을 위한 모든 값을 초기화한다.
     * @param event : accessibilityEvent(터치 외 광범위한 event)
     */
    @Override
    public void pushEvent(AccessibilityEvent event) {
        if(accessibilityEventListener != null) {
            if (screenFocusState == false) {
                int eventType = event.getEventType();
                if (eventType == TYPE_TOUCH_INTERACTION_START)
                    screenTouchCount++;
                else if (eventType == TYPE_TOUCH_INTERACTION_END) {
                    if (screenTouchCount == 2)
                        pushEnterEvent();
                    screenTouchCount = 0;
                } else
                    screenTouchCount = 0;
            }
        } else
            screenTouchCount = 0;
    }


    /**
     * double tab 판정이 날 경우, 해당 함수를 통해 activity로 touch event를 만들어 전송한다.
     * ACTION_DOWN과 ACTION_UP을 같은 시간대로 생성한 뒤 전송한다.
     */
    private void pushEnterEvent() {
        if(accessibilityEventListener != null) {

            int metaState = 0;
            long downTime = SystemClock.currentThreadTimeMillis();
            long eventTime = SystemClock.currentThreadTimeMillis();
            float x = 0.0f;
            float y = 0.0f;

            MotionEvent downEvent = MotionEvent.obtain(
                    downTime,
                    eventTime,
                    MotionEvent.ACTION_DOWN,
                    x,
                    y,
                    metaState
            );

            MotionEvent upEvent = MotionEvent.obtain(
                    downTime,
                    eventTime,
                    MotionEvent.ACTION_UP,
                    x,
                    y,
                    metaState
            );

            accessibilityEventListener.pushDoubleTab(downEvent);
            accessibilityEventListener.pushDoubleTab(upEvent);
        }

    }

    /**
     * singleton 초기화 함수
     */
    public void initialize(){
        setFocusState(false);
        accessibilityEventListener = null;
    }


    /**
     * 접근성 권한이 설정되어 있는지 확인하는 함수.
     * 현재 앱이 접근성 권한이 설정되어 있지 않다면 접근성 권한 설정 화면을 띄운다.
     */
    public void checkPermissions(){
        if (!checkAccessibilityPermissions()) {
            context.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
        }
    }


    /**
     * 현재 접근성 권한이 설정되어 있는 app 목록을 가져온다.
     * 가져온 app목록 중 현재 입의 패키지 명과 동일한 패키지 명이 존재한다면 현재 앱이 접근성 권한이 설정되어 있는것으로 간주한다.
     * @return true(접근성 권한 설정 ok), false(접근성 권한 설정 no)
     */
    private boolean checkAccessibilityPermissions() {
        accessibilityManager = (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);

        // getEnabledAccessibilityServiceList는 현재 접근성 권한을 가진 리스트를 가져오게 된다
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);
        for (int i = 0; i < list.size(); i++) {
            AccessibilityServiceInfo info = list.get(i);
            // 접근성 권한을 가진 앱의 패키지 네임과 패키지 네임이 같으면 현재앱이 접근성 권한을 가지고 있다고 판단함
            if (info.getResolveInfo().serviceInfo.packageName.equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
