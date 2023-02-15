# Amazon ELB

- ELB는 애플리케이션의 트래픽을 여러 EC2 인스턴스 또는 가용 영역에 분산한다. 
- ELB는 EC2 인스턴스의 상태를 지속적으로 확인하고 비정상 인스턴스에서 요청을 자동으로 라우팅하여 애플리케이션의 내결함성과 상태를 높인다. 
- Auto Scaling과 결합된 ELB는 인프라가 변화하는 워크로드 및 최대 트래픽 요구 사항을 충족하도록 보장할 수 있다. 

<br/>

- ELB는 다양한 사용 사례에 대해 여러 유형의 로드밸런서를 제공한다. 
- 여기서는 클래식 ELB 로드 밸런서를 모니터링 할 주요 지표에 중점을 둔다. 

## Metrics to watch

- ELB는 애플리케이션 사용자의 첫 번째 접점 역할을 하기 때문에 주요 ELB 성능 지표를 면밀히 주시하는 것은 최종 사용자 경험을 개선하는 데 매우 중요하다. 
- 다음 메트릭은 전체 ELB 성능과 헬스를 추적하는데 도움을 준다. 

### RequestCount

- 이 메트릭은 쿼리된 기간 동안 ELB에서 처리한 요청 수를 측정한다. 
- AWS또는 DNS 문제의 조기 경고 신호로 적용할 수 있는 급격한 변화가 있는지 이 지표를 모니터링 해야한다. 
- 이는 AutoScaling을 사용하지 않은 경우 EC2 집합을 확장 및 축소해야 하는 시기를 결정하는데 도움을 준다. 

### SurgeQueueLength

- SurgeQueueLength 는 사용 가능한 컴퓨팅 리소스에 대한 배포를 기다리는 요청 수를 측정한다. 
- 대기중인 요청 수가 많으면 지연 시간이 늘어날 수 있다. 
- 이 제한에 도달하면 추가 수신 요청이 삭제되므로 대기열 용량 (1,024개 요청) 에 도달하지 않도록 메트릭의 최대값을 특히 모니터링 해야한다. 

### HTTPCode_ELB_5XX

- 이 메트릭은 프런트엔드 및 백엔드 연결 모두에 대해 HTTP/HTTPS 프로토콜을 사용하여 ELB Listener를 구성한 경우에만 사용할 수 있다. 
- 이는 지정된 기간동안 로드 밸런서에서 반환된 서버 오류수를 추적한다. 
- HTTP 5XX 서버 에러는 잘못된 게이트웨이(502), 서비스를 사용할 수 없음(503) 및 게이트웨이 시간 초과 (504)를 포함한 오류조사를 보증한다.
- 예를 들어 응답 대기 시간과 함께 게이트웨이 시간 초과(504) 오류가 증가하는 경우 백엔드를 확장하거나 유휴시간 초과를 늘여 연결이 더 오래 열려 있도록 할 수 있다. 

### Latency

- 이 메트릭은 로드 밸런서 자체의 지연 시간이 아니라 백엔드 인스턴스의 지연 시간을 측정한다. 
- HTTP 리스너를 사용하는 경우 이 지표는 백엔드 인스턴스가 로드 밸런서에서 보낸 요청에 응답하기 시작하는 데 걸리는 총 시간(초)를 측정한다.
- TCP 리스너의 경우 이 지표는 로드 밸런서가 등록된 인스턴스에 연결하는 데 걸리는 시간(초)을 측정한다. 
- 두 경우 모두 이 메트릭을 사용하여 요청 처리 시간 증가 문제를 해결할 수 있다. 
- 대기 시간이 길다는 것은 애플리케이션 성능에 부정적인 영향을 미칠 수 있는 네트워크 연결, 백엔드 호스트 또는 웹 서버 종속성 문제를 나타낼 수 있다. 

### HealthyHostCount and UnHealthyHostCount

- ELB는 상태확인을 사용하여 요청을 처리하기에 충분히 백엔드 인스턴스가 헬시한지 검증한다. 
- 인스턴스가 연속적으로 특정 수의 상태확인에 실패한 경우 (상태 정상 임계값에 의해 지정되) ELB는 자동으로 해당 인스턴스에서 트래픽을 보내고, 사용 가능한 정상 인스턴스로 라우팅한다. 
- HealthyHostCount를 SurgeQueueLength및 Latency와 같은 다른 메트릭과 상호 연관시켜 각 가용 영역에서 트래픽을 처리하고 서비스 중단을 방지하기에 충분한 정상 인스턴스가 있는지 확인할 수 있다. 

## Furture reading

