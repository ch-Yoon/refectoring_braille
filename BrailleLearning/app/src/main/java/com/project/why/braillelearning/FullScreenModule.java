package com.project.why.braillelearning;

import android.app.Activity;
import android.os.Build;
import android.view.View;

/**
 * Created by hyuck on 2017-08-22.
 */

public class FullScreenModule {
    /*
     * 전체화면 적용을 위한 FullScreen Module
     */

    Activity mActivity;
    View mDecorView;
    public FullScreenModule(Activity activity){
        mActivity = activity;
    }

    public FullScreenModule(View decorView){
        mDecorView = decorView;
    }

    public int getFullScreenOption(){
        int uiOption;

        uiOption = mActivity.getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        return uiOption;
    }
}
