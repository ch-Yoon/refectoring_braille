package com.project.why.braillelearning.Permission;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningView.ActivityManagerSingleton;
import com.project.why.braillelearning.MediaPlayer.MediaPlayerStopCallbackListener;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;
import com.project.why.braillelearning.Module.ImageResizeModule;
import com.project.why.braillelearning.R;
import java.util.Timer;
import java.util.TimerTask;
import static android.content.Context.MODE_PRIVATE;
import static android.view.View.INVISIBLE;

/**
 * Created by EH515 on 2018-03-16.
 */

/**
 * 권한 여부를 사용자에게 안내하는 class
 * 해당 class로 사용자에게 권한 설정 안내를 하거나 권한 설정 확인을 한다.
 */
public class PermissionCheckModule {
    public static final int PERMISSION_ALLOWED = 0;
    public static final int PERMISSION_NOT_ALLOWED = 1;
    public static final int PERMISSION_NOT_CHECKED = 2;
    private Context context;
    private View view;
    private ImageView permissionGuideBackGround, permissionGuideImage;
    private ImageResizeModule imageResizeModule;
    private MediaSoundManager mediaSoundManager;
    private PermissionCheckCallbackListener listener;
    private TimerTask permissionCheckTimerTask;
    private Timer permissionCheckTimer;
    private int permissionCheckTime = 0;
    private ActivityManagerSingleton activityManagerSingleton;

    public PermissionCheckModule(Context context, View view){
        this.context = context;
        this.view = view;
        imageResizeModule = new ImageResizeModule(context.getResources());
        mediaSoundManager = new MediaSoundManager(context);
        activityManagerSingleton = ActivityManagerSingleton.getInstance();
    }


    /**
     * 권한 설정 imageView 생성 check 함수
     */
    private void checkPermissionView(){
        if(permissionGuideBackGround == null) {
            permissionGuideBackGround = new ImageView(context);
            FrameLayout.LayoutParams backGroundLayoutParam = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
            );

            permissionGuideBackGround.setLayoutParams(backGroundLayoutParam);
            permissionGuideBackGround.setBackgroundColor(Color.parseColor("#ee171a2d"));
            ((FrameLayout) view.getParent()).addView(permissionGuideBackGround);
            permissionGuideBackGround.setVisibility(INVISIBLE);
        }

        if(permissionGuideImage == null) {
            permissionGuideImage = new ImageView(context);
            FrameLayout.LayoutParams guideLayoutParam = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
            );

