package com.project.why.braillelearning.Menu;

import com.project.why.braillelearning.R;

import java.util.Deque;
import java.util.LinkedList;

/**
 * Created by User on 2017-09-02.
 */

public class MenuTreeManager {
    TreeNode RootNode;


    MenuTreeManager(){
        RootNode = new TreeNode();
        setImageTree();
    }

    public void setImageTree(){
        // 점자 학습 메뉴 트리 등록 (대 메뉴) : 사용설명서, 기초과정, 숙련과정, 점자번역, 퀴즈, 나만의 단어장, 선생님과의 대화
        TreeNode Tutorial = RootNode.addChildTreeNode(R.drawable.tutorial, R.raw.tutorial);
        TreeNode BasicPractice = RootNode.addChildTreeNode(R.drawable.basic_practice, R.raw.basic_practice);
        TreeNode MasterPractice = RootNode.addChildTreeNode(R.drawable.master_practice, R.raw.master_practice);
        TreeNode BrailleTranslation = RootNode.addChildTreeNode(R.drawable.brailletranslation, R.raw.braille_translation);
        TreeNode Quiz = RootNode.addChildTreeNode(R.drawable.quiz, R.raw.quiz);
        TreeNode Mynote = RootNode.addChildTreeNode(R.drawable.mynote, R.raw.mynote);
        TreeNode Communication = RootNode.addChildTreeNode(R.drawable.comunication, R.raw.comunication);

        // 기초과정 하위메뉴 트리 등록 (중 메뉴) : 초성연습, 모음연습, 종성연습, 숫자연습, 알파벳연습, 문장부호연습, 약자 및 약어연습
        TreeNode InitialPractice = BasicPractice.addChildTreeNode(R.drawable.initial_practice, R.raw.initial_practice);
        TreeNode VowelPractice = BasicPractice.addChildTreeNode(R.drawable.vowel_practice, R.raw.vowel_practice);
        TreeNode FinalPractice = BasicPractice.addChildTreeNode(R.drawable.final_practice, R.raw.final_practice);
        TreeNode NumberPractice = BasicPractice.addChildTreeNode(R.drawable.number_practice, R.raw.number_practice);
        TreeNode AlphabetPractice = BasicPractice.addChildTreeNode(R.drawable.alphabet_practice, R.raw.alphabet_practice);
        TreeNode SentencePractice = BasicPractice.addChildTreeNode(R.drawable.sentence_practice, R.raw.sentence_practice);
        TreeNode PromisePractice = BasicPractice.addChildTreeNode(R.drawable.promise_practice, R.raw.promise_practice);

        // 숙련과정 하위메뉴 트리 등록 (중 메뉴) : 글자연습, 단어연습
        TreeNode LetterPractice = MasterPractice.addChildTreeNode(R.drawable.letter_practice, R.raw.letter_practice);
        TreeNode WordPractice = MasterPractice.addChildTreeNode(R.drawable.word_practice, R.raw.word_practice);

        // 퀴즈 하위메뉴 트리 등록 (중 메뉴) : 초성퀴즈, 모음퀴즈, 종성퀴즈, 숫자퀴즈, 알파벳퀴즈, 문장부호퀴즈, 약자 및 약어퀴즈, 글자퀴즈, 단어퀴즈
        TreeNode InitialQuiz = Quiz.addChildTreeNode(R.drawable.initial_quiz, R.raw.initial_quiz);
        TreeNode VowelQuiz = Quiz.addChildTreeNode(R.drawable.vowel_quiz, R.raw.vowel_quiz);
        TreeNode FinalQuiz = Quiz.addChildTreeNode(R.drawable.final_quiz, R.raw.final_quiz);
        TreeNode NumberQuiz = Quiz.addChildTreeNode(R.drawable.number_quiz, R.raw.number_quiz);
        TreeNode AlphabetQuiz = Quiz.addChildTreeNode(R.drawable.alphabet_quiz, R.raw.alphabet_quiz);
        TreeNode SentenceQuiz = Quiz.addChildTreeNode(R.drawable.sentence_quiz, R.raw.sentence_quiz);
        TreeNode PromiseQuiz = Quiz.addChildTreeNode(R.drawable.promise_quiz, R.raw.promise_quiz);
        TreeNode LetterQuiz = Quiz.addChildTreeNode(R.drawable.letter_quiz, R.raw.letter_quiz);
        TreeNode WordQuiz = Quiz.addChildTreeNode(R.drawable.word_quiz, R.raw.word_quiz);

        // 퀴즈 하위메뉴의 하위메뉴 트리 등록 (소 메뉴) : 읽기퀴즈, 쓰기퀴즈
        TreeNode InitialReadingQuiz = InitialQuiz.addChildTreeNode(R.drawable.reading_quiz, R.raw.reading_quiz);
        TreeNode InitialWritingQuiz = InitialQuiz.addChildTreeNode(R.drawable.writing_quiz, R.raw.writing_quiz);
        TreeNode VowelReadingQuiz = VowelQuiz.addChildTreeNode(R.drawable.reading_quiz, R.raw.reading_quiz);
        TreeNode VowelWritingQuiz = VowelQuiz.addChildTreeNode(R.drawable.writing_quiz, R.raw.writing_quiz);
        TreeNode FinalReadingQuiz = FinalQuiz.addChildTreeNode(R.drawable.reading_quiz, R.raw.reading_quiz);
        TreeNode FinalWritingQuiz = FinalQuiz.addChildTreeNode(R.drawable.writing_quiz, R.raw.writing_quiz);
        TreeNode NumberReadingQuiz = NumberQuiz.addChildTreeNode(R.drawable.reading_quiz, R.raw.reading_quiz);
        TreeNode NumberWritingQuiz = NumberQuiz.addChildTreeNode(R.drawable.writing_quiz, R.raw.writing_quiz);
        TreeNode AlphabetReadingQuiz = AlphabetQuiz.addChildTreeNode(R.drawable.reading_quiz, R.raw.reading_quiz);
        TreeNode AlphabetWritingQuiz = AlphabetQuiz.addChildTreeNode(R.drawable.writing_quiz, R.raw.writing_quiz);
        TreeNode SentenceReadingQuiz = SentenceQuiz.addChildTreeNode(R.drawable.reading_quiz, R.raw.reading_quiz);
        TreeNode SentenceWritingQuiz = SentenceQuiz.addChildTreeNode(R.drawable.writing_quiz, R.raw.writing_quiz);
        TreeNode PromiseReadingQuiz = PromiseQuiz.addChildTreeNode(R.drawable.reading_quiz, R.raw.reading_quiz);
        TreeNode PromiseWritingQuiz = PromiseQuiz.addChildTreeNode(R.drawable.writing_quiz, R.raw.writing_quiz);
        TreeNode LetterReadingQuiz = LetterQuiz.addChildTreeNode(R.drawable.reading_quiz, R.raw.reading_quiz);
        TreeNode LetterWritingQuiz = LetterQuiz.addChildTreeNode(R.drawable.writing_quiz, R.raw.writing_quiz);
        TreeNode WordReadingQuiz = WordQuiz.addChildTreeNode(R.drawable.reading_quiz, R.raw.reading_quiz);
        TreeNode WordWritingQuiz = WordQuiz.addChildTreeNode(R.drawable.writing_quiz, R.raw.writing_quiz);

        // 나만의 단어장 하위메뉴 트리 등록(중 메뉴) : 기초단어장, 숙련단어장, 선생님의단어장
        TreeNode MynoteBasic = Mynote.addChildTreeNode(R.drawable.mynote_basic, R.raw.mynote_basic);
        TreeNode MynoteMaster = Mynote.addChildTreeNode(R.drawable.mynote_master, R.raw.mynote_master);
        TreeNode MynoteCommunication = Mynote.addChildTreeNode(R.drawable.mynote_communication, R.raw.mynote_communication);


        // 선생님과의 대화 하위메뉴 트리 등록(중 메뉴) : 선생님 모드, 학생 모드
        TreeNode CommunicationTeacher = Communication.addChildTreeNode(R.drawable.teacher_mode, R.raw.teacher_mode);
        TreeNode CommunicationStudent = Communication.addChildTreeNode(R.drawable.student_mode, R.raw.student_mode);
    }

