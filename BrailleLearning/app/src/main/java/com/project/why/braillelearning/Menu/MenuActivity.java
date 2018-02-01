package com.project.why.braillelearning.Menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.project.why.braillelearning.ActivityManagerSingleton;
import com.project.why.braillelearning.CustomTouch.CustomTouchConnectListener;
import com.project.why.braillelearning.CustomTouch.CustomTouchEvent;
import com.project.why.braillelearning.CustomTouch.CustomTouchEventListener;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Menu;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningControl.MultiFinger;
import com.project.why.braillelearning.LearningControl.BrailleLearningActivity;
import com.project.why.braillelearning.LearningControl.FingerCoordinate;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;
import com.project.why.braillelearning.Module.ImageResizeModule;
import com.project.why.braillelearning.R;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Menu를 화면에 출력하는 Activity
 * MenuTreeManager에서 메뉴 image와 음성 file들을 관리하고 있음
 */
public class MenuActivity extends Activity implements CustomTouchEventListener{
    private LinearLayout layout;
    private int PageNumber=0; // 메뉴 위치를 알기위한 변수
    private int MenuImageSize;
    private ImageView MenuImageView; // 메뉴 이미지뷰
    private ImageView kakaoImageView;
    private MenuTreeManager menuTreeManager; // 메뉴 트리 manager
    private ImageResizeModule imageResizeModule;
    private Deque<Integer> menuAddressDeque; // 메뉴 탐색을 위한 주소 경로를 담는 Deque
    private int NowMenuListSize = 0; // 현재 위치한 메뉴 리스트의 길이를 저장하는 변수
    private MediaSoundManager mediaSoundManager;
    private MultiFinger multiFingerFunction;
    private CustomTouchConnectListener customTouchConnectListener;
    private ActivityManagerSingleton activityManagerSingleton = ActivityManagerSingleton.getInstance();
    private ImageView menuFocus;
    private AccessibilityManager accessibilityManager;
    private TimerTask focusTimerTask;
    private Timer focusTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braille_learning_menu);
        activityManagerSingleton.addArrayList(this);
        InitMenu();
        setLayout();
        initTouchEvent();
        checkFirstRun();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            setFullScreen();
        }
    }


    private void setFullScreen(){ // 전체화면 함수
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }


    @Override
    protected void onResume(){
        super.onResume();
        refreshData();
        setkakaoLogo();
        connectTouchEvent();
    }


    /**
     * 앱 설치 후 최초 실행인지 확인하는 함수
     */
    private void checkFirstRun(){
        SharedPreferences pref = getSharedPreferences("tutorial", MODE_PRIVATE);
        String state = pref.getString("FIRST_RUN","FALSE");
        if(state == "FALSE" || state.equals("FALSE")){
            menuAddressDeque.addLast(0);
            enterBrailleLearning();
        }
    }


    private void setkakaoLogo(){
        if(kakaoImageView == null) {
            kakaoImageView = (ImageView) findViewById(R.id.imageView_kakao);
            kakaoImageView.setImageDrawable(imageResizeModule.getDrawableImage(R.drawable.kakao_image, 408, 60)); //현재화면에 이미지 설정
        }
    }


    /**
     * 메뉴 초기화 함수
     */
    private void InitMenu(){
        initImageView(); // 메뉴 imageview 사이즈 조절
        imageResizeModule = new ImageResizeModule(getResources());
        mediaSoundManager = new MediaSoundManager(this);
        menuTreeManager = new MenuTreeManager();
        menuAddressDeque = new LinkedList<>();
        menuAddressDeque.addLast(PageNumber);
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
        multiFingerFunction = new MultiFinger(this);
        menuFocus = (ImageView) findViewById(R.id.menufocus_imageview);
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
        setFullScreen();
    }


    /**
     * 메뉴 imageview setting 함수
     */
    private void initImageView(){
        MenuImageView = (ImageView) findViewById(R.id.braillelearningmenu_imageview);
        MenuImageSize = (int)(Global.DisplayY*0.8); // imageview의 width와 height는 세로 높이의 80%
        MenuImageView.getLayoutParams().height = MenuImageSize;
        MenuImageView.getLayoutParams().width = MenuImageSize;
        MenuImageView.requestLayout();
    }


    /**
     * layout 설정
     */
    private void setLayout(){
        layout = (LinearLayout) findViewById(R.id.menu_layout);
        layout.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                onTouchEvent(event);
                return false;
            }
        });
    }


    @Override
    protected void onPause(){
        super.onPause();
        recycleImage();
        recycleLogo();
        onStopSound();
        pauseTouchEvent();
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
    }


    /**
     * 손가락 1개, 2개에 대한 event를 발생하는 touchevent 함수
     * @param event : 발생된 touchevent
     * @return 처리 여부
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(customTouchConnectListener != null)
            customTouchConnectListener.touchEvent(event);

        return false;
    }


    /**
     * touchevent module 초기화
     */
    private void initTouchEvent(){
        customTouchConnectListener = new CustomTouchEvent(this, this);
    }


    /**
     * touchevent module 연결
     */
    private void connectTouchEvent(){
        customTouchConnectListener.onResume();
    }


    /**
     * touchevent module 일시중지
     */
    private void pauseTouchEvent(){
        customTouchConnectListener.onPause();
    }


    /**
     * 화면 새로고침 함수
     * 만약 menuAddressDeque가 비어있을 경우, Application을 종료하는 것을 의미
     */
    private void refreshData() { // 메뉴 이미지 설정 함수
        if(menuAddressDeque.isEmpty())  // 메뉴 Adress Deque가 비어있으면 종료
            activityManagerSingleton.allActivityFinish();
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TreeNode menuNode = menuTreeManager.getMenuTreeNode(menuAddressDeque);
                    if (menuNode != null) {
                        refreshImage(menuNode);
                        refreshSound(menuNode);
                    } else  // 하위메뉴가 존재하지 않는다면 방금 선택한 경로 삭제 후 점자 학습 화면 이동
                        enterBrailleLearning();
                }
            });
        }
    }


    /**
     * 이미지 새로고침 함수
     * @param menuNode : 현재 화면을 의미하는 treeNode
     */
    private void refreshImage(TreeNode menuNode){
        recycleImage();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_fade);
        MenuImageView.startAnimation(animation);
        MenuImageView.setImageDrawable(imageResizeModule.getDrawableImage(menuNode.getImageId(), MenuImageSize, MenuImageSize)); //현재화면에 이미지 설정
        focusSetting();
    }


    /**
     * 음성 새로고침 함수
     * @param menuNode : 현재 화면을 의미하는 treeNode
     */
    private void refreshSound(TreeNode menuNode){
        int id = menuNode.getSoundId();
        mediaSoundManager.start(id);
        mediaSoundManager.start(R.raw.doubletab_info);
    }


    private void refreshSound(){
        onStopSound();
        TreeNode menuNode = menuTreeManager.getMenuTreeNode(menuAddressDeque);
        refreshSound(menuNode);
    }


    /**
     * 점자 학습 화면으로 들어가는 함수
     * 자신이 선택한 menu name을 학습화면으로 전달
     */
    private void enterBrailleLearning(){
        focusThreadStop();
        menuAddressDeque.removeLast();
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
        Menu menuName = menuTreeManager.getMenuName(menuAddressDeque);
        Intent i = new Intent(this, BrailleLearningActivity.class);
        i.putExtra("MENUNAME",menuName);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        startActivity(i);
    }


    /**
     * 화면에 focus를 잡아주는 함수
     */
    @Override
    public void onFocusRefresh() {
        if(mediaSoundManager.getMediaPlaying() == false)
            requestFocus(150, false);
        else {
            requestFocus(150, true);
            mediaSoundManager.stop();
        }
    }


    private void requestFocus(int time, boolean mediaPlaying){
        if(accessibilityManager.isTouchExplorationEnabled() == false)
            focusThreadMaking(time, mediaPlaying);
        else {
            refreshSound();
            layout.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
            layout.requestFocus();
        }
    }


    /**
     * focus 인식 시간 스레드
     */
    private synchronized void focusThreadMaking(int time, final boolean mediaPlaying){
        if(focusTimerTask == null) {
            focusTimerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(!mediaPlaying)
                                refreshSound();
                            focusSetting();
                            focusThreadStop();
                        }
                    });
                }
            };
            focusTimer = new Timer();
            focusTimer.schedule(focusTimerTask, time);
        }
    }


    /**
     * focus setting 함수
     */
    private void focusSetting(){
        if(accessibilityManager.isTouchExplorationEnabled() == false) {
            Animation animation = AnimationUtils.loadAnimation(MenuActivity.this, R.anim.focus_fade);
            menuFocus.startAnimation(animation);
            menuFocus.setBackgroundResource(R.drawable.focusborder);
            mediaSoundManager.focusSoundStart();
        } else {
            layout.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
            layout.requestFocus();
        }
    }


    /**
     * focus 인식 스레드를 종료하는 함수
     */
    private void focusThreadStop(){
        if(focusTimerTask != null) {
            focusTimerTask.cancel();
            focusTimerTask = null;
        }

        if(focusTimer != null){
            focusTimer.cancel();
            focusTimer = null;
        }
    }


    @Override
    public void onStopSound() {
        mediaSoundManager.stop();
        focusThreadStop();
    }


    @Override
    public void onError() {
        FingerFunctionType type = FingerFunctionType.NONE;
        mediaSoundManager.start(type);
    }


    /**
     * 손가락 1개 함수 정의
     * 메뉴 접속 이벤트가 발생되었을 경우 호출됨
     * Deque에 tree를 탐색하기 위한 주소값 변경
     * @param fingerCoordinate : 좌표값
     */
    @Override
    public void onOneFingerFunction(FingerCoordinate fingerCoordinate) {
        FingerFunctionType type = FingerFunctionType.ENTER;
        mediaSoundManager.start(type);
        menuAddressDeque.addLast(0);
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque); // 현재 메뉴 리스트 숫자 리셋
        refreshData();
    }


    /**
     * 손가락 2개 함수 정의
     * 이벤트에 따라 Deque에 tree를 탐색하기 위한 주소값 변경
     * @param fingerCoordinate : 좌표값
     */
    @Override
    public void onTwoFingerFunction(FingerCoordinate fingerCoordinate) {
        FingerFunctionType type = multiFingerFunction.getFingerFunctionType(fingerCoordinate);
        switch (type) {
            case BACK: // 상위 메뉴
                menuAddressDeque.removeLast();
                if (!menuAddressDeque.isEmpty())
                    NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque); // 현재 메뉴 리스트 숫자 리셋
                refreshData();
                break;
            case RIGHT: // 오른쪽 메뉴
                if(menuAddressDeque.peekLast()+1 == NowMenuListSize) {
                    mediaSoundManager.allStop();
                    mediaSoundManager.start(R.raw.last_page);
                } else {
                    menuAddressDeque.addLast(menuAddressDeque.removeLast() + 1);
                    refreshData();
                }
                break;
            case LEFT: // 왼쪽 메뉴
                if(menuAddressDeque.peekLast() == 0) {
                    mediaSoundManager.allStop();
                    mediaSoundManager.start(R.raw.first_page);
                } else {
                    menuAddressDeque.addLast(menuAddressDeque.removeLast() - 1);
                    refreshData();
                }
                break;
            case REFRESH: // 특수기능
                refreshData();
                break;
        }
    }


    /**
     * 손가락 3개 함수 정의
     * 손가락 3개 함수가 발생됬을 시, 메뉴 activity에서는 사용 불가 음성 출력
     * @param fingerCoordinate
     */
    @Override
    public void onThreeFingerFunction(FingerCoordinate fingerCoordinate) {
        FingerFunctionType type = multiFingerFunction.getFingerFunctionType(fingerCoordinate);
        switch (type) {
            case SPEECH:
                mediaSoundManager.start(R.raw.impossble_function);
                break;
            case MYNOTE:
                mediaSoundManager.start(R.raw.impossble_function);
                break;
            default:
                break;
        }
    }


    /**
     * 이미지 메모리 해제 함수
     */
    private void recycleImage(){     //이미지 메모리 해제 함수
        if(MenuImageView != null) {
            Drawable image = MenuImageView.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            MenuImageView.setImageDrawable(null);
        }
    }


    /**
     * 로고 이미지 메모리 해제 함수
     */
    private void recycleLogo(){
        if(kakaoImageView != null){
            Drawable image = kakaoImageView.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            kakaoImageView.setImageDrawable(null);
            kakaoImageView = null;
        }
    }
}
