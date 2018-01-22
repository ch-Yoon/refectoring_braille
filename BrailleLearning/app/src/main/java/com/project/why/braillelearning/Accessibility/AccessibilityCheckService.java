package com.project.why.braillelearning.Accessibility;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by hyuck on 2018-01-16.
 * AccessibilityService를 상속받은 AccessibilityCheckService
 * TouchEvent로 식별되기 전의 화면에 touch된 값들이 AccessibilityCheckService로 수신된다.
 */

public class AccessibilityCheckService extends android.accessibilityservice.AccessibilityService{
    private AccessibilityEventSingleton accessibilityEventSingleton = AccessibilityEventSingleton.getInstance();

    /**
     * 접근성 이벤트 함수
     * AccessibilityEvent를 accessibilityEventSingleton으로 전달한다.
     * @param event : 접근성 이벤트(터치 외 광범위한 데이터를 담고 있음)
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        accessibilityEventSingleton.pushEvent(event);
    }


    /**
     * 접근성 서비스 연결 함수
     * 접근성 권한이 설정되어있고, Talkback이 활성화 되어 있다면 호출된다.
     */
    @Override
    public void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
        info.feedbackType = AccessibilityServiceInfo.DEFAULT;
        info.notificationTimeout = 100; // millisecond
        setServiceInfo(info);
    }

    @Override
    public void onInterrupt() {
    }
}
