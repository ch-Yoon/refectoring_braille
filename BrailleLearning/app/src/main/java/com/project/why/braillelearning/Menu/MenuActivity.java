package com.project.why.braillelearning.Menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import static com.project.why.braillelearning.R.drawable.kakao_image;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braille_learning_menu);
        InitMenu();
        setLayout();
        initTouchEvent();
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            setFullScreen();
        }
    }


    public void setFullScreen(){ // 전체화면 함수
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


    public void setkakaoLogo(){
        kakaoImageView.setImageDrawable(ContextCompat.getDrawable(this, kakao_image));
    }

    /**
     * 메뉴 초기화 함수
     */
    public void InitMenu(){
        initImageView(); // 메뉴 imageview 사이즈 조절
        imageResizeModule = new ImageResizeModule(getResources());
        mediaSoundManager = new MediaSoundManager(this);
        menuTreeManager = new MenuTreeManager();
        menuAddressDeque = new LinkedList<>();
        menuAddressDeque.addLast(PageNumber);
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
        multiFingerFunction = new MultiFinger(this);
        setFullScreen();
    }


    /**
     * 메뉴 imageview setting 함수
     */
    public void initImageView(){
        MenuImageView = (ImageView) findViewById(R.id.braillelearningmenu_imageview);
        kakaoImageView = (ImageView) findViewById(R.id.imageView_kakao);
        MenuImageSize = (int)(Global.DisplayY*0.8); // imageview의 width와 height는 세로 높이의 80%
        MenuImageView.getLayoutParams().height = MenuImageSize;
        MenuImageView.getLayoutParams().width = MenuImageSize;
        MenuImageView.requestLayout();
    }

    /**
     * layout 설정
     */
    public void setLayout(){
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
    public void initTouchEvent(){
        customTouchConnectListener = new CustomTouchEvent(this, this);
        connectTouchEvent();
    }


    /**
     * touchevent module 연결
     */
    public void connectTouchEvent(){
        customTouchConnectListener.onResume();
    }

    /**
     * touchevent module 일시중지
     */
    public void pauseTouchEvent(){
        customTouchConnectListener.onPause();
    }


    /**
     * 화면 새로고침 함수
     * 만약 menuAddressDeque가 비어있을 경우, Application을 종료하는 것을 의미
     */
    public void refreshData() { // 메뉴 이미지 설정 함수
        if(menuAddressDeque.isEmpty())  // 메뉴 Adress Deque가 비어있으면 종료
            finish();
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
    public void refreshImage(TreeNode menuNode){
        recycleImage();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_fade);
        MenuImageView.startAnimation(animation);
        MenuImageView.setImageDrawable(imageResizeModule.getDrawableImage(menuNode.getImageId(), MenuImageSize, MenuImageSize)); //현재화면에 이미지 설정
        layout.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
    }

    /**
     * 음성 새로고침 함수
     * @param menuNode : 현재 화면을 의미하는 treeNode
     */
    public void refreshSound(TreeNode menuNode){
        int id = menuNode.getSoundId();
        mediaSoundManager.start(id);
    }

    public void refreshSound(){
        onStopSound();
        TreeNode menuNode = menuTreeManager.getMenuTreeNode(menuAddressDeque);
        refreshSound(menuNode);
    }

    /**
     * 점자 학습 화면으로 들어가는 함수
     * 자신이 선택한 menu name을 학습화면으로 전달
     */
    public void enterBrailleLearning(){
        menuAddressDeque.removeLast();
        recycleLogo();
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
        Menu menuName = menuTreeManager.getMenuName(menuAddressDeque);
        Intent i = new Intent(this, BrailleLearningActivity.class);
        i.putExtra("MENUNAME",menuName);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        startActivity(i);
    }


    /**
     * 화면에 focus를 잡아주는 함수
     * 이미지가 변결될때마다 호출됨
     */
    @Override
    public void onFocusRefresh() {
        layout.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER);
        refreshSound();
    }

    @Override
    public void onStopSound() {
        mediaSoundManager.stop();
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
    public void recycleImage(){     //이미지 메모리 해제 함수
        if(MenuImageView != null) {
            Drawable image = MenuImageView.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            MenuImageView.setImageDrawable(null);
        }
    }
    public void recycleLogo(){
        kakaoImageView.setImageDrawable(null);
    }
}
