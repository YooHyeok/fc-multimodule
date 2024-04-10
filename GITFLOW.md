# GitFlow전략

## 주요 Branch

1. ### `Main(Master)`
   실제 운영 환경에 나가 있는 코드만 갖고 있는 브랜치로 그만큼 조심스럽게 다뤄야하며, 안정적인 코드만 들어있어야 한다.
2. ### `Dev`  
   메인 브랜치를 베이스로 생성한 브랜치로 메인 브랜치의 차이로는 다음 배포에 나갈 내용들을 바로 메인브랜치에 머지할 수 없다.  
   그 대신 Dev브랜치에 다음 배포에 나갈 feature 개발 건들을 Merge하게 되고, 그 모든 작업 내용을 갖고 있는 브랜치이다.  
3. ### `Feature`  
   어떤 Feature를 개발 할 때 브랜치를 새로 따서 그 브랜치의 작업 내역에 대한 커밋을 하고, 모든 커밋 완료 후(Push?)에 다시 Dev브랜치에 Merge하게 된다.  
   이 모든  과정이 다 끝난 다음 운영에 내보내야 할 시점이 다가왔을 때 Dev브랜치를 베이스로 Release 브랜치를 생성하게 된다.  
   Release 브랜치가 필요한 이유는 다음 배포에 나갈 코드를 그 순간에 Snap샷을 뜨는 느낌으로 Dev 브랜치에서 Release 브랜치를 생성하게 된다.  
4. ### `Release`  
   Release 브랜치가 생성된 시점부터는 Dev브랜치에 배포에 나갈 내용들을 작업하지 않고	무조건 Release브랜치에 작업을 추가해야 한다.  
   Release 브랜치로 실제 서버를 배포하고, QA 혹은 개발 테스트를 마무리 지었을 경우 해당 Release 브랜치를 Main 브랜치에 Merge하게 되고  
   Main브랜치를 운영 환경에 배포하게 된다.  
5. ### `Hotfix`  
   의도치 않은 장애 상황이 벌어졌을 때, 1번부터 4번까지 일련의 과정을 진행한다면 시간이 오래걸리기 때문에 메인 브랜치에서 바로 Hotfix브랜치를 생성한다.  
   당장 수정해야되는 최소한의 부분만 수정해서 커밋을 한 다음 해당 커밋 내역을 다시 메인 브랜치에 Merge한 후 메인브랜치로 운영에 배포하게 된다.  

## 실습 시나리오 리스트
- Release Branch 생성 후 추가 작업이 필요해질 경우
- Release Branch 생성 후 추가 작업이 없는 경우
- Hotfix가 나가야 할 경우



### 정기 배포를 위한 GitFlow전략

#### 요구사항
 1. Login 기능 개발
 2. Logout 기능 개발  
     Lv1. `Main` →  
     Lv2.　　　　　`Dev` →  
　　　　　　　　　　　1. `Feature`:로그인  
　　　　　　　　　　　2. `Feature`:로그아웃  
     Lv3.　　　　　`Dev` ← Merge  
　　　　　　　`Release` (Main으로 배포 전)  
     Lv4. `Main` ←
          최종 배포시 Release브랜치를 Main브랜치에 Merge  
     Lv5.　　　 →  
     Lv6.　　　　　`Dev` (Merge)
  

 Main 브랜치에 추가된 작업 내용을 반드시 Dev 브랜치에 Merge하여 Main과 Dev간의 Sync를 맞춰준다.  
       (Password 조회기능 작업내용)

## Release Branch 생성 후 추가 작업이 필요해질 경우
예를들어 실제 QA나 테스트를 하는 과정에서 로그인/로그아웃 기능 뿐만 아니라 패스워드 조회 기능 필요에 대한 의사결정이 나온다면  
FindPW 라는 Feature 브랜치를 어떤 브랜치를 베이스로 생성해야 되느냐  
Release브랜치가 생성된 이후부터는 무조건 Release브랜치를 베이스로 Feature를 생성을 하고, 작업을 마무리짓고 다시 Release브랜치에 Merge한다.    
최종적으로 Release브랜치는 최종적으로 모든 코드들을 다가지고 있는 상태이다.  
(그러나 이때 Dev에는 Password 조회 작업이 존재하지 않다.)  
## Main과 Dev간의 Sync를 맞춰야 하는 이유
Dev의 경우 전제가 Main을 베이스로 생성된 브랜치이므로, Dev는 Main +@의 코드를 가지고 있어야 한다.
만약 Sync가 맞지 않다면 Release에서 작업한 Feature내용이 Main에는 있지만 Dev에는 없는 그런 상태로 유지될 수 있다.
이 상태에서 누군가 Dev에서 신규 Branch를 생성하게 되면 코드가 꼬이게 된다.

에를들어 초기 Main브랜치의 특정 메소드를 호출했을 때, 1이라는 값을 리턴했었으나
Hotfix 배포 이후 2라는 값을 리턴하도록 수정되었다면
Main브랜치를 Dev에 Merge하지 않았다면 많은 사람들이 Dev브랜치를 베이스로 신규 Feature를 생성하였을 때, 2가 리턴되지 않고 1을 리턴하게 된다.
이것이 바로 Main과 Dev간의 Sync가 깨지는 현상이며, 결과적으로 서로 코드가 꼬이게 되고 실제 마지막에
Dev에서 Release를 따고 Release를 Main에 Merge하는 순간 엄청난 충돌이 발생하게 된다.
따라서 반드시 운영 배포 후에는 Dev에 Main브랜치를 반드시 머지해줘야 된다.

## 릴리즈 브랜치 생성 후 추가작업이 없는 경우
Release를 Main에 Merge한 뒤 Dev에 Main을 Merge하여 Sync동기화 한다.

## Hotfix가 필요한 경우
   Lv1. `Main` →  
   Lv2. 　　　　　`Dev` →  
   　　　　　　　　　　　1. `Feature`:로그인  
   　　　　　　　　　　　2. `Feature`:로그아웃

위와같은 상황에서 운영에서 장애가 발생한다면 Hotfix로 이슈를 수정하여 배포해야한다.
Hotfix의 경우 Dev에서 브랜치를 생성하는것이 아닌 Main 브랜치를 베이스로 Hotfix브랜치를 생성한다.  
이후 Hotfix 브랜치를 베이스로 최소한의 수정 범위를 갖는 Feature브랜치를 생성하여 기능 로직을 개발한 후 Hotfix에 작업내역을 Merge하고,  
완료된 Hotfix Branch를 Main Branch에 Merge 해주면 된다.  

그 다음 동일하게 Main Branch를 Dev Branch에 Merge함으로써 Sync 동기화를 진행하면 된다.  
Hotfix가 Main을 베이스로 생성하였으므로, Dev에는 Main에 Merge한 Hotfix 작업내역이 없다.  
이후 다른 작업자들이 Dev에서 Feature를 생성하면 Hotfix작업내역이 없게 되므로  
Hotfix배포를 포함한 모든 배포 이후에는 함께 작업하는 모든 협업 개발자들에게 새로운 요구사항에 대한 Main을 운영에 배포되었고  
Dev에 Main을 Merge하였으니 Dev에 새로 Merge한 내역을 한번 Pull땡겨주시고 본인 로컬 브랜치에 Merge해주세요 라고 공지를 한다.  

