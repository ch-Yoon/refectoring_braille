package com.project.why.braillelearning.LearningView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.LearningModel.Dot;
import com.project.why.braillelearning.Module.ImageResizeModule;
import com.project.why.braillelearning.R;

/**
 * Created by hyuck on 2017-09-14.
 */

/**
 * 점자를 화면에 출력하는 view class
 * observer로 관찰하는 brailleData를 활용하여 화면에 점자를 그림
 */
public class BasicView extends View implements ViewObservers {
    private Context context;
    private TextView textName;
    private String letterName;
    private Dot[][] brailleMatrix;
    private ImageView logoView;
    private ImageView background, specialview;
    private ImageResizeModule imageResizeModule;

    public BasicView(Context context){
        super(context);
        this.context = context;
        imageResizeModule = new ImageResizeModule(getResources());
    }


    @Override
    protected void onDraw(Canvas canvas){
        Log.d("ondraw","ondraw");
        drawBraille(canvas);
    }


    /**
     * 화면에 점자와 글자 그리는 함수
     * @param canvas
     */
    private void drawBraille(Canvas canvas){
        if(brailleMatrix != null){
            setBraille(canvas);
        }
        setTextName();
        setKakaoLogo();
        setSpecialview();
    }


    @Override
    public void onSpecialFunctionEnable(int drawable) {
        background.setVisibility(VISIBLE);
        specialview.setVisibility(VISIBLE);

        recycleSpecialImage();
        specialview.setImageDrawable(imageResizeModule.getDrawableImage(drawable, Global.DisplayX / 3, Global.DisplayX / 3));
        invalidate();
    }


    @Override
    public void onSpecialFunctionDisable(){
        background.setVisibility(INVISIBLE);
        specialview.setVisibility(INVISIBLE);
        recycleSpecialImage();
    }


    private void setSpecialview(){
        if(background == null) {
            background = new ImageView(context);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    Gravity.CENTER
            );

            background.setLayoutParams(lp);
            background.setBackgroundColor(Color.parseColor("#ee171a2d"));
            ((FrameLayout) this.getParent()).addView(background);
            background.setVisibility(INVISIBLE);
        }

        if(specialview == null) {
            specialview = new ImageView(context);

            FrameLayout.LayoutParams lp2 = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
            );

            lp2.width = Global.DisplayX / 3;
            lp2.height = Global.DisplayX / 3;

            specialview.setLayoutParams(lp2);
            specialview.requestLayout();
            ((FrameLayout) this.getParent()).addView(specialview);
            specialview.setVisibility(INVISIBLE);
        }
    }


    /**
     * 점자를 뜻하는 글자를 담는 textview set 함수
     */
    private void setTextName(){
        if(textName == null) {
            textName = new TextView(context);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER_HORIZONTAL
            );
            textName.setTextColor(Color.WHITE);
            textName.setPadding(0, 50, 0, 0);
            textName.setGravity(Gravity.CENTER);
            textName.setTextSize(60);
            textName.setLayoutParams(lp);
            ((FrameLayout) this.getParent()).addView(textName);
        }
        textName.setText(letterName);
    }


    /**
     * 카카오 로고 set하는 함수
     */
    private void setKakaoLogo(){
        if(logoView == null) {
            logoView = new ImageView(context);

            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.RIGHT
            );

            logoView.setLayoutParams(lp);
            lp.topMargin = 80;
            lp.rightMargin = 80;

            ((FrameLayout) this.getParent()).addView(logoView);

            logoView.getLayoutParams().width = 408;
            logoView.getLayoutParams().height = 60;
            logoView.setImageDrawable(imageResizeModule.getDrawableImage(R.drawable.kakao_image, 408, 60)); //현재화면에 이미지 설정
            logoView.requestLayout();
        }
    }


    /**
     * 점자를 화면에 그리는 함수
     * @param canvas
     */
    private void setBraille(Canvas canvas) {
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        int col = brailleMatrix.length; // 점자는 4행으로 이루어짐
        int row = brailleMatrix[0].length;
        for (int i=0; i<col; i++) {
            for (int j=0; j<row; j++) {
                float coordinate_X = brailleMatrix[i][j].getX();
                float coordinate_Y = brailleMatrix[i][j].getY();
                int dotType = brailleMatrix[i][j].getDotType();
                float radius = brailleMatrix[i][j].getViewAreaRidus();
                int color = getColor(dotType);
                paint.setColor(color);
                canvas.drawCircle(coordinate_X, coordinate_Y, radius, paint); // 점자 그리기
            }
        }
    }


    /**
     * 점자 번호에 따라 점의 색을 구분
     * @param dotType : 점자 번호
     * @return
     */
    private int getColor(int dotType){
        if(dotType == 7 || dotType == 8)
            return ContextCompat.getColor(context, R.color.AppBasicColor);
        else
            return Color.WHITE;
    }


    @Override
    public View getView() {
        return this;
    }


    /**
     * 화면 초기화
     */
    @Override
    public void onPause() {
        if(textName != null)
            textName.setText("");
        letterName = "";
        brailleMatrix = null;
        textName = null;
        recycleLogo();
        recycleSpecialView();
    }


    /**
     * brailleData를 callback받는 함수
     * @param letterName : 점자를 의미하는 글자
     * @param brailleMatrix : 점자의 모든 좌표값
     */
    @Override
    public void nodifyBraille(String letterName, Dot[][] brailleMatrix) {
        this.letterName = letterName;
        this.brailleMatrix = brailleMatrix;
        invalidate();
    }


    /**
     * 카카오 로고 이미지 메모리 해제 함수
     */
    private void recycleLogo(){
        if(logoView != null){
            Drawable image = logoView.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            logoView.setImageDrawable(null);
            ((FrameLayout) this.getParent()).removeView(logoView);
            logoView = null;
        }
    }

    private void recycleSpecialView(){
        if(background != null) {
            ((FrameLayout) this.getParent()).removeView(background);
            background = null;
        }

        recycleSpecialImage();
        ((FrameLayout) this.getParent()).removeView(specialview);
        specialview = null;
    }

    private void recycleSpecialImage(){
        if(specialview != null){
            Drawable image = specialview.getDrawable();
            if(image instanceof BitmapDrawable){
                ((BitmapDrawable)image).getBitmap().recycle();
                image.setCallback(null);
            }
            specialview.setImageDrawable(null);
        }
    }
}


