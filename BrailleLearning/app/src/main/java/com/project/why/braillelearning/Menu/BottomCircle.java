package com.project.why.braillelearning.Menu;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.Module.ImageResizeModule;
import com.project.why.braillelearning.R;

import static android.view.View.GONE;

/**
 * Created by hyuck on 2018-03-14.
 */


/**
 * 화면 하단의 동그라미 이미지로 메뉴 페이지 정보를 안내하기 위한 정보를 담고 있는 클래스
 */
public class BottomCircle {
    private ImageResizeModule imageResizeModule;
    private ImageView circleImageView;
    private Context context;

    BottomCircle(Context context){
        this.context = context;
        imageResizeModule = new ImageResizeModule(context.getResources());
        createCircleImage();
    }


    /**
     * 현재 화면인지 아닌지를 set하는 함수
     * @param nowPage : true(현재화면), false(현재화면 아님)
     */
    public void setNowPage(boolean nowPage){
        int circleWidth = circleImageView.getLayoutParams().width;
        int circleHeight = circleImageView.getLayoutParams().height;


;
        if(nowPage == true)
            setCircleImage(imageResizeModule.getDrawableImage(R.drawable.bigcircle, circleWidth, circleHeight));
        else
            setCircleImage(imageResizeModule.getDrawableImage(R.drawable.minicircle, circleWidth, circleHeight));

        circleImageView.setVisibility(View.VISIBLE);
    }


    /**
     * 동그라미 imageView를 get하는 함수
     * @return 동그라미 imageView
     */
    public ImageView getCircleImageView(){
        return circleImageView;
    }


    /**
     * 이미지의 메모리 해제 함수
     */
    public void recycleCircleImage(){
        Drawable image = circleImageView.getDrawable();
        if(image instanceof BitmapDrawable){
            ((BitmapDrawable)image).getBitmap().recycle();
            image.setCallback(null);
        }
        circleImageView.setImageDrawable(null);
        circleImageView.setVisibility(GONE);
    }


    /**
     * 동그라미 이미지 set함수
     * @param drawable 큰 동그라미 혹은 작은 동그라미
     */
    private void setCircleImage(Drawable drawable){
        if(circleImageView == null)
            createCircleImage();
        recycleCircleImage();
        circleImageView.setImageDrawable(drawable);
    }


    /**
     * 동그라미 imageview 생성 함수
     */
    private void createCircleImage(){
        circleImageView = new ImageView(context);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                Gravity.CENTER
        );

        lp.width = (int) (Global.DisplayY * 0.03);
        lp.height = (int) (Global.DisplayY * 0.03);
        circleImageView.setLayoutParams(lp);
        circleImageView.setImageDrawable(null);
        circleImageView.setVisibility(GONE);
    }
}
