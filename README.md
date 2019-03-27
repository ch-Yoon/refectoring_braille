<img src="https://user-images.githubusercontent.com/20294749/54074178-b8a69100-42d2-11e9-90c6-81e7811dea1d.png" width=200>

# 시각장애인을 위한 점자 학습 어플리케이션
'봄' 점자 학습 어플리케이션은 화면을 볼 수 없더라도 진동, 남성과 여성의 음성, 다양한 효과음으로 점자 학습을 진행할 수 있는 점자 학습 어플리케이션 입니다. 

기존의 점자 학습 어플리케이션들은 눈으로 화면을 보며 점자를 학습해야 합니다. 시각장애로 인해 점자를 배워야 하는 상황이지만, 아이러니하게도 시각에 의존하여 점자를 배워야 합니다.

그렇기에, ‘봄’은 시각, 청각, 촉각을 이용하여 점자 학습이 가능하도록 개발되었습니다. 점자의 형상과 돌출 위치, 점자의 배열정보, 글자의 정보 등 점자 학습에 필요한 정보들을 전달하기 위해 세심하게 표현하였습니다

## 개발자 이야기
사실, 시각장애인을 위한 '봄' 점자 학습 어플리케이션은, 품질 좋은 코드로 작성되지 않았습니다. 코드를 보기 위해 들여다보면, 도대체 무슨 코드가 이리도 복잡하게 얽혀있는지, 함수명은 왜 이런지, 이런 중복 코드들은 무엇인지, 한숨이 푹 나오는 코드입니다. 어쩌면, 정리도 잘 되지 않은 package를 보며 자세히 들여다 볼 엄두도 나지 않을 수 있습니다. **이 프로젝트의 코드를 보면 시력이 떨어지실 수도 있는 점 미리 말씀드립니다.**

하지만 '봄' 점자 학습 어플리케이션은, '개발자'가 되고 싶다는 마음을 불러 일으켜 준, 우리 팀의 대학 생활을 대표하는 프로젝트입니다. 우리가 생각했을 때 필요한 기능들이 아닌, 실제 사용할 시각장애인분들을 찾아가 이야기를 직접 들으며 개발했습니다. 오랜 시간동안 의사소통을 해야했고, 어떻게 기능 구현을 해야 할 지 몰라 많은 공부를 했습니다. 많이 부족한 프로젝트이지만, 코드의 품질을 떠나 참 애틋한 프로젝트입니다.

## 수상 내역
### 교외
- 2017 서울국제발명전시회 금상 : 2018. 12. 02
- 말레이시아 국제대회 금상 : 2017. 05. 12
- 한이음 공모전 입선 : 2016. 12. 07
### 교내
- 산학협동 산업기술대전 한국산업기술시험원장상 : 2017. 10. 18
- 제 2회 P2P & 캡스톤 경진대회 최우수상 : 2017. 03. 15

## 시연동영상

[![Watch the video](https://user-images.githubusercontent.com/20294749/52535521-d8f63500-2d92-11e9-80d8-1b67dbcbd304.png)](https://www.youtube.com/watch?v=-YME1Dlb4iU&t=45s)

# 내용
## 점자 정보 구성
![image](https://user-images.githubusercontent.com/20294749/55057610-0120c580-50ad-11e9-9a4b-d6920defb96f.png)
- 점자 중 돌출 점은 1로, 비 돌출 점은 0으로 설정
- 가령, 초성 기역의 경우 {{0, 1}, {0, 0}, {0, 0}}으로 표현
- 점자 정보는 "010000" 문자열로 Json file에 구성하여 프로젝트에 탑재

![image](https://user-images.githubusercontent.com/20294749/55058465-5e1d7b00-50af-11e9-8ca7-e446c656b4ef.png)
- Json File에 담긴 정보와 display의 좌표를 계산하여 각 점의 속성 설정

## 제스쳐 기능
![image](https://user-images.githubusercontent.com/20294749/55058959-f5cf9900-50b0-11e9-99b7-9d4fa942136e.png)
- Single Tab, Double Tab, Left Drag, Right Drag, Up Drag, Down Drag 구현

 
## 시각장애인과 이야기하여 도출된 요구사항
<img src="https://user-images.githubusercontent.com/20294749/52536131-c8958880-2d99-11e9-88db-44fb560c22a1.png" width=500>

### 도출된 요구사항 구현
![image](https://user-images.githubusercontent.com/20294749/55059623-bf931900-50b2-11e9-9829-79aa729b5101.png)
- Touch Event의 좌표 기반으로 점자 좌표 탐색 후 이벤트 처리 구현
  
## 전체 메뉴 구조
<img src="https://user-images.githubusercontent.com/20294749/54074204-fc00ff80-42d2-11e9-8423-3f93c14d2c96.png" width=500>

## 관련 URL
- [Goole Play Store](https://play.google.com/store/apps/details?id=com.project.why.braillelearning)

- [UI/UX 도출 과정 카드 뉴스](https://m.facebook.com/story.php?story_fbid=969402966558900&id=962857480546782)

- [인터뷰 영상](https://www.facebook.com/BlackBee.BOM/videos/1003795013119695/?__tn__=kC-R&eid=ARAZzdSx42tnVFjk7WeJnrIz4diiCZG8TTC25UyOlHrhNTWz1IQ0y6OtTV4UD0J_X1PTVlAS3uGB_i4B&hc_ref=ARTyrZqiU-0Pxaj6tV5dM0LTfD8HmOiVkrtIFa2_E-SZ4kQmrjEBFQTJV2Qu353s7zE&fref=nf&__xts__[0]=68.ARCquecUGC1RSWhZQlmLHL_fT1oqKbLJX3MXKKaWupUmqqvD38BgCFHorZBcP1Z80y1yBlPWO0xiec_d4h1qBu3De2wZjd-H9eVuUBJB42T14efskIu-rW_xkY5GLbg1XamFgICFbhwfZZxW8-YnHOurPew7mfq9AOyRXVoWPCU_GEUv2Bn5bvdRqqrw4kb7vLG18mIsDX-lUB-Tm0MYgHHsanrL4UjhvKK6CTDFS-08lVJhsOWS3zZ4L6QSuT0Ir__MPQieNYNwunV4elMwk5SY0s7y7Qeu4JnW-KmcOuhAITKV-uv8lBLYC36kU3OU4LRi4mdYNQO3nOuIK_NeI0j7Qg)

- [봄 점자 학습 페이스북 페이지](https://www.facebook.com/BlackBee.BOM)

- [라디오에서 소개된 '봄'](http://m.ablenews.co.kr/news/newscontent.aspx?categorycode=0048&newscode=004820180810114039868693&fbclid=IwAR3wQnA_qrGKtOoOhN72QNnzDuvOZMewX77VqWRHDNnkU7kWCz3ympS8eG0)


