

<div align="center">

<!-- logo -->

# NewsFeed


[<img src="https://img.shields.io/badge/프로젝트 기간-2025.02.10~2025.02.20-blue?style=flat&logo=&logoColor=white" />]()

</div> 

## 📝 소개
인스타를 모델로 한 뉴스피드 프로그램입니다.



## 🗂️ APIs
작성한 API는 아래에서 확인할 수 있습니다.

👉🏻 [API 바로보기](https://teamsparta.notion.site/18-19a2dc3ef51480caa081fd3986e05f14)
👉🏻 [Postman API](https://documenter.getpostman.com/view/41698254/2sAYdZvZtY)

<br />

## ⚙ 기술 스택
### Back-end
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Java.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringBoot.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/SpringDataJPA.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Mysql.png?raw=true" width="80">
</div>

### Infra
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/AWSEC2.png?raw=true" width="80">
</div>

### Tools
<div>
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Github.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Notion.png?raw=true" width="80">
<img src="https://github.com/yewon-Noh/readme-template/blob/main/skills/Postman.png?raw=true" width="80">
</div>

<br />
 
## 🤔 기술적 이슈와 해결 과정

<details>
<summary><strong>댓글 좋아요 API 테스트 결과 400 Bad_Request</strong></summary>

### 💥 문제
댓글의 좋아요 API 테스트를 위해 댓글 작성 후, 댓글 작성자와 다른 사용자 토큰을 받아 좋아요 API 테스트를 실행한 결과, 콘솔 창에는 아무 에러도 없었지만 응답은 계속 `400 Bad Request`를 반환함.

#### 응답 코드 예시
```json
{
    "timestamp": "2025-02-19T11:45:54.129+00:00",
    "status": 400,
    "error": "Bad Request",
    "path": "/5/likes"
}
```
<br />

### 💥 원인
1. `comment` 엔티티에서 댓글의 좋아요 수를 저장하는 `likes_cnt` 필드가 `null`로 저장됨.
    
    **기존 코드:**
    ```java
    @Column(name = "likes_cnt")
    private Long likeCnt;
    ```
   
2. 본인 댓글에 좋아요를 누르면 에러가 발생하도록 구현하려 했으나, 잘못된 부정 연산자로 인해 오히려 남의 댓글에 좋아요를 하면 에러가 발생하는 문제 발생.
    
    **기존 코드:**
    ```java
    // 사용자의 댓글인지 검증
    if (!comment.getUser().getId().equals(userId)) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's your feed");
    }
    ```
<br />

### 💡 해결
1. `likes_cnt` 값을 `0`으로 초기화하여 `null` 문제 해결.
    
    **수정 코드:**
    ```java
    @Column(name = "likes_cnt")
    private Long likeCnt = 0L;
    ```

2. 검증 로직에서 부정 연산자 제거하여 본인 댓글에만 좋아요를 할 수 없도록 수정.
    
    **수정 코드:**
    ```java
    // 사용자의 댓글인지 검증
    if (comment.getUser().getId().equals(userId)) {
        throw new ForbiddenException("본인이 작성한 댓글은 좋아요할 수 없습니다.");
    }
    ```
</details>

<br />

<details>
<summary><strong>api에 넣어야 할 데이터 결정</strong></summary>
</details>



<br />

## 💁‍♂️ 프로젝트 팀원
|                     Backend                     |                   Backend                    |                    Backend                     |                    Backend                     |
|:-----------------------------------------------:|:--------------------------------------------:|:----------------------------------------------:|:----------------------------------------------:|
| ![](https://github.com/wonchaebin.png?size=120) | ![](https://github.com/kny5579.png?size=120) | ![](https://github.com/Doritos38.png?size=120) | ![](https://github.com/ChungwonJ.png?size=120) |
|       [원채빈](https://github.com/wonchaebin)    |     [김나연](https://github.com/kny5579)      |    [김대정](https://github.com/Doritos38)       |      [정청원](https://github.com/ChungwonJ)      |


## 💬 프로젝트 개발 후기
* 팀장 원채빈
>rebase하면서 제가 최신 dev로 pull을 안해가지고 꼬인 코드때문에 고생하신 튜터님,,감사하고 죄송합니다,,bb 깃으로 협업은 처음이었는데 개인 프로젝트 할 때 보다 깃 활용 능력이 많이 상승한 거 같아 좋은 기회였다고 생각합니다 마지막으로 사다리 타기 팀장이고 I라 답답했을텐데 잘 따라와주신 팀원 분들께 감사드립니다! 팀원분들 덕분에 많이 배우고 갑니다! 앞으로도 다들 파이팅입니닷

* 팀원 김나연
> 엄청난 conflict 및 코드 꼬임으로 rebase, merge보다 abort를 더 많이 한 것 같아서  git에 대한 공부가 필요하다고 뼈저리게 느꼈습니다. 그래도 좋은 팀원들을 만나서 자주 소통하고 진정한 협업에 한걸음 다가간 것 같아서 뿌듯합니다.

* 팀원 김대정
> 김대정 : 프로젝트 작업보다 git이 더 어려웠던 것 같습니다. 리베이스 할 때마다 혹시 잘못된 데이터를 적용한 건 아닐까 긴장하면 했습니다. 이번 경험으로 git에 대한 숙련도가 아직 너무 부족하다는 것을 알았고 혼자 프로젝트 할 때도 브랜치를 나눠 개발하는 습관을 들여 연습하겠습니다.

* 팀원 정청원
>  JWT,아마존AWS 등등 머릿속에 있었던 것들을 구현해 내기가 참 힘들었네요. 아직 가야 할 길이 많은거 같네요 git에 대해서도 생각을 좀 해야할거 같고 암호화 암복화 등등 프론트에서의 생각과 조금 바라보는 방향성을 다르게 봐야한다는 것을 깨달은 프로젝트 였던거 같습니다.
