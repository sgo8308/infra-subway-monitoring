<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
  <a href="https://edu.nextstep.camp/c/R89PYi5H" alt="nextstep atdd">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/next-step/atdd-subway-service">
</p>

<br>

# 인프라공방 샘플 서비스 - 지하철 노선도

<br>

## 🚀 Getting Started

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run dev
```
#### application 구동
```
./gradlew clean build
```
<br>


### 1단계 - 웹 성능 테스트
1. 웹 성능예산은 어느정도가 적당하다고 생각하시나요

### 가장 중요한 페이지

제가 생각하는 저희 지하철 노선도 서비스는 다음과 같은 사용자 시나리오를 가진 아주 간단한 서비스입니다.

1. 사용자는 경로 검색 페이지에 들어온다.
2. 출발역과 도착역을 정하고 검색 버튼을 클릭한다.
3. 최단 거리와 경로를 확인한다.

사용자 시나리오에 따르면, 가장 중요한 페이지는 **'경로 검색 페이지'** 와 **'경로 검색 결과 페이지'** 라고 생각됩니다.

### 목표 지표 고르기

<경로 검색 페이지>

저희 서비스의 경로 검색 페이지에서는 단순히 출발역 도착역 등의 텍스트가 뜨는 것보다, 출발역을 클릭해서 입력이 가능해지는 상태를 가장 기다릴 것입니다.
따라서 저는 웹사이트가 사용자와 상호작용이 가능해지기까지 걸리는 시간을 의미하는 TTI(Time To Interactive)지표를 목표 지표로 고르겠습니다.

<경로 검색 결과 페이지>

반면 경로 검색 결과 페이지 같은 경우 사용자들은 거리나 경로와 같은 텍스트 또는 이미지가 뜨기를 가장 기다릴 것입니다.
따라서 저는 가장 큰 비주얼 콘텐츠가 표시되는 시간을 의미하는 LCP(Largest Contentful Paint)를 목표 지표로 고르겠습니다.

### 목표 시간 잡기

저는 3초의 법칙을 지키면서 목표 지표에 대한 시간이 주요 경쟁사 중 가장 빠른 서비스 대비 최소 20% 이상 뛰어난 수준으로 목표 시간을 잡아보도록 하겠습니다.

위 목표에 대한 저의 논리는 이렇습니다.

'지하철 노선도 서비스는 서비스 자체로 다른 서비스와 차별화를 꾀하기 힘들다'<br>
'이러한 상황에서 후발주자로써 돋보이려면 속도가 빨라야 한다.'<br>
'더군다나 우리 서비스는 주요 경쟁사보다 간단한 정보를 제공한다. '<br>
'따라서 주요 경쟁사 중 가장 빠른 서비 대비 최소 20% 이상 뛰어난 수준으로 목표를 설정한다.'

### 경쟁사 분석

저희는 웹 서비스이기 때문에 웹으로 이용 가능한 '네이버지도'와 '카카오맵'을 주요 경쟁사로 잡겠습니다.
성능 측정 툴은 PageSpeed를 이용하겠습니다.

<경로 검색 페이지>
네이버지도 측정 URL : https://map.naver.com/v5/subway/1000/-/-/-?c=15,0,0,0,dh
결과 : TTI 4.6초

카카오맵 측정 URL : https://map.kakao.com/
결과 : TTI 2.6초

네이버보다 빠른 카카오의 TTI인 2.6초에 비해 20% 빠른 2초를 목표로 잡도록 하겠습니다.

<경로 검색 결과 페이지>
네이버지도 측정 URL : https://map.naver.com/v5/subway/1000/417/239,4003,1609/-?c=15,0,0,0,dh
결과 : LCP 6.3초

네이버지도의 LCP가 3초를 넘어가기 때문에 저는 3초의 법칙에 따라 LCP 3초를 목표로 잡도록 하겠습니다.

> 카카오맵의 경우 출발역과 도착역을 각각 선택할 때마다 결과가 바로 바로 비동기적으로 업데이트되어 URL이 변화하지 않기 때문에
WebPageTest나 PageSpeed로 테스트가 어렵고, 동작 방식이  저희 서비스와 사뭇 다르다 생각해 제외하였습니다.

### 최종 목표

경로 검색 페이지 : 최대 TTI 2s
경로 검색 결과 페이지 : 최대 LCP 3s


3. 웹 성능예산을 바탕으로 현재 지하철 노선도 서비스의 서버 목표 응답시간 가설을 세워보세요.

<경로 검색 페이지>

정적 서버

브라우저와 정적 웹서버는 TTI를 달성하기까지 처음부터 끝까지 상호작용하며 작업을 진행합니다.
따라서 이 구간의 목표는 최종 목표와 동일하게 '최대 TTI 2s'로 잡겠습니다.

WAS 및 DB 서버

경로 검색 페이지는 따로 WAS의 요청할 일이 없으므로 목표는 따로 없습니다.

<경로 검색 결과 페이지>

정적 서버

위와 마찬가지 이유로 최종 목표와 동일하게 '최대 LCP 3s'로 잡겠습니다.

WAS 및 DB 서버

퍼포먼스 탭을 통해 분석해본 결과 WAS와 DB가 담당한 것으로 여겨진 회색 json 파일 중 가장 오래 걸린 것이 116ms가 걸렸습니다.
저는 116ms 보다 20% 빠른 92.8ms를 목표로 잡고, 일반적으로 뒷단에서는 WAS보다 DB의 레이턴시가 크다는 것을 근거로
3:7의 비율로 WAS의 로직 처리에 27.8ms를 분배하고 DB 조회에는 65ms의 시간을 분배하도록 하겠습니다.

* 정리

|               | 경로 검색 페이지 | 경로 검색 결과 페이지 |
|---------------|------------------|-----------------------|
| 최종 목표         | 최대 TTI 2s      | 최대 LCP 3s           |
| 정적 리소스 반환     | 최대 TTI 2s      | 최대 LCP 3s           |
| WAS 로직 처리     |                  | 최대 27.8ms           |
| DB 조회         |                  | 최대 65ms             |


---

### 2단계 - 부하 테스트
1. 부하테스트 전제조건은 어느정도로 설정하셨나요

대상 시스템 범위
- Web Server - WAS - DB 등에서 경로 검색 API와 관련 있는 부분

테스트 환경
k6 agent를 서버와 다른 AZ에 구성 후 공용 IP를 이용해 접근

목표 값
- Latency : 138ms (WAS + DB = 93ms + 정적 리소스가 브라우저에 도착하는 시간 = 45ms)
- throughput : 925rps
- 부하 유지기간
  - smoke : 1분
  - load : 평소 상황 10분 최대 트래픽 30분
  - stress : vuser를 100씩 올려가며 3분간 유지
 
부하 테스트 시 저장될 데이터 건수 및 크기
- 없음. (조회만 테스트하므로)

시나리오

1. 경로 검색 페이지 접속
2. 역 검색 (이 때 다양한 역을 검색할 수 있도록 전체 역에 대해 랜덤 값이 나오도록 했으나, 라인에 등록된 역만 조회 가능하기에 라인에 등록된 역 중 랜덤값 내기 용이한 1~16의 번호를 가진 역으로 조정)

최종 목표 
- 925 RPS 상황에서 요청 당 138ms 응답 안정적으로 내기

VUser 계산

Smoke Test
- Vuser : 1

load test
- 평소 트래픽 상황 rps = 185
- Vuser
  = 185 x (0.138s + 0s(network latency))
  = 25
 
- 최대 트래픽 상황 rps = 925
- Vuser
  = 925 x 0.138
  = 128

Stress Test
- Vuser : 100 to 300


2. Smoke, Load, Stress 테스트 스크립트와 결과를 공유해주세요

스크립트와 결과는 performance-test 폴더에 넣어 놓았습니다.

결과 요약

찾은 문제점
1. 테스트가 오래될 경우 특정 시점부터 응답 시간이 급격히 늘어나게 되고 실패하는 요청들이 생깁니다. from load  test
   - 이 부분은 로그 파일이 너무 커져서 디스크가 꽉찼기 때문으로 밝혀졌습니다.
     - 사이즈나 기간 단위로 로깅 파일을 삭제하는 등의 로깅 전략을 구성해서 해결해야 할 것으로 보입니다.
2. 동시 사용자(Vuser)가 250이 넘어가는 순간부터 요청이 대규모로 실패하게 됩니다. from stress test


시스템의 성능
동시 사용자 3, RPS 40까지는 목표 응답 시간 138ms를 달성함

(이렇게 시스템의 성능을 표현하는게 맞는지 잘 모르겠네요 ;)

---

### 3단계 - 로깅, 모니터링
1. 각 서버내 로깅 경로를 알려주세요

Web Server 
- /var/log/syslog
- /var/log/nginx/access.log
- /var/log/nginx/error.log

WAS
- /var/log/syslog
- /home/ubuntu/nextstep/infra-subway-monitoring/log/file.log

DB
- /var/log/syslog

2. Cloudwatch 대시보드 URL을 알려주세요

https://ap-northeast-2.console.aws.amazon.com/cloudwatch/home?region=ap-northeast-2#dashboards:name=sgo8308