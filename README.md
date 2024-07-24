# 광주청사교회 어플입니다.
광주청사교회를 소개하고 관련 이미지, 설교 영상 시청이 가능합니다.
1인 프로젝트라 많이 부족하지만 관련 자료들을 찾아 배포까지 하였습니다.
API 서버는 추후 Java Spring으로 업데이트할 예정입니다.

> 링크<br>
google play store 👉 https://play.google.com/store/apps/details?id=com.gj.gjchungsa&hl=ko<br>
homepage  👉 http://chungsa.or.kr

<br>

## 이용사진
<img src="https://github.com/user-attachments/assets/7c844a7a-5f32-4593-bd83-deab14b8ae9f" width="200">
<img src="https://github.com/user-attachments/assets/8ceaf8e3-bc93-40d5-af7c-77467b79a54e" width="200">
<img src="https://github.com/user-attachments/assets/e941b0c6-93a7-4d8e-957f-c0f6a83c3e7c" width="200">


<br>

## 아키 텍쳐
![gjchungsa drawio](https://github.com/user-attachments/assets/701cdd53-56c0-4d98-9965-8eb76a7453c5)

- 카카오 로그인으로 회원정보를 얻어와 관리하도록 구성하였습니다.
- API 서버를 운영해 관리하였습니다.

<br>

## 사용기술
 - Kakao Login
   - Kakao oauth2를 사용하여 회원 정보를 가져옴
 - Android Studio
   - Android Studio, Java, Gradle 사용하여 개발
 - API 서버 운영
   - Aws Ec2를 사용하여 API 서버 운영 (php로 구현하여 추후 java Spring Restful API로 변경 예정)
   - Aws rds를 사용하여 Maria DB 운영
 - Kakao Map
   - 교회 캠프 일정을 보여주는 기능구현
   - Kakao Map에 마크를 활용하여 맛집, 숙소, 먹거리 정보등을 확인 가능











