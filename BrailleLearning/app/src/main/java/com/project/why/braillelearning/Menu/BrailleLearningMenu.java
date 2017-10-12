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

import com.project.why.braillelearning.EnumConstant.FingerFunctionType;
import com.project.why.braillelearning.EnumConstant.Menu;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningControl.BasicTwoFinger;
import com.project.why.braillelearning.LearningControl.BrailleLearning;
import com.project.why.braillelearning.LearningControl.FingerCoordinate;
import com.project.why.braillelearning.LearningControl.FingerFunction;
import com.project.why.braillelearning.LearningControl.TwoFingerFunction;
import com.project.why.braillelearning.Module.ImageResizeModule;
import com.project.why.braillelearning.Module.MediaPlayerSingleton;
import com.project.why.braillelearning.R;

import java.util.Deque;
import java.util.LinkedList;

public class BrailleLearningMenu extends Activity implements FingerFunction {
    private int TWO_FINGER = FingerFunctionType.TWO_FINGER.getNumber(); // 손가락 2개
    private boolean MultiFinger = false; // 멀티터치 체크 변수
    private int PageNumber=0; // 메뉴 위치를 알기위한 변수
    private int MenuImageSize;
    private ImageView MenuImageView; // 메뉴 이미지뷰
    private MenuTreeManager menuTreeManager; // 메뉴 트리 manager
    private ImageResizeModule imageResizeModule;
    private Deque<Integer> menuAddressDeque; // 메뉴 탐색을 위한 주소 경로를 담는 Deque
    private int NowMenuListSize = 0; // 현재 위치한 메뉴 리스트의 길이를 저장하는 변수
    //private MediaPlayerModule mediaPlayerModule;
    private MediaPlayerSingleton mediaPlayerModule;
    private TwoFingerFunction twoFinger;
    private FingerCoordinate fingerCoordinate;
    private FingerFunctionType type = FingerFunctionType.NONE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_braille_learning_menu);
        MenuImageView = (ImageView) findViewById(R.id.braillelearningmenu_imageview);
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
        pauseMediaPlayer();
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
        setMenuImageSize(); // 메뉴 imageview 사이즈 조절
        imageResizeModule = new ImageResizeModule(getResources());
        mediaPlayerModule = MediaPlayerSingleton.getInstance();
        mediaPlayerModule.setContext(this);
        menuTreeManager = new MenuTreeManager();
        menuAddressDeque = new LinkedList<>();
        menuAddressDeque.addLast(PageNumber);
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
        twoFinger = new BasicTwoFinger();
        fingerCoordinate = new FingerCoordinate(TWO_FINGER);
    }

    public void setMenuImageSize(){ // 이미지 size setting
        MenuImageSize = (int)(Global.DisplayY*0.8); // imageview의 width와 height는 세로 높이의 80%
        MenuImageView.getLayoutParams().height = MenuImageSize;
        MenuImageView.getLayoutParams().width = MenuImageSize;
        MenuImageView.requestLayout();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int Pointer_Count = event.getPointerCount(); // 현재 발생된 터치 event의 수

        if(Pointer_Count > TWO_FINGER) // 발생된 터치 이벤트가 2개를 초과하여도 2개까지만 인식
            Pointer_Count = TWO_FINGER;

        if(Pointer_Count == FingerFunctionType.ONE_FINGER.getNumber()){
            switch(event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN: // 손가락 1개를 화면에 터치하였을 때 발생되는 Event
                    fingerCoordinate.setDownCoordinate(event, Pointer_Count);
                    break;
                case MotionEvent.ACTION_UP: // 손가락 1개를 화면에서 떨어트렸을 때 발생되는 Event
                    fingerCoordinate.setUpCoordinate(event, Pointer_Count);
                    oneFinegerFunction();
                    break;
            }
        } else if(Pointer_Count == FingerFunctionType.TWO_FINGER.getNumber()){
            switch(event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_POINTER_DOWN: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    fingerCoordinate.setDownCoordinate(event, Pointer_Count);
                    break;
                case MotionEvent.ACTION_POINTER_UP: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                    fingerCoordinate.setUpCoordinate(event, Pointer_Count);
                    twoFingerFunction();

                    break;
            }
        }

        return true;
    }

    @Override
    public boolean oneFinegerFunction() {
        if(MultiFinger == false) {// 싱글터치
            CheckMenuType();
        } else
            MultiFinger = false; // 멀티 터치를 사용하기 위한 체크 변수 초기화
        return false;
    }

    @Override
    public boolean twoFingerFunction() {
        if(MultiFinger == false)
            MultiFinger = true;

        CheckMenuType();
        return false;
    }

    @Override
    public boolean threeFingerFunction() {
        return false;
    }

    public void CheckMenuType(){ // 화면 전환을 위한 CustomTouchEvent 함수
        if(MultiFinger==false) // 싱글터치
            type = FingerFunctionType.ENTER;
        else  // 멀티터치
            type = twoFinger.getTwoFingerFunctionType(fingerCoordinate.getDownX(), fingerCoordinate.getDownY(), fingerCoordinate.getUpX(), fingerCoordinate.getUpY());

        if(type != FingerFunctionType.NONE)
            CheckMenuChange();
    }

    public void CheckMenuChange(){ // 화면전환 체크 함수
        switch(type){
            case ENTER: // 메뉴 접속
                menuAddressDeque.addLast(0);
                NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque); // 현재 메뉴 리스트 숫자 리셋
                refreshData();
                break;
            case BACK: // 상위 메뉴
                menuAddressDeque.removeLast();
                if(!menuAddressDeque.isEmpty())
                    NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque); // 현재 메뉴 리스트 숫자 리셋
                refreshData();
                break;
            case NEXT: // 오른쪽 메뉴
                menuAddressDeque.addLast(getPageNumber(menuAddressDeque.removeLast()+1));
                refreshData();
                break;
            case PREVIOUS: // 왼쪽 메뉴
                menuAddressDeque.addLast(getPageNumber(menuAddressDeque.removeLast()-1));
                refreshData();
                break;
            case SPECIAL: // 특수기능
                refreshSound();
                break;
            default:
                break;
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
    }

    public void refreshImage(TreeNode menuNode){
        recycleImage();
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_fade);
        MenuImageView.startAnimation(animation);
        MenuImageView.setImageDrawable(imageResizeModule.getDrawableImage(menuNode.getImageId(), MenuImageSize, MenuImageSize)); //현재화면에 이미지 설정
    }

    public void refreshSound(TreeNode menuNode){
        mediaPlayerModule.SoundPlay(type.getNumber(), menuNode.getSoundId());
        type = FingerFunctionType.NONE;
    }

    public void refreshSound(){
        TreeNode MenuNode = menuTreeManager.getMenuTreeNode(menuAddressDeque);
        mediaPlayerModule.SoundPlay(type.getNumber(), MenuNode.getSoundId());
        type = FingerFunctionType.NONE;
    }

    public void enterBrailleLearning(){
        menuAddressDeque.removeLast();
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
        Menu menuName = menuTreeManager.getMenuName(menuAddressDeque);

        Intent i = new Intent(this, BrailleLearning.class);
        i.putExtra("MENUNAME",menuName);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        startActivity(i);
    }

    public void pauseMediaPlayer(){
        mediaPlayerModule.InitMediaPlayer();
    }

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
}
