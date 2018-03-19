package com.project.why.braillelearning.CustomTouch;

import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.LearningControl.FingerCoordinate;

/**
 * Created by hyuck on 2018-01-18.
 * activity와 customTouchEvent 가 연결되기 위한 interface
 * customTouchEvent 에서 touch를 식별 후, 해당 함수들을 통해 activity로 이벤트를 push한다.
 */

public interface CustomTouchEventListener {
    void onFocusRefresh();
    void onStopSound();
    void onError();
    void onOneFingerFunction(FingerCoordinate fingerCoordinate);
    void onTwoFingerFunction(FingerFunctionType fingerFunctionType);
    void onSpecialFunctionGuide();
    void onSpecialFunctionDisable();
    void onStartSpecialFunction();
    void onPermissionUseAgree();
    void onPermissionUseDisagree();
}
