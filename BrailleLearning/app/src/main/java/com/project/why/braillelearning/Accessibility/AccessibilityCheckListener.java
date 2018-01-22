package com.project.why.braillelearning.Accessibility;

import android.view.accessibility.AccessibilityEvent;

/**
 * Created by hyuck on 2018-01-16.
 * 접근성 서비스에서 AccessibilityEventSingleton으로 pushEvent를 보내기 위한 interface
 * AccessibilityService에서 발생된 touch event를 보낸다
 */

public interface AccessibilityCheckListener {
    void pushEvent(AccessibilityEvent event);
}
