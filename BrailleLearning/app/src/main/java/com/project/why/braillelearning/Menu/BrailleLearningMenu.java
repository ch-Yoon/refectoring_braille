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

import com.project.why.braillelearning.EnumConstant.Menu;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningControl.BrailleLearning;
import com.project.why.braillelearning.Module.ImageResizeModule;
import com.project.why.braillelearning.Module.MediaPlayerModule;
import com.project.why.braillelearning.R;

import java.util.Deque;
import java.util.LinkedList;

public class BrailleLearningMenu extends Activity {
    private final int NONE = -1; // 해당 안됨
    private final int NEXT = 0; // 다음페이지
    private final int PREVIOUS = 1; // 이전페이지
    private final int ENTER = 2; // 페이지 접속
    private final int BACK = 3; // 뒤로가기
    private final int SPECIALFUNCTION = 4;
    private final int TWO_FINGER = 2; // 손가락 2개
    private View decorView;
    private int uiOption;
    private int TwoFinger_downX[] = new int[TWO_FINGER]; // 손가락 2개 down 좌표
    private int TwoFinger_downY[] = new int[TWO_FINGER];
    private int TwoFinger_upX[] = new int[TWO_FINGER]; // 손가락 2개 up 좌표
    private int TwoFinger_upY[] = new int[TWO_FINGER];
    private boolean MultiFinger = false; // 멀티터치 체크 변수
    private boolean DragCheck = false; // 화면전환 체크 변수
    private int PageNumber=0; // 메뉴 위치를 알기위한 변수
    private int MenuImageSize;
    private ImageView MenuImageView; // 메뉴 이미지뷰
    private MenuTreeManager menuTreeManager; // 메뉴 트리 manager
    private ImageResizeModule imageResizeModule;
    private Deque<Integer> menuAddressDeque; // 메뉴 탐색을 위한 주소 경로를 담는 Deque
    private int NowMenuListSize = 0; // 현재 위치한 메뉴 리스트의 길이를 저장하는 변수
    private MediaPlayerModule mediaPlayerModule;



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
        setMenuImage(NONE);
    }

    @Override
    protected void onPause(){
        super.onPause();
        recycleImage();
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
        mediaPlayerModule = new MediaPlayerModule(this);
        menuTreeManager = new MenuTreeManager();
        menuAddressDeque = new LinkedList<>();
        menuAddressDeque.addLast(PageNumber);
        NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
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

        switch(event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: // 손가락 1개를 화면에 터치하였을 때 발생되는 Event
                MultiFinger = false; // 멀티 터치를 사용하기 위한 체크 변수 초기화
                DragCheck = false; // 화면전환을 하기 위한 체크 변수 초기화
                break;
            case MotionEvent.ACTION_UP: // 손가락 1개를 화면에서 떨어트렸을 때 발생되는 Event
                if(MultiFinger == false) // 싱글터치
                    CheckMenuType();
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                MultiFinger = true; // 두번째 손가락이 화면에 터치될 때
                if(DragCheck==false) { // 화면 전환 체크를 하지 않은 경우 멀티 터치 down 좌표 셋팅
                    for (int i = 0; i < Pointer_Count; i++) {
                        TwoFinger_downX[i] = (int) event.getX(i);
                        TwoFinger_downY[i] = (int) event.getY(i);
                    }
                }
                break;
            case MotionEvent.ACTION_POINTER_UP: // 다수의 손가락이 화면에 닿았을 때 발생되는 Event
                for(int i=0; i<Pointer_Count ; i++) { // 멀티 터치 up 좌표 셋팅
                    TwoFinger_upX[i] = (int)event.getX(i);
                    TwoFinger_upY[i] = (int)event.getY(i);
                }
                CheckMenuType(); // 화면전환 체크
                break;
        }
        return true;
    }

    public void CheckMenuType(){ // 화면 전환을 위한 CustomTouchEvent 함수
        int Type=0;

        if(MultiFinger==false){ // 싱글터치
            Type = ENTER;
            CheckMenuChange(Type); // 화면전환 체크
        } else { // 멀티터치
            double Finger_gapX[] = new double[TWO_FINGER]; // 첫번째와 두번째 손가락의 downX 좌표와 upX좌표의 격차
            double Finger_gapY[] = new double[TWO_FINGER]; // 첫번째와 두번째 손가락의 downY 좌표와 upY좌표의 격차
            int Drag_countX=0; // 좌측 이동인지 우측 이동인지를 확인하기 위한 변수
            int Drag_countY=0; // 뒤로가기인지 특수기능인지를 확인하기 위한 변수

            double DragSpace = Global.DisplayX*(0.2); // 화면전환 범위는 해상도 가로축의 20%

            for(int i=0 ; i<TWO_FINGER ; i++){
                Finger_gapX[i] = TwoFinger_downX[i] - TwoFinger_upX[i]; //손가락 2개의 x좌표 격차
                Finger_gapY[i] = TwoFinger_downY[i] - TwoFinger_upY[i]; //손가락 2개의 Y좌표 격차

                if(Finger_gapX[i]>DragSpace) // x격차가 양수이면서 화면전환 허용 범위 충족시 오른쪽 화면전환 변수 값 증가
                    Drag_countX++;
                else if(Finger_gapX[i]<DragSpace*(-1)) // x격차가 음수이면서 화면전환 허용 범위 충족시 왼쪽 화면 전환 변수 값 증가
                    Drag_countX--;

                if(Finger_gapY[i]<DragSpace*(-1)) // y격차가 음수이면서 화면전환 허용 범위 충족시 뒤로가기 전환 변수 값 증가
                    Drag_countY++; // 왼쪽 화면전환 변수 값 증가
                else if(Finger_gapY[i]>DragSpace) // y격차가 양수이면서 화면전환 허용 범위 충족시 특수기능 변수 값 증가
                    Drag_countY--; // 오른쪽 화면전환 변수 값 증가
            }

            Type = getTwofingerFunctionType(Drag_countX, Drag_countY, Finger_gapX, Finger_gapY);
            if(Type != NONE) // 손가락 2개를 활용하는 특정 조건을 만족하였을 경우
                CheckMenuChange(Type);
        }
    }

    public int getTwofingerFunctionType(int Drag_countX, int Drag_countY, double Finger_gapX[], double Finger_gapY[]){
        int Type=0;
        boolean DragX = false; // 화면 전환을 충족했다면 true
        boolean DragY = false;

        if(Drag_countX == TWO_FINGER || Drag_countX == TWO_FINGER*(-1))
            DragX = true;
        else if(Drag_countY == TWO_FINGER || Drag_countY == TWO_FINGER*(-1))
            DragY = true;

        if(DragX == false && DragY == false){ // x과 y축 모두 화면전환 조건을 충족하지 못했을 경우
            Type = NONE;
        } else if(DragX == true && DragY == false){ // x축만 화면전환 조건을 충족하였을 경우
            if(Drag_countX > 0) // 우측 페이지 전환
                Type = NEXT;
            else // 좌측 페이지 전환
                Type = PREVIOUS;
        } else if(DragX == false && DragY == true){ // y축만 화면전환 조건을 충족하였을 경우
            if(Drag_countY > 0) // 특수기능
                Type = SPECIALFUNCTION;
            else // 뒤로가기
                Type = BACK;
        } else if(DragX == true && DragY == true){ // x축 화면전환 조건과 y축 화면전환 조건 모두 충족하였을 경우 이동거리로 구분
            double gapX=0;
            double gapY=0;

            for(int i=0 ; i<TWO_FINGER ; i++){
                gapX = gapX + Finger_gapX[i];
                gapY = gapY + Finger_gapY[i];
            }

            if(gapX >= gapY){ // x축의 이동거리가 클 경우
                if(Drag_countX > 0) // 우측 페이지 전환
                    Type = NEXT;
                else // 좌측 페이지 전환
                    Type = PREVIOUS;
            } else if(gapY > gapX){ // y축의 이동거리가 클 경우
                if(Drag_countY > 0) // 특수기능
                    Type = SPECIALFUNCTION;
                else // 뒤로가기
                    Type = BACK;
            }
        }

        return Type;
    }

    public void CheckMenuChange(int Type){ // 화면전환 체크 함수
        switch(Type){
            case ENTER: // 메뉴 접속
                menuAddressDeque.addLast(0);
                NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque); // 현재 메뉴 리스트 숫자 리셋
                setMenuImage(Type);
                break;
            case BACK: // 상위 메뉴
                menuAddressDeque.removeLast();
                if(!menuAddressDeque.isEmpty())
                    NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque); // 현재 메뉴 리스트 숫자 리셋
                setMenuImage(Type);
                break;
            case NEXT: // 오른쪽 메뉴
                menuAddressDeque.addLast(getPageNumber(menuAddressDeque.removeLast()+1));
                setMenuImage(Type);
                break;
            case PREVIOUS: // 왼쪽 메뉴
                menuAddressDeque.addLast(getPageNumber(menuAddressDeque.removeLast()-1));
                setMenuImage(Type);
                break;
            case SPECIALFUNCTION: // 특수기능

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

    public void setMenuImage(int FingerFunctinoType) { // 메뉴 이미지 설정 함수
        if(menuAddressDeque.isEmpty()) { // 메뉴 Adress Deque가 비어있으면 종료
            finish();
        }
        else {
            TreeNode MenuNode = menuTreeManager.getMenuTreeNode(menuAddressDeque);
            if (MenuNode != null) {
                recycleImage();
                Animation animation = AnimationUtils.loadAnimation(this, R.anim.image_fade);
                MenuImageView.startAnimation(animation);
                MenuImageView.setImageDrawable(imageResizeModule.getDrawableImage(MenuNode.getImageId(), MenuImageSize, MenuImageSize)); //현재화면에 이미지 설정
                mediaPlayerModule.SoundPlay(FingerFunctinoType, MenuNode.getSoundId());
            } else { // 하위메뉴가 존재하지 않는다면 방금 선택한 경로 삭제 후 점자 학습 화면 이동
                menuAddressDeque.removeLast();
                NowMenuListSize = menuTreeManager.getMenuListSize(menuAddressDeque);
                Menu menuName = menuTreeManager.getMenuName(menuAddressDeque);
                enterBrailleLearning(menuName);
            }
        }
    }

    public void enterBrailleLearning(Menu menuName){
        Intent i = new Intent(this, BrailleLearning.class);
        i.putExtra("MENUNAME",menuName);
        overridePendingTransition(R.anim.fade, R.anim.hold);
        startActivity(i);
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
