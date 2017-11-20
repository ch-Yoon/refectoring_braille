package com.project.why.braillelearning.Menu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Menu;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningControl.MultiFinger;
import com.project.why.braillelearning.LearningControl.BrailleLearningActivity;
import com.project.why.braillelearning.LearningControl.FingerCoordinate;
import com.project.why.braillelearning.LearningControl.SingleFIngerFactory;
import com.project.why.braillelearning.LearningControl.SingleFingerFunction;
import com.project.why.braillelearning.MediaPlayer.MediaSoundManager;
import com.project.why.braillelearning.Module.ImageResizeModule;
import com.project.why.braillelearning.R;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Menu를 화면에 출력하는 Activity
 * MenuTreeManager에서 메뉴 image와 음성 file들을 관리하고 있음
 */
public class MenuActivity extends Activity {
    private BrailleLearningType brailleLearningType = BrailleLearningType.MENU;
    private int ONE_FINGER = FingerFunctionType.ONE_FINGER.getNumber(); // 손가락 1개
    private int TWO_FINGER = FingerFunctionType.TWO_FINGER.getNumber(); // 손가락 2개
    private int THREE_FINGER = FingerFunctionType.THREE_FINGER.getNumber(); // 손가락 3개
    private boolean multiFinger = false; // 멀티터치 체크 변수
    private boolean functionLock = false;
    private int PageNumber=0; // 메뉴 위치를 알기위한 변수
    private int MenuImageSize;
    private ImageView MenuImageView; // 메뉴 이미지뷰
    private MenuTreeManager menuTreeManager; // 메뉴 트리 manager
    private ImageResizeModule imageResizeModule;
    private Deque<Integer> menuAddressDeque; // 메뉴 탐색을 위한 주소 경로를 담는 Deque
    private int NowMenuListSize = 0; // 현재 위치한 메뉴 리스트의 길이를 저장하는 변수
    private MediaSoundManager mediaSoundManager;
    private SingleFingerFunction singleFingerFunction;
    private MultiFinger multiFingerFunction;
    private FingerCoordinate fingerCoordinate;
    private FingerFunctionType type = FingerFunctionType.NONE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braille_learning_menu);
        InitMenu();
    }

    @Override
    protected void onResume(){
        super.onResume();
        refreshData();
    }

    @Override
    protected void onPause(){
        super.onPause();
        recycleImage();
        stopMediaPlayer();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
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

    public void InitMenu(){ // 메뉴 초기화 함수
        initImageView(); // 메뉴 imageview 사이즈 조절
        imageResizeModule = new ImageResizeModule(getResources());
        mediaSoundManager = new MediaSoundManager(this);
        menuTreeManager = new MenuTreeManager();
        menuAddressDeque = new LinkedList<>();
        menuAddressDeque.addLast(PageNumber);
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
        singleFingerFunction = getSingleFingerFunction();
        multiFingerFunction = new MultiFinger(this);
        fingerCoordinate = new FingerCoordinate(THREE_FINGER);
    }

    /**
     * 손가락 1개에 대한 event 모듈을 setting하는 함수
     * @return : 손가락 1개 event 모듈 리턴
     */
    public SingleFingerFunction getSingleFingerFunction(){
        SingleFIngerFactory singleFIngerFactory = new SingleFIngerFactory(this, brailleLearningType);
        return singleFIngerFactory.getSingleFingerMoudle();
    }

    public void initImageView(){ // 이미지 size setting
        MenuImageView = (ImageView) findViewById(R.id.braillelearningmenu_imageview);
        MenuImageSize = (int)(Global.DisplayY*0.8); // imageview의 width와 height는 세로 높이의 80%
        MenuImageView.getLayoutParams().height = MenuImageSize;
        MenuImageView.getLayoutParams().width = MenuImageSize;
        MenuImageView.requestLayout();
    }

    /**
     * 손가락 1개, 2개에 대한 event를 발생하는 touchevent 함수
     * @param event : 발생된 touchevent
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointer_Count = event.getPointerCount(); // 현재 발생된 터치 event의 수

        if(pointer_Count > THREE_FINGER) // 발생된 터치 이벤트가 2개를 초과하여도 2개까지만 인식
            pointer_Count = THREE_FINGER;

        if(pointer_Count == ONE_FINGER){
            switch(event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: // 손가락 1개를 화면에 터치하였을 때 발생되는 Event
                    multiFinger = false;
                    functionLock = false;
                    fingerCoordinate.setDownCoordinate(event, pointer_Count);
                    stopMediaPlayer();
                    break;
                case MotionEvent.ACTION_UP: // 손가락 1개를 화면에서 떨어트렸을 때 발생되는 Event
                    if(multiFinger == false) {
                        fingerCoordinate.setUpCoordinate(event, pointer_Count);
                        CheckMenuType();
                    }
                    break;
            }
        } else if(pointer_Count > ONE_FINGER){
            switch(event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    multiFinger = true;
                    fingerCoordinate.setDownCoordinate(event, pointer_Count);
                    break;
                case MotionEvent.ACTION_POINTER_UP: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    fingerCoordinate.setUpCoordinate(event, pointer_Count);
                    CheckMenuType();
                    functionLock = true;
                    break;
            }
        }

        return false;
    }

    /**
     * 현재 화면에 출력될 메뉴를 check
     * 손가락 2개에서 발생되는 event에 따라, 메뉴가 이동됨.
     */
    public void CheckMenuType(){ // 화면 전환을 위한 CustomTouchEvent 함수
        if(functionLock == false) {
            if (multiFinger == false) // 싱글터치
                type = singleFingerFunction.oneFingerFunction(null, fingerCoordinate);
            else  // 멀티터치
                type = multiFingerFunction.getFingerFunctionType(fingerCoordinate);

            switch (type) {
                case ENTER: // 메뉴 접속
                    menuAddressDeque.addLast(0);
                    NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque); // 현재 메뉴 리스트 숫자 리셋
                    refreshData();
                    break;
                case BACK: // 상위 메뉴
                    menuAddressDeque.removeLast();
                    if (!menuAddressDeque.isEmpty())
                        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque); // 현재 메뉴 리스트 숫자 리셋
                    refreshData();
                    break;
                case RIGHT: // 오른쪽 메뉴
                    menuAddressDeque.addLast(getPageNumber(menuAddressDeque.removeLast() + 1));
                    refreshData();
                    break;
                case LEFT: // 왼쪽 메뉴
                    menuAddressDeque.addLast(getPageNumber(menuAddressDeque.removeLast() - 1));
                    refreshData();
                    break;
                case REFRESH: // 특수기능
                    refreshData();
                    break;
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
    }

    public int getPageNumber(int PageNumber){ // 메뉴 번호 얻어오는 함수
        int Startpage = 0;
        int Lastpage = NowMenuListSize-1; // 마지막 페이지는 현재 메뉴 리스트의 마지막 페이지

        if(PageNumber > Lastpage)  // 메뉴를 원형으로 연결
            PageNumber = Startpage;
        else if(PageNumber < Startpage)
            PageNumber = Lastpage;

        return PageNumber;
    }

    /**
     * 화면 새로고침 함수
     * 만약 menuAddressDeque가 비어있을 경우, Application을 종료하는 것을 의미
     */
    public void refreshData() { // 메뉴 이미지 설정 함수
        if(menuAddressDeque.isEmpty())  // 메뉴 Adress Deque가 비어있으면 종료
            finish();
        else {
            TreeNode menuNode = menuTreeManager.getMenuTreeNode(menuAddressDeque);
            if (menuNode != null) {
                refreshImage(menuNode);
                refreshSound(menuNode);
            } else  // 하위메뉴가 존재하지 않는다면 방금 선택한 경로 삭제 후 점자 학습 화면 이동
                enterBrailleLearning();
        }

        type = FingerFunctionType.NONE;
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
    }

    /**
     * 음성 새로고침 함수
     * @param menuNode : 현재 화면을 의미하는 treeNode
     */
    public void refreshSound(TreeNode menuNode){
        int id = menuNode.getSoundId();
        mediaSoundManager.start(id);
    }

    /**
     * 점자 학습 화면으로 들어가는 함수
     * 자신이 선택한 menu name을 학습화면으로 전달
     */
    public void enterBrailleLearning(){
        menuAddressDeque.removeLast();
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
        Menu menuName = menuTreeManager.getMenuName(menuAddressDeque);
        Intent i = new Intent(this, BrailleLearningActivity.class);
        i.putExtra("MENUNAME",menuName);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        startActivity(i);
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

    public void stopMediaPlayer(){
        mediaSoundManager.stop();
    }
}
