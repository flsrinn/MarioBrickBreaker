# MarioBrickBreaker

## gamePlayer 패키지
벽돌 깨기 게임을 플레이할 수 있는 프로그램입니다. <br> 

<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/92bea331-16bf-4598-accb-5cf060d490d6" width="600" height="400"> <br>
### 게임 메인 화면
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/96770884-8c05-49c3-aec5-08026c8cb250" width="600" height="400">
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/a387baba-4f2c-4aa9-9df3-a0e54476cf63" width="600" height="400"> <br>
벽돌에 쓰인 숫자만큼 공으로 맞춰 깨는 게임입니다. <br>
⚪ : 1단계 벽돌 <br>
🔵 : 2단계 벽돌 <br>
🟢 : 3단계 벽돌 <br>
🟣 : 4단계 벽돌 <br>
🔴 : 미스터리 벽돌 <br>
✖️ : 숫자가 없는 벽돌 = 깨지지 않는 벽돌 <br>
높은 단계 벽돌일 수록 높은 숫자가 나옵니다.<br>


### 메뉴바 설명 

#### Game 메뉴
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/dbe5e6a7-f6df-4121-b699-92b4e0a097d2" width="600" height="400"> <br>

Play: 게임 플레이 버튼 <br>
Setting: 게임 난이도 조절(상, 중, 하)  <br>
상: 5-15 사이의 숫자 <br>
중: 3-10 사이의 숫자 <br>
하: 1-7 사이의 숫자 <br>
Pause: 움직이는 공을 멈춤 <br>

#### Music 메뉴
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/6075f975-7065-44b6-9974-2447c10ee2d1" width="600" height="400"> <br>
On: 음악 재생 버튼<br>
Mute: 음악 음소거 버튼<br>

#### File 메뉴
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/ced3fbcc-3cab-4652-b756-5f6a89a961e3" width="600" height="400"> <br>

Open: XML 파일을 오픈하여 맵을 변경할 수 있음<br>
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/a236205b-e7a3-44ef-a649-edcb2fc404e5" width="500" height="300"> <br>
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/da71e125-7f3f-494d-9380-61d533fd59d1" width="600" height="400"> <br>
block5.xml을 선택했을 때<br>

상단 메뉴바에서 **Game->Play 버튼**을 눌러야 게임을 진행할 수 있습니다.

**a** 또는 **d** 버튼을 눌러 공을 왼쪽 또는 오른쪽으로 발사 <br>
발사된 공은 대각선 방향으로 움직이고, 블록 또는 벽에 맞을 경우 방향 전환 <br>
공에 맞은 벽돌은 쓰여있는 숫자에서 -1 된 값으로 표시 <br>
공이 바닥에 떨어질 경우 생명 -1<br>
패들을 마우스로 드래그하며 공이 바닥에 떨어지지 않게 컨트롤해야함 <br>

벽돌에 공을 맞출 때마다 **+100원** <br>
돈을 이용해 상점에서 **아이템** 구매 가능

### 아이템
#### 벽돌에서 나오는 아이템
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/62b6a50a-5c04-408b-8db9-1e0270d71d82" width="600" height="400"> <br>
미스터리 벽돌을 깰 경우 특정 아이템이 나오고, 최종 점수에 영향을 끼친다. <br>
**현재 오류 = 꽃의 리스폰 위치 조정 중** <br>
🌻 : 60% 확률, 점수 +30<br>
👻 : 30% 확률, 점수 -50<br>
⭐ : 10% 확률, 점수 +100<br>

#### 상점에서 구매 가능한 아이템
오른쪽 점수판 아래 버튼을 이용해 아이템을 구매할 수 있다. <br>
🔫 : 총알 +1 <br>
❤️ : 생명 +1 (이미 최대치일 경우 증가하지 않음) <br>

**총알**을 발사하려면 패들 하단에 위치한 초록색 발사대를 좌, 우 방향키로 원하는 곳으로 이동시키고, <br>
스페이스바를 이용해 발사할 수 있다.<br>
### 게임 클리어 화면
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/4e9d928f-6558-446d-941c-44c723b465b4" width="600" height="400"> <br>
벽돌에서 얻은 아이템의 수로 최종 점수를 나타낸다. <br>
RESTART 버튼을 누르면 재시작 가능 <br> 


## gameTool 패키지
게임의 기본 틀이 되는 XML 파일을 제작하는 프로그램입니다. 

