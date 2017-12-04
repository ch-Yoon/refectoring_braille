package com.project.why.braillelearning.LearningControl;

import android.content.Context;

import com.project.why.braillelearning.EnumConstant.BrailleLearningType;

/**
 * Created by hyuck on 2017-11-01.
 */

/**
 * 손가락 1개를 위한 event 모듈을 return 하는 factory class
 */
public class SingleFIngerFactory {
    private Context context;
    private BrailleLearningType brailleLearningType;

    public SingleFIngerFactory(Context context, BrailleLearningType brailleLearningType){
        this.context = context;
        this.brailleLearningType = brailleLearningType;
    }

    /**
     * brailleLearningType에 따른 손가락 1개 event모듈을 return하는 함수
     * @return 메뉴 선택, 점자 학습, 선생님 모드 손가락 1개 모듈 리턴
     */
    public SingleFingerFunction getSingleFingerMoudle() {
        switch (brailleLearningType) {
            case TEACHER:
                return new TeacherSingleFinger(context);
            default:
                return new BasicSingleFinger(context);
        }
    }
}
