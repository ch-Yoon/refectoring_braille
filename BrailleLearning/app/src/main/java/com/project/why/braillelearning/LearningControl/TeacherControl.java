package com.project.why.braillelearning.LearningControl;

import android.content.Context;
import android.os.AsyncTask;
import com.project.why.braillelearning.EnumConstant.BrailleLearningType;
import com.project.why.braillelearning.EnumConstant.Database;
import com.project.why.braillelearning.EnumConstant.Json;
import com.project.why.braillelearning.EnumConstant.TouchLock;
import com.project.why.braillelearning.LearningModel.BrailleData;
import com.project.why.braillelearning.LearningModel.Dot;
import com.project.why.braillelearning.LearningModel.JsonBrailleData;
import com.project.why.braillelearning.MediaPlayer.MediaPlayerStopCallbackListener;
import com.project.why.braillelearning.Permission.PermissionCheckCallbackListener;
import com.project.why.braillelearning.Permission.PermissionCheckModule;
import com.project.why.braillelearning.R;
import com.project.why.braillelearning.SpeechRecognition.SpeechRecognitionListener;
import com.project.why.braillelearning.SpeechRecognition.SpeechRecognitionModule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by hyuck on 2017-11-22.
 */

public class TeacherControl extends BasicControl implements SpeechRecognitionListener, MediaPlayerStopCallbackListener {
    private SpeechRecognitionModule speechRecognitionModule;
    private boolean sendCheck = false;
    private String teacherServerURL = "http://13.125.23.151/teacher.php";
    private String room = "0";

    TeacherControl(Context context, Json jsonFileName, Database databaseFileName, BrailleLearningType brailleLearningType, ControlListener controlListener){
        super(context, jsonFileName, databaseFileName, brailleLearningType, controlListener);
        speechRecognitionModule = new SpeechRecognitionModule(context, this);
    }


    /**
     * 일시정지 되었을 때 함수
     */
    @Override
    public void onPause() {
        customTouchConnectListener.setTouchLock(TouchLock.UNLOCK);
        mediaSoundManager.stop();
        speechRecognitionModule.pause();
        pauseTouchEvent();
    }


    /**
     * 음성은 재생하지 않고, draw만 새로고침하는 함수
     */
    public void nodifyViewObserver(){
        if(data != null)
            viewObservers.nodifyBraille(data.getLetterName(), data.getBrailleMatrix());
    }


    /**
     * 손가락 1개 재정의 함수
     * 점자릉 입력하기 위해 내부 구현
     * @param fingerCoordinate : 좌표값
     */
    @Override
    public void onOneFingerMoveFunction(FingerCoordinate fingerCoordinate) {
        if (data != null) {
            Dot tempDot[][] = oneFingerFunction.oneFingerFunction(data.getBrailleMatrix(), fingerCoordinate);
            if (tempDot != null) {
                data.setBrailleMatrix(tempDot);
                data.refreshData();
                nodifyViewObserver();
            }
        }
    }


    /**
     * 음성인식 특수기능 함수
     * 현재화면에 점자가 입력되어 있지 않다면, 점자 입력 후 다시시도하라는 멘트 출력
     */
    @Override
    public void onSpeechRecognition() {
        int checkPermissionResult = permissionCheckModule.checkPermission();
        if(checkPermissionResult == PermissionCheckModule.PERMISSION_NOT_ALLOWED){
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
        } else {
            if (checkInputBraille()) {
                customTouchConnectListener.setTouchLock(TouchLock.SPEECH_RECOGNITION_LOCK);
                speechRecognitionModule.start();
            } else
                mediaSoundManager.start(R.raw.teacher_no_input);
        }
    }


    /**
     * 음성인식에 대한 callback method
     * 음성인식에 대한 ArrayList가 전달됨
     */
    @Override
    public void speechRecogntionResult(ArrayList<String> text) {
        if(text != null) {
            try{
                if(!sendCheck) {
                    sendCheck = true;
                    String letterName = text.get(0);
                    data.setLetterName(letterName);
                    data.refreshData();
                    nodifyViewObserver();
                    speechRecognitionModule.start(data.getRawId());
                } else {
                    sendCheck = false;
                    boolean check = checkAnswer(text);
                    if(check) {
                        sendServer();
                    } else {
                        data.setLetterName("");
                        data.refreshData();
                        nodifyViewObserver();
                        mediaSoundManager.start(R.raw.send_cancel);
                        customTouchConnectListener.setTouchLock(TouchLock.UNLOCK);

                    }

                    if(checkPage())
                        addJsonBrailleData(context, Json.TEACHER);
                }
            } catch(Exception e){
                sendCheck = false;
                data.setLetterName("");
                data.refreshData();
                nodifyViewObserver();
                mediaSoundManager.start(R.raw.send_cancel);
                customTouchConnectListener.setTouchLock(TouchLock.UNLOCK);

            }
        } else {
            ((BrailleLearningActivity)context).runOnUiThread(new Runnable() { // onError가 호출되었을 경우
                @Override
                public void run() {
                    data.setLetterName("");
                    data.refreshData();
                    nodifyViewObserver();
                    mediaSoundManager.start(R.raw.speechrecognition_fail);
                    mediaSoundManager.start(R.raw.retry);
                }
            });
            customTouchConnectListener.setTouchLock(TouchLock.UNLOCK);
        }
    }


