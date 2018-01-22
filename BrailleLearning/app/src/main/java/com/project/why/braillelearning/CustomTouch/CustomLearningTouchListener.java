package com.project.why.braillelearning.CustomTouch;

import com.project.why.braillelearning.LearningControl.FingerCoordinate;

/**
 * Created by hyuck on 2018-01-21.
 * customTouchEventListenr interface를 상속받은 interface
 * 메뉴가 아닌 학습화면의 경우, ACTION_MOVE의 action이 필요하다.
 * 이에 따라, 현재 interface를 구현하여 학습화면의 ACTION_MOVE를 정의한다.
 */

public interface CustomLearningTouchListener extends CustomTouchEventListener {
    void onOneFingerMoveFunction(FingerCoordinate fingerCoordinate);
}
