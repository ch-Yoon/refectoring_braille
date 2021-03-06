package com.project.why.braillelearning.Menu;

import com.project.why.braillelearning.EnumConstant.Menu;
import com.project.why.braillelearning.R;
import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by User on 2017-09-02.
 */

/**
 * 메뉴 정보를 유지하는 menuTreeManager class
 * 하위메뉴를 등록해가는 방식으로 구현 됨
 */
public class MenuTreeManager {
    private TreeNode rootNode; // 최상단 rootnode

    MenuTreeManager(){
        rootNode = new TreeNode();
        setImageTree();
    }

    /**
     * menu들을 tree구조로 등록하는 함수
     */
    private void setImageTree(){
        // 점자 학습 메뉴 트리 등록 (대 메뉴) : 사용설명서, 기초과정, 숙련과정, 점자번역, 퀴즈, 나만의 단어장, 선생님과의 대화
        TreeNode tutorial = rootNode.addChildTreeNode(Menu.TUTORIAL, R.drawable.tutorial, R.raw.tutorial);
        TreeNode basicPractice = rootNode.addChildTreeNode(Menu.BASIC, R.drawable.basic_practice, R.raw.basic_practice);
        TreeNode masterPractice = rootNode.addChildTreeNode(Menu.MASTER, R.drawable.master_practice, R.raw.master_practice);
        TreeNode brailleTranslation = rootNode.addChildTreeNode(Menu.TRANSLATION, R.drawable.brailletranslation, R.raw.braille_translation);
        TreeNode quiz = rootNode.addChildTreeNode(Menu.QUIZ, R.drawable.quiz, R.raw.quiz);
        TreeNode mynote = rootNode.addChildTreeNode(Menu.MYNOTE, R.drawable.mynote, R.raw.mynote);
//        TreeNode Communication = RootNode.addChildTreeNode(Menu.COMMUNICATION, R.drawable.comunication, R.raw.comunication);

        // 기초과정 하위메뉴 트리 등록 (중 메뉴) : 초성연습, 모음연습, 종성연습, 숫자연습, 알파벳연습, 문장부호연습, 약자 및 약어연습
        TreeNode InitialPractice = basicPractice.addChildTreeNode(Menu.INITIAL_BASIC, R.drawable.initial_practice, R.raw.initial_practice);
        TreeNode VowelPractice = basicPractice.addChildTreeNode(Menu.VOWEL_BASIC, R.drawable.vowel_practice, R.raw.vowel_practice);
        TreeNode FinalPractice = basicPractice.addChildTreeNode(Menu.FINAL_BASIC, R.drawable.final_practice, R.raw.final_practice);
        TreeNode NumberPractice = basicPractice.addChildTreeNode(Menu.NUMBER_BASIC, R.drawable.number_practice, R.raw.number_practice);
        TreeNode AlphabetPractice = basicPractice.addChildTreeNode(Menu.ALPHABET_BASIC, R.drawable.alphabet_practice, R.raw.alphabet_practice);
        TreeNode SentencePractice = basicPractice.addChildTreeNode(Menu.SENTENCE_BASIC, R.drawable.sentence_practice, R.raw.sentence_practice);
        TreeNode abbreviationPractice = basicPractice.addChildTreeNode(Menu.ABBREVIATION_BASIC, R.drawable.promise_practice, R.raw.promise_practice);

        // 숙련과정 하위메뉴 트리 등록 (중 메뉴) : 글자연습, 단어연습
        TreeNode LetterPractice = masterPractice.addChildTreeNode(Menu.LETTER_MASTER, R.drawable.letter_practice, R.raw.letter_practice);
        TreeNode WordPractice = masterPractice.addChildTreeNode(Menu.WORD_MASTER, R.drawable.word_practice, R.raw.word_practice);

        // 퀴즈 하위메뉴 트리 등록 (중 메뉴) : 초성퀴즈, 모음퀴즈, 종성퀴즈, 숫자퀴즈, 알파벳퀴즈, 문장부호퀴즈, 약자 및 약어퀴즈, 글자퀴즈, 단어퀴즈
        TreeNode InitialQuiz = quiz.addChildTreeNode(Menu.INITIAL_QUIZ, R.drawable.initial_quiz, R.raw.initial_quiz);
        TreeNode VowelQuiz = quiz.addChildTreeNode(Menu.VOWEL_QUIZ, R.drawable.vowel_quiz, R.raw.vowel_quiz);
        TreeNode FinalQuiz = quiz.addChildTreeNode(Menu.FINAL_QUIZ, R.drawable.final_quiz, R.raw.final_quiz);
        TreeNode NumberQuiz = quiz.addChildTreeNode(Menu.NUMBER_QUIZ, R.drawable.number_quiz, R.raw.number_quiz);
        TreeNode AlphabetQuiz = quiz.addChildTreeNode(Menu.ALPHABET_QUIZ, R.drawable.alphabet_quiz, R.raw.alphabet_quiz);
        TreeNode SentenceQuiz = quiz.addChildTreeNode(Menu.SENTENCE_QUIZ, R.drawable.sentence_quiz, R.raw.sentence_quiz);
        TreeNode abbreviationQuiz = quiz.addChildTreeNode(Menu.ABBREVIATION_QUIZ, R.drawable.promise_quiz, R.raw.promise_quiz);
        TreeNode LetterQuiz = quiz.addChildTreeNode(Menu.LETTER_QUIZ, R.drawable.letter_quiz, R.raw.letter_quiz);
        TreeNode WordQuiz = quiz.addChildTreeNode(Menu.WORD_QUIZ, R.drawable.word_quiz, R.raw.word_quiz);

        // 나만의 단어장 하위메뉴 트리 등록(중 메뉴) : 기초단어장, 숙련단어장, 선생님의단어장
        TreeNode MynoteBasic = mynote.addChildTreeNode(Menu.BASIC_MYNOTE, R.drawable.mynote_basic, R.raw.mynote_basic);
        TreeNode MynoteMaster = mynote.addChildTreeNode(Menu.MASTER_MYNOTE, R.drawable.mynote_master, R.raw.mynote_master);
 //       TreeNode MynoteCommunication = Mynote.addChildTreeNode(Menu.COMMUNICATION_MYNOTE, R.drawable.mynote_communication, R.raw.mynote_communication);

        // 선생님과의 대화 하위메뉴 트리 등록(중 메뉴) : 선생님 모드, 학생 모드
 //       TreeNode CommunicationTeacher = Communication.addChildTreeNode(Menu.TEACHER_COMMUNICATION, R.drawable.teacher_mode, R.raw.teacher_mode);
 //       TreeNode CommunicationStudent = Communication.addChildTreeNode(Menu.STUDENT_COMMUNICATION, R.drawable.student_mode, R.raw.student_mode);
    }

