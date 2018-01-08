package com.project.why.braillelearning;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import java.util.Timer;
import java.util.TimerTask;

public class AccessibilityCheckService extends Service {
    private TimerTask mTask;
    private Timer mTimer;
    private AccessibilityManager am;

    public AccessibilityCheckService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        Log.d("test","accessibility check service start");
        if(am == null)
            am = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        threadStop();
        threadMaking();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.d("test","accessibility check service stop");
        threadStop();
        super.onDestroy();
    }



    private synchronized void threadMaking(){
        if(mTask == null) {
            mTask = new TimerTask() {
                @Override
                public void run() {
                    boolean isAccessibilityEnabled = am.isEnabled();
                    boolean isExploreByTouchEnabled = am.isTouchExplorationEnabled();
                    Log.d("test", "talkback system : " + isAccessibilityEnabled + ",     talkback touch : " + isExploreByTouchEnabled);
                }
            };
            mTimer = new Timer();
            mTimer.schedule(mTask, 1000, 1000); // 1초마다 생성
        }
    }

    private void threadStop(){
        if(mTask != null) {
            mTask.cancel();
            mTask = null;
        }

        if(mTimer != null){
            mTimer.cancel();
            mTimer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