- [Top ELB health and performance metrics](https://www.datadoghq.com/blog/top-elb-health-and-performance-metrics/)
- [How to collect AWS ELB metrics](https://www.datadoghq.com/blog/how-to-collect-aws-elb-metrics/)
- [Monitor ELB performance with Datadog](https://www.datadoghq.com/blog/monitor-elb-performance-with-datadog/)

## Key ELB performance metrics

- 사용자와 애플리케이션 간의 첫 번째 게이트웨이인 로드 밸런서는 확장 가능한 인프라의 중요한 부분이다. 
- 제대로 작동하지 않으면 사용자는 애플리케이션 응답 시간이 훨씬 느려지거나 완전히 오류가 발생하여 트랜잭션이 손실될 수 있다.
- 그렇기 때문에 로드 밸런서 자체와 그 뒤에 있는 EC2 인스턴스가 정상 상태를 유지하려면 ELB를 지속적으로 모니터링 하고 주요 지표를 잘 이해애야한다.
- 여기에는 2가지 메트릭 모니터링 카테고리가 있다. 
  - 로드밸런서 메트릭
  - 백엔드 관련 메트릭 

### Load balancer metrics

![loadbalancer](https://imgix.datadoghq.com/img/blog/top-elb-health-and-performance-metrics/1-02.png?auto=format&fit=max&w=847)

- 고려해야할 지표의 첫번재 범주는 로드 밸런서에 등록된 백엔드 인스턴스와 달리 로드 밸런서 자체에서 비롯된다. 
- 각 메트릭에 대해 일반적으로 모두 사용할 수 있으므로 모니터링할 가장 관련성 있고 유용한 통계(합계, 평균, 최소, 최대)를 기록했다. 

- RequestCount
  - 선택한 기간 동안 ELB가 수신하고 등록된 EC2 인스턴스로 보낸 요청 수(합계)
  - Work: Throughput
- SurgeQueueLength
  - 백엔드 인스턴스에서 수락 및 처리되기를 기다리고 있는 로드 밸런서에서 현재 대기 중인 인바운드 요청 수(최대)
  - Resource: Saturation
- SpilloverCount
  - 선택한 기간 동안 전체 급증 대기열로 인해 거부된 요청 수(합계)
  - Work: Error(due to resource saturation)
- HTTPCode_ELB_4XX*
  - 선택한 기간(합계) 동안 로드 밸런서에서 반환된 HTTP 4xx 오류 (클라이언트 오류)의 수이다.
  - Work: Error
- HTTPCode_ELB_5XX*
  - 선택한 기간(합계) 동안 로드 밸런서에서 반환한 HTTP 5xx 오류 (서버오류) 의 수이다.
  - Work: Error

- Elastic Load Balancing: 구성에는 연결 요청을 확인 하는 ELB 프로세스인 하나 이상의 리스너가 필요하다. 
- 위에 명명된 HTTPCode_ELB 메트릭은 리스너가 프런트 엔드 및 백 엔드 연결 모두에 대해 HTTP 또는 HTTPS 프로토콜로 구성된 경우에만 사용할 수 있다. 

#### RequestCount

- 이 측정항목은 로드 밸런서가 처리하는 트래픽의 양을 측정한다. 
- 피크와 드롭을 주시하면 AWS의 문제 또는 DNS와 같은 업스트림 문제를 나타낼수 있는 급격한 변화에 대해 경고할 수 있다. 
- AutoScaling을 사용하지 않는다면 오쳥 수가 크게 변경되는 시기를 알면 로드 밸런서를 지원하는 인스턴스 수를 조정해야하는 시기를 알 수 있다. 
  
#### SurgeQueueLength

- 백엔드 인스턴스가 완젼히 로드되어 더이상 요청을 처리할 수없으면 수신 요청이 대기열에 들어가 지연시간이 증가하여 사용자 탐색 속도가 느려지거나 시간초과 오류를 발생할 수 있다. 
- 그렇기 때문에 이 메트릭은 가능한 한 낮게, 이상적으로는 0으로 유지해야한다. 
- 백엔드 인스턴스는 여러가지 이유로 새 요청을 거부할 수 있지만 너무 많은 연결이 원인인 경우가 많다. 
- 이 경우 백엔드를 조정하거나 백엔드 용량을 추가하는 것을 고려해야한다. 
- "max" 통계는 대기 중인 요청의 피크를 볼 수 있도록 이 메트릭의 가장 관련성이 높은 사항이다. 
- 결정적으로 대기열 길이가 현재 최대 1,024개 요청으로 제한되어 있는 최대 대기열 용량보다 항상 상당히 작게 유지되도록 하여 요청이 삭제되는 것을 방지할 수 있다. 

#### SpilloverCount

- SurgeQueueLength 가 대기열에 있는 최대 요청 1,023개에 도달하면 새 요청이 삭제되고 사용자는 503 오류를 수신하며 스필오버 카운트 메트릭이 증가한다. 
- 건강한 시스템에서 이 값은 항상 0을 유지한다. 

#### HTTPCode_ELB_5xx

- 이 측정항목은 제대로 처리할 수 없는 요청 수를 계산한다. 다양한 근본 원인이 있을 수 있다. 

##### 502 (Bad Gateway): 

- 에러 코드가 502인 경우 백엔드 인스턴스가 응답을 반환했지만 로드 밸런서가 제대로 작동하지 않거나 응답 형식이 잘못되어 로드 밸런서가 이를 구문 분석할 수 없다. 

##### 503 (Service Unavailable)

- 에러 코드가 503인 경우 요청을 처리할 용량이 충분하지 않은 백엔드 인스턴스 또는 로드 밸런서에서 오류가 발생한 것이다. 
- 인스턴스가 정상이고 로드 밸런서에 등록되어 있는지 확인하라. 

##### 504 (Gateway Timeoout)

- 504 오류가 반환되면 응답 시간이 ELB의 유휴 시간 초과를 초가환경우 
- 대기시간이 높고 ELB에서 5xx 오류가 반환되는지 확인하자.
- 이 경우 백엔드를 확장하거나 조정하거나 유휴 시간제한을 늘려 파일 업로드와 같은 느린 작업을 지원하는 것을 고려하라..
- 인스턴스가 ELB와의 연결을 닫는 경우 ELB유휴 시간 초과보다 높은 식나 초과로 연결 유지를 확덩화해야한다.

#### Note about HTTPCode_ELB_4XX:

- 이 메트릭은 기본적으로 ELB(4xx 코드를 반환함)로 전송이 잘못된 요청의 수를 측정하기 때문에 일반적으로 4xx 오류에 대해 할 수 있는 일은 많지 않다.
- 조사가 필요한경우 ELB 엑세스 로그를 참조하여 반환 코드를 확인할 수 있다. 

### Backend-related metrics

![backend-related](https://imgix.datadoghq.com/img/blog/top-elb-health-and-performance-metrics/1-04.png?auto=format&fit=max&w=847)

- CloudWatch 는 또한 응답지연 시간 또는 ELB 상태 확인 결과와 같이 백엔드 인스턴스의 상태 및 성능에 대한 지표를 제공한다. 
- 상태 확인은 ELB가 다른 곳에서 요청을 보낼 수 있도록 비정상 인스턴스를 식별하는 데 사용하는 메커니즘 이다. 
- 기본 상태 확인을 사용하거나 다른 프로토콜, 포트 또는 정상/비정상 임계값을 사용하도록 구성할 수 있다. 
- 상태 확인 빈도는 기본적으로 30초 이지만 이 간격을 5~300초 사이로 설정할 수 있다. 

- HealthyHostCount:
  - 각 가용 영역의 현재 정상 인스턴스 수이다. 
  - Resource: Availability
- UnHealthyHostCount
  - 각 가용 영역의 현재 비정상 인스턴스 수이다. 
  - Resource: Availability 
- Latency
  - 로드 밸런서와 백엔드 간의 왕복 요청 처리시간
  - Work: Performance
- HTTPCode_Backend_2XX / HTTPCode_Backend_3XX
  - 선택한 기간 동안 등록된 백엔드 인스턴스에서 반환된 HTTP 2xx(성공)/3xx(리디렉션) 코드 수이다. 
  - Work: Success
- HTTPCode_Backend_4XX / HTTPCode_Backend_5XX
  - 선택한기간 동안 등록된 백엔드 인스턴스에서 반환된 HTTP 4xx(클라이언트 오류)/5xx(서버오류) 코드의 수이다.
  - Work: Error
- BackendConnectionErrors
  - 로드 밸런서와 겉보기에 정상인 백엔드 인스턴스 간에 시도했지만 실패한 연결수
  - Resource: Error

- 이러한 수는 경우에 따라 CloudWatch 에서 해석하기 까다로울 수 있다. 
- 사실 교차영역 밸런싱이 ELB에서 활성화 되면 (트래픽이 서로 다른 가용 영역에 고르게 분산되도록) 이 로드 밸런서에 연결된 모든 인스턴스는 Cloud Watch에 의해 모든 AZ의 일부로 간주된다.)
- 따라서 예를 들어 한존에 2개의 정상 인스턴스가 있고 다른 존에 3개의 정상 인스턴스가 있는 ELB는 AZ당 5개의 정상 호스트를 표시하므로 직관적이지 않을 수 있다. 

- HealthyHostCount and UnHealthyHostCount
  - Alert 메트릭
  - 인스턴스가 상태 확인에 대해 정의된 비정상 임계값을 초과하는 경우 ELB는 플래그를 지정하고 해당 인스턴스에 대한 요청 전송을 중지한다. 
  - 가장 일반적인 원인은 로드 밸런서의 시간 초과를 초과하는 상태 확인이다.
  - 우수한 성능을 보장하려면 각 가용 영역에 항상 충분한 정상 백엔드 인스턴스가 있어야한다. 
  - 또한 이 지표를 Latency 및 SurgeQueueLength 와 연결시켜 응답 시간을 크게 늦추지 않고 수신 요청의 보륨을 지원할 수 있는 충분한 인스턴스가 있는지 확인해야 한다. 
- Latency
  - Alert 메트릭
  - 이 지표는 로드 밸런서 자체의 지연 시간이 아니라. 백엔드 인스턴스의 요청 처리로 인한 애플리케이션 지연 시간을 측정한다. 
  - 백엔드 지연 시간을 추적하면 애플리케이션 성능에 대한 좋은 통찰력을 얻을 수 있다. 
  - 높으면 시간 초과로 인해 요청이 삭제되어 사용자가 좌절할 수 있다. 
  - 높은 대기 시간은 네트워크 문제, 과부하된 백엔드 호스트 또는 최적화되지 않은 구성으로 인해 발생할 수 있다. (예 연결 유지를 확성화 하면 대기 시간을 줄이는 데 도움이 될 수 있다.)
  - 다음은 높은 지연 시간 문제를 해결하기 위해 AWS에서 제공하는 몇 가지 팁이다. https://aws.amazon.com/premiumsupport/knowledge-center/elb-latency-troubleshooting/


- BackendConnectionErrors
  - ELB가 백엔드에 연결을 시도하지만 성공적으로 연결 할 수 없을때 ELB와 서버 간의 연결 오류가 발생한다. 
  - 이 타입의 오류는 일반적으로 네트워크 문제 또는 제대로 실행되지 ㅇ낳는 백엔드 인스턴스로 인해 발생한다. 
  - ELB성능 오류 및 대기 시간에 대해 이미 경고하기 있는 경우 사용자에게 직접적인 영향을 미치지 않는 연결 오류에 대해 경고를 받고 싶지 않을 수 있다. 

  - 노트: 백엔드와의 연결이 실패하면 ELB가 다시 시도하므로 이 수가 요청 속도보다 높을 수 있다. 

### About backend response codes

- 서버에 대한 상위 수준 보기를 위해 백엔드에서 반환된 HTTP코드를 모니터링 할 수 있다.
- 그러나 서버에 대한 더 세분화되고 더 나은 통찰력을 얻으려면 직접 또는 인스턴스에서 기본 메트릭을 수집하여 모니터링 하거나 해당 로그도 분석해야한다. 

### About timeouts

- 각 요청에 대해 클라이언트와 로드 밸런서 간에 하나의 연결이 있고, 로드 밸런서와 백엔드 간에 하나의 연결이 있다. 
- 그리고 각 요청에 대해 ELB에는 기본적으로 60초인 전체 유휴 시간 제한이 있다.
- 이 60초 이내에 요청이 완료되지 않으면 연결이 닫힌다. 
- 만약 파일 전송과 같은 긴 작업을 완료할 수 있도록 이 유휴 시간 초과를 늘릴수 있다. 

- 로드 밸런서가 백엔드 호스트와의 연결을 재사용할 수 있도록 EC2 백엔드 인스턴스 설정에서 연결 유지를 활성화 하여 리소스 사용율을 줄이는 것을 고려할 수 있다. 
- 연결 유지 시간이 ELB의 유휴 시간 초과보다 크게 설정되어 로드 밸런서보다 먼저 백엔드 인스턴스가 연결을 닫지 않도록 하라. 
- 그렇지 않으면 ELB가 백엔드 호스트를 비정상으로 잘못 플래그할 수 있다. 

### Hosts metrics for a full picture

- 백엔드 인스턴스의 상태와 로드 밸런서의 성능은 직접적인 관련이 있다. 
- 예를 들어 백엔드 인스턴스의 CPU 사용율이 높으면 대기 중인 요청이 발생할 수 있다. 
- 이러한 대기열은 결국 최대 길이를 초과하여 요청을 삭제하기 시작할 수 있다. 
- 따라서 백엔드 호스트의 리소스를 주시하는 것은 매우 좋은 생각이다. 
- 이러한 이유로 ELB의 성능 및 상태에 대한 전체 그림에는 EC2지표가 포함된다. 