    /**
     * 현재 내가 위치하고 있는 메뉴 리스트의 길이를 구하는 함수
     * RootMenuItem의 SubMenu가 대 메뉴
     */
    public int getMenuListSize(Deque<Integer> deque){
        Deque<Integer> MenuAddressDeque = dequeCopy(deque);
        MenuAddressDeque.removeLast();
        TreeNode ParentNode = getMenuTreeNode(MenuAddressDeque);

        return ParentNode.getChildTreeNodeListSize();
    }

    public Menu getMenuName(Deque<Integer> deque){
        Deque<Integer> MenuAddressDeque = dequeCopy(deque);
        TreeNode targetNode = getMenuTreeNode(MenuAddressDeque);

        return targetNode.getTreeName();
    }

    /**
     * Deque 안에 있는 MenuIndex 정보를 이용하여 RootNode부터 탐색 후 해당 TreeNode 반환하는 함수
     * @param deque
     * @return
     */
    public TreeNode getMenuTreeNode(Deque<Integer> deque){
        Deque<Integer> MenuAddressDeque = dequeCopy(deque);

        TreeNode TargetNode = rootNode;
        while(!MenuAddressDeque.isEmpty()){
            TargetNode = TargetNode.getChildTreeNode(MenuAddressDeque.pollFirst());
        }

        return TargetNode;
    }

    /**
     *Deque copy 함수
     */
    public Deque<Integer> dequeCopy(Deque<Integer> Deque){
        Deque<Integer> CopyDeque = new LinkedList<>();
        CopyDeque.addAll(Deque);

        return CopyDeque;
    }
}