    /**
     * 새로운 페이지를 만들기 위한 함수
     * 현재 화면이 아무런 점자가 입력되지 않았다면 새로운 페이지를 생성하지 않음.
     * 현재 화면에 점자가 입력되었다면 새로운 페이지를 생성
     * @return true(새로운 페이지 생성), false(새로운 페이지 생성 x)
     */
    private boolean checkPage(){
        if(pageNumber != brailleDataArrayList.size()-1)
            return false;
        else
            return checkInputBraille();
    }


    /**
     * 현재 화면에 점자가 입력되어있는지 확인하는 함수
     * @return true(입력되어 있음), false(입력되어 있지 않음)
     */
    private boolean checkInputBraille(){
        Dot[][] brailleMatrix = data.getBrailleMatrix();
        int col = brailleMatrix.length;
        int row = brailleMatrix[0].length;

        for (int i = 0; i < col; i++) {
            for (int j = 0; j < row; j++) {
                boolean target = brailleMatrix[i][j].getTarget();
                if (target == true)
                    return true;
            }
        }

        return false;
    }


    /**
     * 점자가 입력되지 않은 깨끗한 페이지(점자 칠판)을 만들기 위한 함수
     * @param context
     * @param jsonFileName
     */
    private void addJsonBrailleData(Context context, Json jsonFileName) {
        JsonBrailleData jsonBrailleData = new JsonBrailleData(context, jsonFileName, BrailleLearningType.TEACHER);
        ArrayList<BrailleData> newArray = jsonBrailleData.getBrailleDataArray();
        brailleDataArrayList.addAll(newArray);
    }


    /**
     * 점자 정보를 server로 보내기 전 확인하는 함수
     * 예, 또는 아니오로 말한 음성인식 결과를 통해 송신여부 결정
     * @param text : 음성인식 결과 arraylist
     * @return true(송신 동의), false(송신 비동의)
     */
    private boolean checkAnswer(ArrayList<String> text){
        String yes[] = {"예","네"};
        String no[] = {"아니오","아니요","아니"};

        for(int i=0 ; i<text.size() ; i++){
            String targetText = text.get(i);

            for(int j=0 ; j<yes.length ; j++){
                if(targetText == yes[j] || targetText.equals(yes[j])) {
                    return true;
                }
            }

            for(int j=0 ; j<no.length ; j++){
                if(targetText == no[j] || targetText.equals(no[j])) {
                    return false;
                }
            }
        }

        return false;
    }


    /**
     * server로 점자 데이터를 송신하기 위한 AsyncTask 생성 함수
     */
    private void sendServer(){
        SendServerTask task = new SendServerTask();
        task.execute(data.getLetterName(), data.getStrBrailleMatrix(), data.getRawId());
    }


    /**
     * server로 점자 데이터를 송신하기 위한 AsyncTask
     * 선생님이 말한 방번호와 입력한 점자 데이터를 server로 송신
     */
    class SendServerTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try{
                String lettername = params[0];
                String brailleText = params[1];
                String rawId = params[2];

                String link = teacherServerURL;
                String inputData  = URLEncoder.encode("letterName", "UTF-8") + "=" + URLEncoder.encode(lettername, "UTF-8");
                inputData += "&" + URLEncoder.encode("brailleText", "UTF-8") + "=" + URLEncoder.encode(brailleText, "UTF-8");
                inputData += "&" + URLEncoder.encode("rawID", "UTF-8") + "=" + URLEncoder.encode(rawId, "UTF-8");
                inputData += "&" + URLEncoder.encode("room", "UTF-8") + "=" + URLEncoder.encode(room, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(inputData);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = "";

                while((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return sb.toString();
            }
            catch(Exception e){
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result.equals("error") || result == "error")
                mediaSoundManager.start(R.raw.sendfail);
            else {
                try {
                    JSONObject jsonObj = new JSONObject(result);
                    JSONArray jsonArray = jsonObj.getJSONArray("result");   //  mysql에서 불러온값을 저장.

                    for(int i=0; i<jsonArray.length(); i++){
                        JSONObject c = jsonArray.getJSONObject(i);
                        String str = c.getString("id");    // 저장코드들
                        String text = "";
                        if(0 < str.length()) {
                            if(room == "0" || room.equals("0"))
                                text = str + "번! " + str + ",send_ok";
                            else
                                text = "resend_info," + str + ". 입니다.";
                            mediaSoundManager.start(text);
                            room = str;
                            break;
                        }
                    }

                    mediaSoundManager.setMediaPlayerStopCallbackListener(TeacherControl.this);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * mediaSoundPlayer stop callback listener
     */
    @Override
    public void mediaPlayerStop() {
        customTouchConnectListener.setTouchLock(TouchLock.UNLOCK);
    }


    /**
     * 학습화면 종료 함수
     */
    @Override
    public void exit(){
        speechRecognitionModule.stop();
        controlListener.exit();
    }


    /**
     * 권한 설정에 동의하였을 경우
     */
    @Override
    public void onPermissionUseAgree() {
        permissionCheckModule.permissionSettingMove();
    }


    /**
     * 권한 설정에 동의하지 않았을 경우
     */
    @Override
    public void onPermissionUseDisagree() {
        permissionCheckModule.cancelPermissionGuide();
        refreshData();
    }

}


