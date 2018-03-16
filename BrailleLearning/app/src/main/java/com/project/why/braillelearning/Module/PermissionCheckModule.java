package com.project.why.braillelearning.Module;

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
import com.project.why.braillelearning.R;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.INVISIBLE;

/**
 * Created by EH515 on 2018-03-16.
 */

public class PermissionCheckModule {
    public static final int PERMISSION_ALLOWED = 1;
    public static final int PERMISSION_NOT_ALLOWED = 0;
    private Context context;
    private View view;
    private ImageView permissionGuideBackGround, permissionGuideImage;
    private ImageResizeModule imageResizeModule;

    public PermissionCheckModule(Context context, View view){

        this.context = context;
        this.view = view;
        imageResizeModule = new ImageResizeModule(context.getResources());
        initPermissionView();
    }

    private void initPermissionView(){
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


        permissionGuideImage = new ImageView(context);
        FrameLayout.LayoutParams guideLayoutParam = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        );

        guideLayoutParam.width = (int)(Global.DisplayY*0.6) * 2;
        guideLayoutParam.height = (int)(Global.DisplayY*0.6);
        permissionGuideImage.setLayoutParams(guideLayoutParam);
        ((FrameLayout) view.getParent()).addView(permissionGuideImage);
        permissionGuideImage.setVisibility(INVISIBLE);
    }

    public int checkPermission(){
        SharedPreferences pref = context.getSharedPreferences("settingCheck", MODE_PRIVATE);
        String state = pref.getString("TRUE", "FALSE");
        if(state == "TRUE" || state.equals("TRUE")){
            if (PermissionChecker.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED || PermissionChecker.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(Uri.parse("package:" + context.getPackageName()));
                context.startActivity(intent);
                return PERMISSION_NOT_ALLOWED;
            } else
                return PERMISSION_ALLOWED;
        } else {
            startPermissionGuide();
            return PERMISSION_NOT_ALLOWED;
        }
    }

    private void startPermissionGuide(){
        recyclePermissionImage();
        permissionGuideBackGround.setVisibility(View.VISIBLE);
        permissionGuideImage.setImageDrawable(imageResizeModule.getDrawableImage(R.drawable.check_permission, permissionGuideImage.getLayoutParams().width, permissionGuideImage.getLayoutParams().height));
        permissionGuideImage.setVisibility(View.VISIBLE);
    }

    private void stopPermissionGuide(){
        recyclePermissionImage();
    }

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
}
