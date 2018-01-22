package com.project.why.braillelearning.Accessibility;

import android.view.MotionEvent;

/**
 * Created by hyuck on 2018-01-16.
 * 접근성 서비스에서 발생된 터치값을 Activity로 전달하기 위한 interface
 * AccessibilityEventSingleton에서 touch 값들을 분석 한뒤, 손가락 1개로 두번 연속 탭(더블탭) 판정이 되었을 경우 Activity로 전달함
 */

public interface AccessibilityEventListener {
    void pushDoubleTab(MotionEvent event);
}
