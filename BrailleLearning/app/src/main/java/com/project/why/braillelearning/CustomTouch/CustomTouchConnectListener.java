package com.project.why.braillelearning.CustomTouch;

import android.view.MotionEvent;
import com.project.why.braillelearning.EnumConstant.TouchLock;

/**
 * Created by hyuck on 2018-01-18.
 * activity와 customTouchEvent가 연결되기 위한 interface
 * activity의 생명주기에 따라 customTouchEvent의 onResume과 onPause를 호출한다.
 * activity에서 발생된 touchevent를 customTouchEvent class로 event를 전달한다.
 */
public interface CustomTouchConnectListener {
    void onResume();
    void onPause();
    void touchEvent(MotionEvent event);
    void setTouchLock(TouchLock lockType);
}
