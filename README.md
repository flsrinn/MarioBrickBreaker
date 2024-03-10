# MarioBrickBreaker

## gamePlayer 패키지
벽돌 깨기 게임을 플레이할 수 있는 프로그램 <br> 

<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/92bea331-16bf-4598-accb-5cf060d490d6" width="600" height="400"> <br>
### 게임 메인 화면
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/96770884-8c05-49c3-aec5-08026c8cb250" width="600" height="400">
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/a387baba-4f2c-4aa9-9df3-a0e54476cf63" width="600" height="400"> <br>
벽돌에 쓰인 숫자만큼 공으로 맞춰 깨는 게임 <br>
⚪ : 1단계 벽돌 <br>
🔵 : 2단계 벽돌 <br>
🟢 : 3단계 벽돌 <br>
🟣 : 4단계 벽돌 <br>
🔴 : 미스터리 벽돌 <br>
✖️ : 숫자가 없는 벽돌 = 깨지지 않는 벽돌 <br>
높은 단계 벽돌일 수록 높은 숫자가 나옴 <br>


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

상단 메뉴바에서 **Game->Play 버튼**을 눌러야 게임을 진행할 수 있다.

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
### 게임 오버 화면
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/8562cb86-8821-4244-926c-0aa39b702ab0" width="600" height="400"> <br>
RESTART 버튼을 누르면 재시작 가능 <br> 

## gameTool 패키지
게임의 기본 틀이 되는 XML 파일을 제작하는 프로그램  <br>
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/d9475c72-cba7-4525-a205-2b181ecea023" width="600" height="400"> <br>
title에서 마우스의 현재 좌표 확인 <br>
원하는 벽돌/아이템을 선택해서 원하는 곳에 왼클릭하면 배치 가능 <br>
아이템의 왼쪽 하단 모서리를 드래그해서 크기 조정 가능 <br>
아이템을 드래그하면 위치 이동 가능

### 공 설정
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/299da3af-52c2-458b-aa95-d508000891cc" width="400" height="300"> <br>
BALL 버튼을 눌러 공의 속성을 설정할 수 있다. <br>

### 생명 개수 설정
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/0cec2999-6ca8-4a84-b8c4-91b10453a4bb" width="300" height="150"> <br>
LIFE 버튼을 눌러 생명의 개수를 설정할 수 있다. <br>

### 메뉴바 설명 

#### 벽돌 메뉴
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/76a596ac-8a7f-4bd4-a04d-88b34372ddf1" width="600" height="400"> <br>
벽돌을 우클릭으로 2번 클릭하면 메뉴가 나타남
Type : 벽돌 레벨이나 미스터리 블록, 깨지지 않는 블록으로 설정 가능 <br>
Copy : 해당 벽돌 속성, 크기 등을 클립보드에 복사하고 <br>
원하는 곳에 우클릭 한 번을 하면 붙여넣기
Image : 벽돌 이미지 변경 <br>
Delete : 벽돌 삭제 <br>


#### Edit 메뉴
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/c32afc14-4ddf-42c3-a871-8ddae2b50387" width="600" height="400"> <br>
Undo : 가장 직전에 배치된 아이템 삭제 <br>
Resize : 창 사이즈 변경  <br>
Reset : 모든 아이템 삭제 <br> 

#### Music 메뉴
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/b414f0cc-a39c-465b-ab7b-40f1aa61feb8" width="600" height="400"> <br>
Setting : 배경 음악, 효과 설정<br>

#### Image 메뉴
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/c678b2d1-12ad-47d5-8700-802396921716" width="600" height="400"> <br>
Change : 블록 이미지, 배경 사진 변경 <br>
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/c02c6e8c-b7fc-4648-811b-aba45e623137" width="400" height="300"> <br>
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/9396fc5c-a721-4f6e-99af-04ae8ae3c092" width="600" height="400"> <br>
적용 화면

#### File 메뉴
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/c5eeded6-d841-4172-b898-8e065b1c195d" width="600" height="400"> <br>
Save : 기본 파일명으로 저장 (바로 직전에 저장된 숫자 다음 수로 저장) <br>
ex) block3.xml까지 저장되었을 경우, block4.xml로 저장<br>
Save As : 파일 이름을 지정해서 저장<br>
Load : 저장된 XML 파일을 불러와 수정<br>
게임 플레이의 필수 요소인 벽돌, 패들, 대포를 무조건 하나 이상 배치해야 저장 가능<br>
**단 패들과 대포는 하나만 배치 가능하고, 2개 이상 배치할 경우 경고 메세지가 출력되며 배치 불가함.** <br>
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/6e6f4ad0-67f0-4c25-b21f-dfff20a90309" width="600" height="400"> <br>
<img src="https://github.com/flsrinn/MarioBrickBreaker/assets/123474937/df5eb5ba-43ce-4cd0-95e6-3ce43c4ed1cc" width="500" height="400"> <br>

Save As를 통해 저장된 hello.xml 파일 
