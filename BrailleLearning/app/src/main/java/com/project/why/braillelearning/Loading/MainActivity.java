package com.project.why.braillelearning.Loading;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.kakao.sdk.newtoneapi.SpeechRecognizeListener;
import com.kakao.sdk.newtoneapi.SpeechRecognizerClient;
import com.kakao.sdk.newtoneapi.SpeechRecognizerManager;
import com.kakao.sdk.newtoneapi.impl.util.PermissionUtils;
import com.project.why.braillelearning.Global;
import com.project.why.braillelearning.Module.FullScreenModule;
import com.project.why.braillelearning.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;
import static com.kakao.util.helper.Utility.getPackageInfo;

public class MainActivity extends Activity implements View.OnClickListener, SpeechRecognizeListener {
    private View decorView; // 최상단 BackgroundView
    private int uiOption;

    Button button;
    private SpeechRecognizerClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InitFullScreen();
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        SpeechRecognizerManager.getInstance().initializeLibrary(this);
    }

    public void InitFullScreen(){ // 전체화면 함수
        decorView = getWindow().getDecorView(); // 실제 윈도우의 배경 drawable을 담고있는 decorView
        FullScreenModule fullScreenModule = new FullScreenModule(this);
        uiOption = fullScreenModule.getFullScreenOption(); // decorView의 ui를 변경하기 위한 값
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 액션바 제거
        decorView.setSystemUiVisibility(uiOption); // 전체화면 적용

    }


    public void InitDisplaySize(){  // Display 해상도를 구하기 위한 메소드
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        Global.DisplayX = size.x; // Display의 가로값. Global변수에 저장하여 상시 사용
        Global.DisplayY = size.y; // Display의 세로값. Global변수에 저장하여 상시 사용
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) { // 화면에 포커스가 잡혔을 경우
            decorView.setSystemUiVisibility(uiOption); // 전체화면 적용
            InitDisplaySize(); // 화면 해상도 구하기
//            Intent i = new Intent(MainActivity.this, BrailleLearningLoading.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.fade, R.anim.hold);
//            finish();
            Log.d("onWindowFocus","start");
            Log.d("keyHash: ",getKeyHash(this));
        }
    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.w(TAG, "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    @Override
    public void onReady() {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int errorCode, String errorMsg) {
        client = null;
        Log.d("NewTone errorCode : ", errorCode+"");
    }

    @Override
    public void onPartialResult(String partialResult) {

    }

    @Override
    public void onResults(Bundle results) {
        final StringBuilder builder = new StringBuilder();
        Log.i("SpeechSampleActivity", "onResults");

        ArrayList<String> texts = results.getStringArrayList(SpeechRecognizerClient.KEY_RECOGNITION_RESULTS);
    }

    @Override
    public void onAudioLevel(float audioLevel) {

    }

    @Override
    public void onFinished() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        String serviceType = SpeechRecognizerClient.SERVICE_TYPE_WEB;


        Log.i("SpeechSampleActivity", "serviceType : " + serviceType);


        // 음성인식 버튼 listener
        if (id == R.id.button) {
            if(PermissionUtils.checkAudioRecordPermission(this)) {

                SpeechRecognizerClient.Builder builder = new SpeechRecognizerClient.Builder().

                        setServiceType(serviceType);

                client = builder.build();

                client.setSpeechRecognizeListener(this);
                client.startRecording(true);


            }
        }
    }

}
