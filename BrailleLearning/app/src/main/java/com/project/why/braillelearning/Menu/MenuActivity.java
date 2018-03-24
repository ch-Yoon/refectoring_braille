package com.project.why.braillelearning.Menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.project.why.braillelearning.BrailleInformationFactory.BrailleFactory;
import com.project.why.braillelearning.BrailleInformationFactory.BrailleInformationFactory;
import com.project.why.braillelearning.BrailleInformationFactory.GettingInformation;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.EnumConstant.TouchLock;
import com.project.why.braillelearning.Loading.BrailleLearningLoading;
import com.project.why.braillelearning.SpecialFunction.SpecialFunctionListener;
import com.project.why.braillelearning.SpecialFunction.SpecialFunctionManager;
import com.project.why.braillelearning.LearningView.ActivityManagerSingleton;
import com.project.why.braillelearning.CustomTouch.CustomTouchConnectListener;
import com.project.why.braillelearning.CustomTouch.CustomTouchEvent;
import com.project.why.braillelearning.CustomTouch.CustomTouchEventListener;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Menu;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningControl.BrailleLearningActivity;
import com.project.why.braillelearning.LearningControl.FingerCoordinate;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;
import com.project.why.braillelearning.Module.ImageResizeModule;
import com.project.why.braillelearning.Permission.PermissionCheckCallbackListener;
import com.project.why.braillelearning.Permission.PermissionCheckModule;
import com.project.why.braillelearning.R;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Menu를 화면에 출력하는 Activity
 * MenuTreeManager에서 메뉴 image와 음성 file들을 관리하고 있음
 */