            guideLayoutParam.width = (int) (Global.displayY * 0.65) * 2;
            guideLayoutParam.height = (int) (Global.displayY * 0.65);
            permissionGuideImage.setLayoutParams(guideLayoutParam);
            ((FrameLayout) view.getParent()).addView(permissionGuideImage);
            permissionGuideImage.setVisibility(INVISIBLE);
        }
    }


    /**
     * permission check 함수
     * 음성인식을 활용하는 메뉴 접속 시 권한 사용 여부를 사용자에게 안내한적이 있는지 확인하는 함수
     * @return
     */
    public int checkPermission(){
        SharedPreferences pref = context.getSharedPreferences("settingCheck", MODE_PRIVATE);
        String state = pref.getString("PERMISSION_CHECK", "FALSE");
        if(state == "TRUE" || state.equals("TRUE")){
            if (PermissionChecker.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || PermissionChecker.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                return PERMISSION_NOT_ALLOWED;
            else
                return PERMISSION_ALLOWED;
        } else
            return PERMISSION_NOT_CHECKED;
    }


    /**
     * 권한 사용 여부 안내 화면 중지 함수
     */
    public void stopPermissionGuide(){
        permissionCheckWaitThreadStop();
        recyclePermissionImage();
        if(permissionGuideBackGround != null)
            permissionGuideBackGround.setVisibility(View.INVISIBLE);
    }


    /**
     * 권한 사용 여부 안내 화면 취소 함수
     */
    public void cancelPermissionGuide(){
        stopPermissionGuide();
        mediaSoundManager.setMediaPlayerStopCallbackListener(null);
        mediaSoundManager.allStop();
        mediaSoundManager.start(R.raw.permission_cancel);
    }


    /**
     * 권한 사용 안내 화면 출력 함수
     * @param permissionCheckResult
     */
    public void startPermissionGuide(int permissionCheckResult){
        int drawableId = 0;
        int rawid = 0;
        if(permissionCheckResult == PERMISSION_NOT_CHECKED) {
            drawableId = R.drawable.permission_not_check;
            rawid = R.raw.permission_guide;
        } else if(permissionCheckResult == PERMISSION_NOT_ALLOWED) {
            drawableId = R.drawable.permission_not_allowed;
            rawid = R.raw.permission_not_allowed_guide;
        }

        if(drawableId != 0 && rawid != 0) {
            checkPermissionView();
            recyclePermissionImage();
            permissionGuideBackGround.setVisibility(View.VISIBLE);
            permissionGuideImage.setImageDrawable(imageResizeModule.getDrawableImage(drawableId, permissionGuideImage.getLayoutParams().width, permissionGuideImage.getLayoutParams().height));
            permissionGuideImage.setVisibility(View.VISIBLE);
            mediaSoundManager.stop();
            mediaSoundManager.start(rawid);
            mediaSoundManager.setMediaPlayerStopCallbackListener(new MediaPlayerStopCallbackListener() {
                @Override
                public void mediaPlayerStop() {
                    permissionCheckWaitThreadStart();
                }
            });
        }
    }


    /**
     * application 권한 설정 화면으로 이동하는 함수
     */
    public void permissionSettingMove(){
        stopPermissionGuide();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }


    /**
     * 권한설정을 허용하였을 때의 함수
     */
    public void allowedPermission(){
        stopPermissionGuide();
        SharedPreferences pref = context.getSharedPreferences("settingCheck", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("PERMISSION_CHECK","TRUE");
        editor.commit();
    }


    /**
     * permission check 종료여부 확인을 위한 callbacklistener 등록 함수
     * @param listener
     */
    public void setPermissionCheckCallbackListener(PermissionCheckCallbackListener listener){
        this.listener = listener;
    }


    /**
     * 이미지 메모리 해제 함수
     */
    private void recyclePermissionImage(){
        if(permissionGuideImage != null) {
            Drawable image = permissionGuideImage.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            permissionGuideImage.setImageDrawable(null);
        }
    }


    /**
     * 5초간의 대기 시간 안에 더블 탭이 발동되면 권한 사용 메뉴접속을 허용한다.
     * 5초간의 대기 시간 안에 더블 탭이 발동되지 않으면  실행하지 않는다.
     */
    private void permissionCheckWaitThreadStart(){
        if (permissionCheckTimerTask == null) {
            permissionCheckTimerTask = new TimerTask() {
                @Override
                public void run() {
                    activityManagerSingleton.getNowActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (permissionCheckTime == 5) {
                                permissionCheckTime = 0;
                                permissionCheckWaitThreadStop();
                                cancelPermissionGuide();
                                if(listener != null)
                                    listener.permissionCancel();
                                    listener = null;
                                } else {
                                    permissionCheckTime++;
                                    mediaSoundManager.start(R.raw.clock);
                                }
                            }
                    });
                }
            };
            permissionCheckTimer = new Timer();
            permissionCheckTimer.schedule(permissionCheckTimerTask, 0, 1000);
        }
    }


    /**
     * 권한 확인을 위해 5초간의 대기를 하는 스레드를 초기화 하는 함수
     */
    private void permissionCheckWaitThreadStop(){
        permissionCheckTime = 0;

        if(permissionCheckTimerTask != null) {
            permissionCheckTimerTask.cancel();
            permissionCheckTimerTask = null;
        }

        if(permissionCheckTimer != null){
            permissionCheckTimer.cancel();
            permissionCheckTimer = null;
        }

        mediaSoundManager.stop();
    }
}
