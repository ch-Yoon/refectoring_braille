package com.project.why.transalationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {
    String chosung[] = {"ㄱ","ㄲ","ㄴ","ㄷ","ㄸ","ㄹ","ㅁ","ㅂ","ㅃ","ㅅ","ㅆ","ㅇ","ㅈ","ㅉ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};
    String moum[] ={"아","애","야","얘","어","에","여","예","오","와","왜","외","요","우","워","웨","위","유","으","의","이"};
    String jongsung[] ={"","ㄱ","ㄲ","ㄳ","ㄴ","ㄵ","ㄶ","ㄷ","ㄹ","ㄺ","ㄻ","ㄼ","ㄽ","ㄾ","ㄿ","ㅀ","ㅁ","ㅂ","ㅄ","ㅅ","ㅆ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅎ"};

    int Text_number = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textvew = (TextView) findViewById(R.id.textview);

        String text = "고";
        int finalcode = get_final(text);                                   //종성의 배열 인덱스를 산출
        int vowelcode = get_vowel(text, finalcode);                      //모음의 배열 인덱스를 산출
        int initcode = get_initial(text, vowelcode, finalcode);          //초성의 배열 인덱스를 산출
        textvew.setText(initcode+" "+vowelcode+" "+finalcode+" "+get_vowel_final(text, finalcode, vowelcode));
    }


    //초음 + 모음을 얻어오는 함수
    public String get_initial_vowel(String text, int final_code){
        text=Integer.toHexString((text.charAt(Text_number) & 0xFFFF) - final_code);
        text=UNI_change_KOR(text);
        return text;
    }


    //ㅇ + 모음 + 종성을 구하는 함수
    public String get_vowel_final(String text, int finalcode, int vowelcode){
        text = Integer.toHexString(0xAC00+((11)+vowelcode)*28+finalcode);  //유니코드 변환 (초성)
        text=UNI_change_KOR(text);

        return text;
    }

    //유니코드로 변환하는 함수
    public String getUnicode(String text){
        if(text.equals("")==false)
            text = Integer.toHexString(text.charAt(Text_number) & 0xFFFF);
        return text;
    }

    //초성을 얻어오는 함수
    public int get_initial(String text, int vowel_code, int final_code){
        int code=0;
        text = Integer.toHexString(((((text.charAt(Text_number) & 0xFFFF)- final_code - 0xAC00) / 28) - vowel_code) / 21);  //유니코드 변환 (초성)
        code = Numberchange(text);

        return code;
    }


    //모음을 얻어오는 함수
    public int get_vowel(String text, int final_code){
        int code=0;
        text = Integer.toHexString((((text.charAt(Text_number) & 0xFFFF)- final_code - 0xAC00) / 28) % 21);     //유니코드로 변환해줌 (모음)
        code = Numberchange(text);

        return code;
    }


    //종성을 얻어오는 함수
    public int get_final(String text){
        int code=0;
        text = Integer.toHexString(((text.charAt(Text_number) & 0xFFFF)-0xAC00) % 28);   //유니코드로 변환해줌 (종성)
        code = Numberchange(text);

        return code;
    }

    //유니코드를 한글로 바꿔주는 함수
    public String UNI_change_KOR(String unicode){
        String Kor="";
        StringTokenizer str1 = new StringTokenizer(unicode,"\\u");
        while(str1.hasMoreTokens()){
            String str2 = str1.nextToken();
            int j = Integer.parseInt(str2,16);
            Kor += (char)j;
        }
        return Kor;
    }


    //16진수를 10진수로 바꾸어주는 함수
    public int Numberchange(String number){
        int return_result=0;
        return_result = Integer.parseInt(number,16);

        return return_result;
    }

}