    public void setTree(TreeNode TargetNode){
        RootNode = TargetNode;
    }

    /*
     * 현재 내가 위치하고 있는 메뉴 리스트의 길이를 구하는 함수
     * RootMenuItem의 SubMenu가 대 메뉴
     */
    public int getMenuListSize(Deque<Integer> deque){
        Deque<Integer> MenuAdreeDeque = DequeCopy(deque);
        MenuAdreeDeque.removeLast();
        TreeNode ParentNode = getMenuTreeNode(MenuAdreeDeque);

        return ParentNode.getChildTreeNodeListSize();
    }

    // Deque 안에 있는 MenuIndex 정보를 이용하여 RootNode부터 탐색 후 해당 TreeNode 반환하는 함수
    public TreeNode getMenuTreeNode(Deque<Integer> deque){
        Deque<Integer> MenuAdressDeque = DequeCopy(deque);

        TreeNode TargetNode = RootNode;
        while(!MenuAdressDeque.isEmpty()){
            TargetNode = TargetNode.getChildTreeNode(MenuAdressDeque.pollFirst());
        }

        return TargetNode;
    }

    //Deque copy 함수
    public Deque<Integer> DequeCopy(Deque<Integer> Deque){
        Deque<Integer> CopyDeque = new LinkedList<>();
        CopyDeque.addAll(Deque);

        return CopyDeque;
    }
}