public class MenuActivity extends Activity implements CustomTouchEventListener, SpecialFunctionListener{
    private LinearLayout layout, bottomGuideLayout;
    private int PageNumber; // 메뉴 위치를 알기위한 변수
    private int specialViewSize;
    private ImageView MenuImageView, kakaoImageView, specialBackgroundView, specialImageView;
    private ArrayList<BottomCircle> bottomGuideCircleArray = new ArrayList<>();
    private MenuTreeManager menuTreeManager; // 메뉴 트리 manager
    private ImageResizeModule imageResizeModule;
    private Deque<Integer> menuAddressDeque; // 메뉴 탐색을 위한 주소 경로를 담는 Deque
    private int NowMenuListSize = 0; // 현재 위치한 메뉴 리스트의 길이를 저장하는 변수
    private MediaSoundManager mediaSoundManager;
    private CustomTouchConnectListener customTouchConnectListener;
    private ActivityManagerSingleton activityManagerSingleton = ActivityManagerSingleton.getInstance();
    private ImageView menuFocus;
    private AccessibilityManager accessibilityManager;
    private TimerTask focusTimerTask;
    private Timer focusTimer;
    private SpecialFunctionManager specialFunctionManager;
    private PermissionCheckModule permissionCheckModule;
    private int specialFunctionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null) {
            Intent i = new Intent(this, BrailleLearningLoading.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        } else {
            setContentView(R.layout.activity_braille_learning_menu);
            activityManagerSingleton.addArrayList(this);
            InitMenuImageView();
            initLayout();
            InitModule();
            initTouchEvent();
            InitMenuTree();
            checkFirstRun();
            initSpecialImageView();
            initSpecialFunctionManager(BrailleLearningType.MENU);
        }
    }


    /**
     * 화면에 포커스가 잡힐 때 전체화면 설정하는 함수
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
           setFullScreen();
        }
    }


    /**
     * 재시작 함수
     * 메뉴 화면 데이터 새로고침
     * 카카오 로고 set
     * touch event interface 연결
     */
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("menuactivity","onResume"+"");
        refreshData();
        initKakaoLogo();
        connectTouchEvent();
    }


    /**
     * 일시정지 함수
     * 이미지 메모리 해제
     * 미디어 중지
     * touch event interface 일시중지
     * 특수기능 화면 해제
     */
    @Override
    protected void onPause(){
        super.onPause();
        recycleImage();
        recycleLogo();
        onStopSound();
        pauseTouchEvent();
        onSpecialFunctionViewOff();
        permissionCheckModule.stopPermissionGuide();
    }


    /**
     * 메뉴 tree init 함수
     */
    private void InitMenuTree(){
        menuTreeManager = new MenuTreeManager();
        if(menuAddressDeque == null) {
            menuAddressDeque = new LinkedList<>();
            menuAddressDeque.addLast(PageNumber);
            NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
        }
    }


    /**
     * activity에서 사용되는 module init 함수
     */
    private void InitModule(){
        permissionCheckModule = new PermissionCheckModule(this, layout);
        imageResizeModule = new ImageResizeModule(getResources());
        mediaSoundManager = new MediaSoundManager(this);
        accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
    }


    /**
     * 메뉴 초기화 함수
     */
    private void InitMenuImageView(){
        MenuImageView = (ImageView) findViewById(R.id.braillelearningmenu_imageview);
        MenuImageView.getLayoutParams().width = (int) (Global.DisplayY * 0.45);
        MenuImageView.getLayoutParams().height = (int) (Global.DisplayY * 0.45);
        MenuImageView.requestLayout();

        bottomGuideLayout = (LinearLayout)findViewById(R.id.buttomcircleguide_layout);
        menuFocus = (ImageView) findViewById(R.id.menufocus_imageview);
        setFullScreen();
    }


    /**
     * layout 설정
     * layout에서 hover event 수신
     */
    private void initLayout(){
        layout = (LinearLayout) findViewById(R.id.menu_layout);
        layout.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                onTouchEvent(event);
                return false;
            }
        });
    }


    /**
     * 특수기능 imageView 초기화 함수
     */
    private void initSpecialImageView(){
        specialBackgroundView = (ImageView) findViewById(R.id.specialbackground);
        specialBackgroundView.setVisibility(View.INVISIBLE);

        specialImageView = (ImageView) findViewById(R.id.specialimageview);
        specialViewSize = (int)(Global.DisplayX*0.3);
        specialImageView.getLayoutParams().height = specialViewSize;
        specialImageView.getLayoutParams().width = specialViewSize;
        specialImageView.requestFocus();
        specialImageView.setVisibility(View.INVISIBLE);
    }


    /**
     * 특수기능 이미지, 음성 id를 유지할 arraylist set 함수
     * @param brailleLearningType
     */
    private void initSpecialFunctionManager(BrailleLearningType brailleLearningType){
        specialFunctionManager = new SpecialFunctionManager(brailleLearningType, this);
    }


    /**
     * touchevent module 초기화
     */
    private void initTouchEvent(){
        customTouchConnectListener = new CustomTouchEvent(this, this);
    }


    /**
     * 카카오 로고 이미지 set함수
     */
    private void initKakaoLogo(){
        if(kakaoImageView == null) {
            kakaoImageView = (ImageView) findViewById(R.id.imageView_kakao);
            kakaoImageView.setImageDrawable(imageResizeModule.getDrawableImage(R.drawable.kakao_image, 408, 60)); //현재화면에 이미지 설정
        }
    }


    /**
     * 전체화면 적용 함수
     * 네비게이션바 제거
     * 풀스크린 모드
     */
    private void setFullScreen(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
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
     * 손가락 1개 함수 정의
     * 메뉴 접속 이벤트가 발생되었을 경우 호출됨
     * Deque에 tree를 탐색하기 위한 주소값 변경
     * @param fingerCoordinate : 좌표값
     */
    @Override
    public void onOneFingerFunction(FingerCoordinate fingerCoordinate) {
        focusThreadStop();
        FingerFunctionType type = FingerFunctionType.ENTER;
        mediaSoundManager.start(type);
        menuAddressDeque.addLast(0);
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque); // 현재 메뉴 리스트 숫자 리셋
        refreshData();
    }


    /**
     * 손가락 2개 함수 정의
     * 이벤트에 따라 Deque에 tree를 탐색하기 위한 주소값 변경
     * @param fingerFunctionType : 제스처 타입
     */
    @Override
    public void onTwoFingerFunction(FingerFunctionType fingerFunctionType) {
        switch (fingerFunctionType) {
            case BACK: // 상위 메뉴
                beforeMenu();
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
        }
    }


    /**
     * 화면에 focus를 잡아주는 함수
     * 손가락 1개 터치와 손가락 2개 터치를 구분하기 위해 딜레이를 발생시키는스레드 생성
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


    /**
     * 음성파일 중지 함수
     */
    @Override
    public void onStopSound() {
        mediaSoundManager.stop();
        focusThreadStop();
    }


    /**
     * 터치 판별 불가 시 다시 시도 멘트 출력 함수
     */
    @Override
    public void onError() {
        FingerFunctionType type = FingerFunctionType.NONE;
        mediaSoundManager.start(type);
    }


    /**
     * 앱 설치 후 최초 실행인지 확인하는 함수
     * FALSE(최초 실행), TRUE(최초 실행 x)
     */
    private void checkFirstRun(){
        SharedPreferences pref = getSharedPreferences("tutorial", MODE_PRIVATE);
        String state = pref.getString("FIRST_RUN","FALSE");
        if(state == "FALSE" || state.equals("FALSE")){
            menuAddressDeque.addLast(0);
            enterBrailleLearning();
        }
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
     * menuAddressDeque에 들어있는 값으로 tree구조로 구성된 메뉴를 탐색
     * 만약 menuAddressDeque가 비어있을 경우, Application을 종료하는 것을 의미
     */
    private void refreshData() {
        if(menuAddressDeque.isEmpty())
            activityManagerSingleton.allActivityFinish();
        else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TreeNode menuNode = menuTreeManager.getMenuTreeNode(menuAddressDeque);
                    if (menuNode != null) {
                        refreshImage(menuNode);
                        refreshSound(menuNode);
                    } else
                        checkPermission();
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
        MenuImageView.setImageDrawable(imageResizeModule.getDrawableImage(menuNode.getImageId(), MenuImageView.getLayoutParams().width, MenuImageView.getLayoutParams().height)); //현재화면에 이미지 설정
        refreshBottomGuideCircleImage();

        focusSetting();
    }


    /**
     * 페이지 수와 현재 페이지 수를 나타내는 하단의 동그라미 이미지 set함수
     */
    private void refreshBottomGuideCircleImage(){
        int bottomCircleArraySize = bottomGuideCircleArray.size();
        if (bottomCircleArraySize < NowMenuListSize)
            addBottomGuideCircleImage();
        else if (NowMenuListSize < bottomCircleArraySize)
            recycleRemainderCircleImage(bottomCircleArraySize);

        setBottomGuideCircleImage();
    }


    /**
     * 페이지를 안내해 줄 하단 동그라미 imageView 추가 함수
     */
    private void addBottomGuideCircleImage(){
        while(true) {
            BottomCircle bottomCircle = new BottomCircle(this);
            bottomGuideCircleArray.add(bottomCircle);
            bottomGuideLayout.addView(bottomCircle.getCircleImageView());

            if(NowMenuListSize == bottomGuideCircleArray.size())
                break;
        }
    }


    /**
     * 현재 트리노드 위치의 가로 사이즈(메뉴 길이)보다 초과하는 동그라미들을 메모리 해제하는 함수
     * @param bottomCircleArraySize 현재 동그라미 이미지 array size
     */
    private void recycleRemainderCircleImage(int bottomCircleArraySize){
        for(int i = NowMenuListSize ; i<bottomCircleArraySize ; i++){
            BottomCircle bottomCircle = bottomGuideCircleArray.get(i);
            bottomCircle.recycleCircleImage();
        }
    }


    /**
     * 메뉴 하단의 동그라미 이미지를 set하는 함수
     */
    private void setBottomGuideCircleImage(){
        int nowPage = menuAddressDeque.getLast();
        for(int i=0 ; i<NowMenuListSize ; i++){
            BottomCircle circle = bottomGuideCircleArray.get(i);
            if(i == nowPage)
                circle.setNowPage(true);
            else
                circle.setNowPage(false);
        }
    }


    /**
     * 음성 새로고침 함수
     * @param menuNode : 현재 화면을 의미하는 treeNode
     */
    private void refreshSound(TreeNode menuNode){
        int id = menuNode.getSoundId();
        mediaSoundManager.start(id);
        mediaSoundManager.start(R.raw.fingerfunction_guide);
    }


    /**
     * 음성 새로고침 함수
     */
    private void refreshSound(){
        TreeNode menuNode = menuTreeManager.getMenuTreeNode(menuAddressDeque);
        refreshSound(menuNode);
    }


    /**
     * 메뉴에 들어가기 전 앱 권한 확인하는 함수
     */
    private void checkPermission(){
        focusThreadStop();
        Deque<Integer> tempMenuAddressDeque = new LinkedList<>();
        tempMenuAddressDeque.addAll(menuAddressDeque);
        tempMenuAddressDeque.removeLast();
        Menu menuName = menuTreeManager.getMenuName(tempMenuAddressDeque);
        boolean usePermission = menuName.getUsePermission();
        if(usePermission == true){
            int checkPermissionResult = permissionCheckModule.checkPermission();
            if(checkPermissionResult == PermissionCheckModule.PERMISSION_NOT_CHECKED){
                menuAddressDeque.removeLast();
                NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
                customTouchConnectListener.setTouchLock(TouchLock.PERMISSION_CHECK_LOCK);
                permissionCheckModule.startPermissionGuide(checkPermissionResult);
                permissionCheckModule.setPermissionCheckCallbackListener(new PermissionCheckCallbackListener() {
                    @Override
                    public void permissionCancel() {
                        customTouchConnectListener.setTouchLock(TouchLock.UNLOCK);
                        permissionCheckModule.cancelPermissionGuide();
                        refreshData();
                    }
                });
            } else
                enterBrailleLearning();
        } else
          enterBrailleLearning();
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

        GettingInformation object = getBrailleInformationObject(menuName);
        Json jsonFileName = object.getJsonFileName();
        BrailleLearningType brailleLearningType = object.getBrailleLearningType();
        Database databaseTableName = object.getDatabaseTableName();

        Intent i = new Intent(this, BrailleLearningActivity.class);
        i.putExtra("JSONFILENAME",jsonFileName);
        i.putExtra("BRAILLELEARNINGTYPE",brailleLearningType);
        i.putExtra("DATABASENAME",databaseTableName);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        startActivity(i);
    }


    /**
     * menu에 따라 결정되는 점자 정보 class를 얻는 함수
     * @param menuName : 선택된 메뉴 이름
     * @return : 점자 학습 정보 class 리턴
     */
    private GettingInformation getBrailleInformationObject(Menu menuName){
        BrailleInformationFactory brailleInformationFactory = new BrailleFactory();
        GettingInformation object = brailleInformationFactory.getInformationObject(menuName);
        return object;
    }



    /**
     *  focus set 함수
     * @param time : 딜레이 시간
     * @param mediaPlaying : 음성재생 여부 확인
     */
    private void requestFocus(int time, boolean mediaPlaying){
        if(accessibilityManager.isTouchExplorationEnabled() == false)
            focusThreadMaking(time, mediaPlaying);
        else {
            onStopSound();
            refreshSound();
            layout.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
            layout.requestFocus();
        }
    }


    /**
     * focus 인식 시간 스레드
     * media가 재생중이라면 소리를 출력하지 않고 focus set
     * 재생중이지 않다면 소리를 출력 후 화면에 focus set
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

        for(int i=0 ; i<bottomGuideCircleArray.size() ; i++){
            BottomCircle bottomCircle = bottomGuideCircleArray.get(i);
            bottomCircle.recycleCircleImage();
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

    /**
     * 상위 메뉴 이동 함수
     */
    private void beforeMenu(){
        menuAddressDeque.removeLast();
        if (!menuAddressDeque.isEmpty())
            NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque); // 현재 메뉴 리스트 숫자 리셋
        refreshData();
    }


    /**
     * 뒤로가기 버튼 재정의
     */
    @Override
    public void onBackPressed() {
        beforeMenu();
    }


    /**
     * 특수기능 이미지 메모리 해제 함수
     */
    private void recycleSpecialFunction(){
        if(specialImageView != null){
            Drawable image = specialImageView.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            specialImageView.setImageDrawable(null);
        }
    }


    /**
     * 특수기능 안내 멘트 및 화면 이미지 set 함수
     */
    @Override
    public void onSpecialFunctionGuide() {
        int size = specialFunctionManager.getSize()-1;
        if(size < specialFunctionIndex)
            specialFunctionIndex = 0;

        int drawableId = specialFunctionManager.getDrawableId(specialFunctionIndex);
        int soundId = specialFunctionManager.getSoundId(specialFunctionIndex++);
        setSpecialImageView(drawableId);
        mediaSoundManager.start(soundId);
    }


    /**
     * 화면에 특수기능 이미지를 띄우는 함수
     * @param drawableId
     */
    private void setSpecialImageView(int drawableId){
        recycleSpecialFunction();
        specialBackgroundView.setVisibility(View.VISIBLE);
        specialImageView.setVisibility(View.VISIBLE);
        specialImageView.setImageDrawable(imageResizeModule.getDrawableImage(drawableId, specialViewSize, specialViewSize)); //현재화면에 이미지 설정
    }


    /**
     * 특수기능 취소시 발생되는 함수
     * 현재 재생중인 음성 중지 -> 특수기능 취소 멘트 출력 -> 화면정보 새로고침
     */
    @Override
    public void onSpecialFunctionDisable() {
        onSpecialFunctionViewOff();
        mediaSoundManager.allStop();
        mediaSoundManager.start(R.raw.specialfunction_cancel);
        refreshSound();
    }


    /**
     * 특수기능 이미지 숨기는 함수
     * INVISIBLE 후 메모리 해제
     */
    private void onSpecialFunctionViewOff(){
        specialBackgroundView.setVisibility(View.INVISIBLE);
        specialImageView.setVisibility(View.INVISIBLE);
        recycleSpecialFunction();
    }


    /**
     * 특수기능 실행 함수
     * 특수기능 이미지 숨긴 뒤 특수기능 실행
     */
    @Override
    public void onStartSpecialFunction() {
        onSpecialFunctionViewOff();
        int specialType = --specialFunctionIndex;
        specialFunctionManager.checkFunction(specialType);
    }


    /**
     * 권한 설정에 동의하였을 경우
     */
    @Override
    public void onPermissionUseAgree() {
        customTouchConnectListener.setTouchLock(TouchLock.UNLOCK);
        permissionCheckModule.allowedPermission();

        FingerFunctionType type = FingerFunctionType.ENTER;
        mediaSoundManager.start(type);
        menuAddressDeque.addLast(0);
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque); // 현재 메뉴 리스트 숫자 리셋
        enterBrailleLearning();
    }

    /**
     * 권한 설정에 동의하지 않았을 경우
     */
    @Override
    public void onPermissionUseDisagree() {
        permissionCheckModule.cancelPermissionGuide();
        refreshData();
    }


    /**
     * 화면 새로고침 함수
     */
    @Override
    public void onRefresh() {
        refreshData();
    }

    @Override
    public void onSpeechRecognition() {
    }

    @Override
    public void onSaveMynote() {
    }

    @Override
    public void onDeleteMynote() {
    }
}
