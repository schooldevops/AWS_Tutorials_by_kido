# Key metrics for AWS Monitoring

from: https://www.datadoghq.com/blog/aws-monitoring/

- 초기 AWS는 IaaS 서비스에 대해서 빌드, 자동화, 확장성을 위한 작업을 열심히 수행했음
- 시간이 지나고 AWS는 기본 리소스 EC2, S3 등을 확장하고, CloudWatch를 통한 모니터링, RDS와 데이터베이스 관리와 같은 관리형 인프라, AWS Lambda 와 같은 서버리스 컴퓨팅 등으로 확장하였다. 
- 비록 AWS 에코시스템이 개발자와 운영팀이 빠르게 자신의 인프라를 확장을 지원하더라도 실제 서비스들의 헬스와 성능에 대한 더 많은 도전이 있다.

<br/>

- Datadob에서 AWS 통합메트릭은 전체 AWS환경의 지표를 한 곳에서 집계하고 잠재적인 문제를 효율적으로 조사하기 위해 매우 동적인 서비스에 대한 완전한 가시성을 얻을 수 있다. 
- 이 문서에서 Amazon EC2, EBS, ELB, RDS, ElastiCache 와 같이 널리 사용되는 서비스를 나머지 인프라 및 애프릴케이션과 함께 전체 컨텍스트에서 모니터링 하는 데 도움이 되는 몇 가지 주요 지표를 살펴본다. 
- 또한 Amazon Lambda 와 애플리케이션 서버의 시스템 수준 데이터가 없는 경우 집중해야 하는 지표에 대해서도 설명한다. 
- 각 세션에는 이 게시물에서 다루는 각 AWS 서비스에 대한 보다 포괄적인 모니터링 전략을 찾을 수 있는 "추가 자료" 섹션이 포함되어 있다. 

## Amazon EC2

### What is Amazon EC2?

- EC2는 필요한 경우 인프라 스트럭쳐를 효과적으로 프로비저닝 하고 스케일 하도록 한다. 
- EC2인스턴스 혹은 가상 서버들 이들은 인스턴스 타입의 범위에서 다양한 레벨의 CPU, 메모리, 스토리지, 네트워크 용량 등을 사용할 수 있도록 한다. 
- EC2 인스턴스는 Auto Scaling 및 Elastic Load Balance와 같은 다른 AWS 서비스와 원활하게 통합된다. 
- 컨테이너가 계속해서 증가함에 따라 EC2는 Amazon 의 ECS(Elastic Container Service) 및 EKS(Elastic Container Service for Kubernetes) 를 통해 오케스트레이션 된 애플리케이션의 요구사항을 충족하도록 진화했다. 

### Metrics to watch

- EC2 인스턴스를 배포하고 확장하기 위해서 지속적인 모니터링을 통해서 인프라스트럭쳐의 기능적인 속성을 보장해야한다. 
- 다음 메트릭은 성능과 인스턴스의 가용성에 대한 기본적인 이해를 제공한다. 

#### CPUUtilization

- CPUUtilicastion 은 EC2인스턴스에 의해서 사용되는 컴퓨트 유닛 할당에 대한 퍼센테이지를 보여준다. 
- 만약 높은 CPU 사용성이 지속적으로 사용되어 성능 저하가 발생하는 경우 (네트워크, 디스크 I/O, 메모리등의 영향이 없는경우), CPU는 아마도 리소스의 보틀넥이 된다. 
- CPUUtilization 을 트래킹 하는 것은 인스턴스들이 워크로드에 비해 너무 크거나 작은지 판단하는데 도움이 될 수 있다. 

![cpuutilization](./imgs/aws-monitoring-host-map-v2.avif)

#### DiskReadBytes and DiskWriteBytes

- EC2에 붙어 있는 인스턴스 스토어에서 읽기/쓰기의 바이트수를 측정한 값이다. 
- 이 C5, M5 인스턴스 타입에 붙어있는 EBS볼륨에 대한 메트릭스는 I/O 데이터를 포함한다. 
- 다른 인스턴스 타입에 대해서 EBS로 부터 직접적으로 디스크 I/O에 대해 접근할 필요가 있다. 
- 이 메트릭을 모니터링 하는 것은 어플리케이션 레벨 이슈를 구별하는데 도움을 준다. 
- 예를 들어 많은 용량의 데이터를 지속적으로 디스크로 부터 읽는 경우 아마도 캐싱 레이어를 추가하여 어플리케이션 성능을 향상시킬 수 있다. 

#### StatusCheck Failed

- CloudWatch 는 1분 인터벌 주기로 2번의 통과/실패 상태를 확인한다. 
- 한번은 각 EC2 인스턴스의 가용성을 쿼리하고 두번째는 인스턴스를 호스팅 하는 시스템에 대한 정보를 보고한다. 
- 이 체크들은 EC2의 헬스 상태를 가시적으로 보여주고, 문제의 원인이 인스턴스 자체에 있는지 아니면 인스턴스를 지원하는 기본 인프라에 있는지 판단하는데 도움을 준다. 

#### 이후 확인하기

- 여기에 커머된 모니터링 메트릭은 EC2인스턴스의 헬스와 성능을 트래킹 하는데 좋은 시작 포인트이다. 
- 그러나 동적인 AWS 인프라스트럭쳐에 대해서 스케일 업/다운이 수행될때 EC2인스턴스, 어플리케이션, 서비스 등에 대한 통찰력을 얻으려면 이러한 지표를 자동으로 수집, 시각화, 경고를 해야한다. 
- EC2에 대한 AWS 모니터링에 대해서 더 깊은 이해를 위해서는 EC2를 Datadog으로 시작할 필요가 있고, 다음 3가지가 도움이 될 것이다. 

- [Key metrics for EC2 monitoring](https://www.datadoghq.com/blog/ec2-monitoring/)
- [How to collect EC2 metrics](https://www.datadoghq.com/blog/collecting-ec2-metrics/)
- [How to monitoring EC2 instance with Datadog](https://www.datadoghq.com/blog/monitoring-ec2-instances-with-datadog/)

## Key metrics for EC2 monitoring

### Disk I/O metrics

- EC2에 붙어있는 저장소 볼륨은 2가지가 있음
  - EBS Volumes
    - 영구 저장공간을 제공함
  - Instance store Volume(일시적)
    - Host computer 에 물리적으로 붙어 있는 저장소
    - 인스턴스가 실행되면 사용가능한 영역임
    - EBS볼륨보다 성능 예측이 가능한 저장소이다. 
    - 하드웨어 리소스가 분리된다. 
    - 그러나 디스크가 fail이 되거나, 인스턴스가 종료되면 데이터는 유실된다. 
    - 많은 인스턴스 타입이 인스턴스 저장소를 제공하지 않는다.
- 두 저장소는 SSD, HDD 형태로 제공, 개수, 용량, 이스크 성능, 볼륨설정 등 다양한 현태를 가짐
- EC2 Monitoring 은 디스크 IOPS와 throughput 을 니즈에 따라 선택이 필요함
- 클라우드 워치의 메인 EC2 디스크 I/O 메트릭은 저장소로 부터 메트릭을 수집함
- Cloud Watch 는 EC2 네임스페이스에 대해서 EBS 디스크 I/O메트릭을 제공하지만, C5및 M5인스턴스 유형에만 사용할 수 있다. 
- CloudWatch에서 EBS메트릭은 다음 내용을 확인할 필요가 있따. 

<br/>

- DiskReadOps/DiskWriteOps
  - 인스턴스에 사용 가능한 모든 임시 볼륨에서 읽기/쓰기 작업 완료
  - Resource Utilization 메트릭 타입
- DiskReadBytes/DiskWriteBytes
  - 인스턴스에 사용 가능한 모든 임시 볼륨으로 부터 읽기/쓰기 바이트수
  - Resource Utilization 메트릭 타입

#### Disk Read/Write operations

- 인스턴스 스토어 볼륨들에서 저장된 데이터가 유실되기 때문에 이 타입의 볼륨은 I/O 인텐시브 (버퍼, 캐시, 임시 저장 스토어, 자주 내용이 변경되는 것과 같은) 작업에 적합하다. 
- 이 메트릭 쌍은 성능 저하가 지속적으로 높은 IOPS 의 결과인지 판단하는 데 도움을 줄 수 있다. 
- 인스턴스 볼륨이 HTTD인 경우 더 빠른 SSD 로의 전환을 고려할 수 있다. 
- 혹은 더 많은 볼륨을 붙일 수 있도록 인스턴스를 업그레이드 할 수 있다. 

#### Disk read/Write byte

- 디스크에 읽기/쓰기 데이터의 양을 모니터링 한다. 이는 어플리케이션 레벨의 문제를 드러날 수 있도록 해준다. 
- 너무 많은 데이터가 디스크로 읽으면 어플리케이션은 캐시 레이어를 두어서 향상 시킬수 있다. 
- 예상 보다 높은 디스크 읽기 또는 쓰기 수준이 장기간 지속되면 디스크 속도가 사용 사례와 일치할 만큼 충분히 빠르지 않은 경우 요청 대기 및 속도 저하를 의미할 수도 있다. 

### Network metrics

- 네트워크 메트릭은 클라우드 기반 서비스인 EC2와 같은 서비스에 매우 중요하다. 이는 지속벅인 네트워크 연결에 의존하며, 다양한 Available Zone에 분산되어 이루어진다. 
- 특히 EBS 볼륨을 인스턴스에 붙이는 경우 더 중요하다. 이들은 네트워크 드라이브이다. 
- 인스턴스 타입은 서로다른 네트워크 밴드위스와 최대 전송량 유닛 (MTU) 를 가지거나 혹은 많은 양의 데이터가 단일 패킷으로 전송될 수 있다. 
- 밴드위스 제한은 5 ~ 25Gbs 까지이다. 
- 네트워크 MTU는 표준은 1,500 바이트로 대부분의 인스턴스에서 적용된다. 
- 그러나 9,001 바이트와 같이 점보 프레임을 적용하여 어플리케이션이 큰 데이터를 이동할때 효과를 증가시키고, 오버헤드를 줄이는 작업이 필요하기도 하다. 
- 올바른 타입을 선택하는 것과 인스턴스에 대한 AZ를 선택하는 것은 네트워크 성능을 높여주고, placement group과 enhanced networking 과 같은 설정옵션을 통해 가능하다. 

<br/>

- 네트워크 처리량을 바이트로 측정하는 것과 더해 CloudWatch는 패킷이 전송/수신 되는 양을 메트릭으로 측정한다. 
- 패킷 메트릭은 기본 모니터링에서 가능하며 5분의 주기를 가진다. 

- NetworkIn/NetworkOut
  - 인스턴스에 의해서 모든 네트워크 인터페이스 상에 전송/수신된 바이트수이다. 
  - Resource Utilization 지표
- NetworkPacketIn/NetworkPacketOut
  - 인스턴스에 의해서 모든 네트워크 인터페이스 상에 전송/수신된 패킷 수이다. 오직 5분 주기의 수집만 가능하다. 
  - Resource Utilization 지표

#### NetworkIn/Out

- 이 메트릭은 네트워크 처리량을 바이트로 나타낸다. 
- 하락 혹은 플럭은 가능한 문제를 정확히 찾아내기 위해 다른 애플리케이션 수준 메트릭과 상호 연관될 수 있다. 
- 애플리케이션의 요구 사항과 심각하게 일치하지 않는 한 인스턴스가 네트워크 처리량 제한에 접근할 가능성은 낮지만 인스턴스가 수요를 충족하는지 확인하기 위해 가능한 네트워크 포화 상태를 주시하는 것은 여전히 도움이 될 수 있다. 
- 예를 들어 많은 양의 데이터를 빠르게 복원하거나 백업하고자 하는 경우 확인이 가능하다. 
- 혹은 특정 인스턴스들이 다른것보다 더 많은 네트워크 트래픽을 수신하기를 고려한다면 분산 트래픽을 더 효과적으로 사용하기 위해서 로드밸런서를 이용할 수 있다. 

### CPU 메트릭 

- EC2 인스턴스 타입은 넓은 범위의 vCPU 설정을 가진다. 
- CPU 사용을 트래킹 하는 것은 인스턴스들이 워크로드에 적합한지 확인하는데 사용된다.ㅏ 
- CloudWatch 는 인스턴스의 처리 능력을 퍼센트로 사용성을 측정한다. 
- AWS레이블에서 "Compute units" 로 되어 있다. 
- 이는 인스턴스가 호스팅되는 기본 하드웨어 CPU사용량은 보고하지 않는다. 

<br/>

- T2인스턴스들은 버스팅 능력을 가진다. 혹은 짧은 시간동안 표준 베이스라인 위로 처리 파워를 제공한다. 
- 이는 이상적인 어플리케이션으로 일반적으로는 CPU 인텐시브하지 않다. 그러나 짧은 주기로 높은 CPU 용량이 필요한경우 이점이 있다. 
- T2인스턴스 문서를 확인하고 인스턴스 타입에 대해서 상세히 확인하자. 

- CPUUtilization
  - EC2 계산에 할당되어 있는 퍼센트로 인스턴스에서 현재 사용하고 있는 단위이다. 
  - Resource Utilization 타입
- CPUCreditBalance
  - 인스턴스가 누적한 CPU 크레딧 수이다.
  - Resource Utilization 타입
  - T2 인스턴스에 대해서만 가능
- CPUCreditUsage
  - 소비된 CPU크레딧 수
  - Resource Utilization 타입
  - T2 인스턴스에 대해서만 가능
- CPUSurplusCreditBalance
  - CPU 크레딧이 0에 근접한 이후 소비된 크레딧수
  - Resource Utilization 타입 
  - T2 Unlimited 인스턴스 타입에서만 가능
- CPUSurplusCreditsCharged
  - 획득한 CPU 크레딧으로 상쇄되지 않고 비용이 발생할 잉여 크레딧 수: CPUSurplusCreditBalance - CPUCreditBalance
  - Resource Utilization 타입 
  - T2 Unlimited 인스턴스 타입에서만 가능 

#### CPUUtilization

- CPU 사용성량은 모니터링할 주요 호스트 수준 메트릭중 하나이다. 
- 애플리케이션에 따라 지속적으로 높은 활용도 수준이 정상일 수 있다. 
- 그러나 성능이 저하되거나 어플리케이션이 디스크 I/O, 메모리, 와 관련이 없거나, 네트워크 리소스에 의해 제한되지 않는 경우 최대 CPU가 리소스 병목 현상 또는 응용프로그램 성능 문제를 나타낼 수 있다. 
- 애플리케이션 수준 지표를 살펴보거나 추적을 요청하여 CPU 포화의 원인을 진단하거나 vCPU 가 더 많은 인스턴스 유형으로 전환할 수 있다. 
- 예를 들어 인스턴스가 버스팅 되는 경우 프로세싱 파워의 증가가 CPU 크레딧의 비용을 사용하면서 처리 능력이 향상된다. 
- EC2의 CPU 크레잇 메트릭은 가용한 잔고를 확인하고, 사용량을 추적하는데 도움이 되며, 확장 버스팅의 결과로 발생할수 있는 요금을 알 수 있다. 

#### CPU credit balance 

- 표준 T2 인스턴스 버스팅을 이용하면 버스트는 CPU 크레딧이 있는 동안 지속적으로 버스팅을 이용할 수 있다. 
- 모니터를 위해 중요한 점은 인스턴스 잔고이다. 
- 크레딧들은 인스턴스가 기준 CPU 성능 수준 이하로 실행될 때마다 적립된다. 
- 초기 잔액, 발생 비율 및 최대 가능 잔액은 모두 인스턴스 수준에 따라 다르다. 

#### CPU credit usage

- 하나의 CPU 크레딧은 CPU사용율이 100%인 경우 1분 (또는 50%인 경우 2분 등)에 해당한다.
- 인스턴스가 해당 인스턴스 유형의 기준보다 높은 CPU 성능을 요구할 때마다 수요가 줄어들거나 크레딧 잔고가 소진될 때까지 CPU크레딧을 소모하여 버스트 한다. 
- 인스턴스 크레딧 사용량을 주시하면 CPU 집약적 워크로드에 최적화된 인스턴스 유형으로 전환해야하는지 여부를 식별하는데 도움을 줄 수 있다.
- 혹은 CPU사용량이 기준선 이상으로 유지되는 동안 크레딧 잔고가 임계값 아래로 떨어질 때 경로를 생성할 수 있다. 

#### CPU surplus credit balance

- T2 Unlimited 인스턴스 케이스에서 CPU크레딧 밸런스가 소진된경우 그러나 버스트 성능의 유지가 필요한경우 인스턴스는 추가적인 크레딧을 소비가 필요하고 더 많은 CPU사용량을 유지하기 위해 추가 크레딧을 소비한다. 
- 이 메트릭은 누적 잔액을 추적한다.

#### CPU surplus credits charged

- 이 메트릭은 누적된 크레딧 수와 잉여 잔액을 지불하는 데 사용할 수 있는 현재 크레딧 잔액 간의 차이를 추적한다.
- 다른 말로 추가 요금이 발생하는 추가 크레딧의 척도이다.

### Status checks

- EC2 상테 체크는 단순하게 말해서 개별 인스턴스와 이를 호스팅 하는 AWS 시스템의 상태를 확인하는 것이다. 상태 확인은 1분 간격으로 제공된다. 
- 이는 인스턴스의 상태와 더 큰 AWS 인프라 또는 인스턴스 자체의 소프트웨어 또는 네트워크 구성에 문제가 있는지 여부에 대한 명확하고 높은 수준의 표시를 제공한다. 

- StatusCheckFailed_System
  - 만약 인스턴스가 EC2시스템 상태 체크에서 실패하면 1을 반환한다. 
  - Resource: Availability 지표
- StatusCheckFailed_Instance
  - 만약 인스턴스가 EC2시스템 상태 체크에서 실패하면 1을 반환한다. 
  - Resource: Availability 지표

#### Metric to watch: Status check failed - system

- 이 상태 체크 리포트는 호스팅 인스턴스 시스템에 문제가 발생하는 경우 발견된다. 
- 일반적으로 이는 인스턴스가 호스팅 되고 제어 할 수 없는 Amazon 관리 컴퓨터의 문제이다. (예: 정전등)
- 가능한 솔루션들은 정지와 인스턴스 재시작을 통해서 새로운 호스트로 교체하는 것을 포함한다. 
- 기억할 것은 인스턴스 스토어-볼륨은 인스턴스가 종료되면 유실된다는 것이다. 
- 만약 인스턴스가 시스템 상태 체크에서 성공하면 False(0)을 반환하고, 실패하면 True(1)을 반환한다. 

#### Metric to watch: Status check failed - instance

- 인스턴스 자체의 문제가 발견된경우인지 검사하는 것으로 만약 인스턴스 상태 체크에서 성공하면 False(0)을 반환하고, 실패한경우 True(1)의 값을 반환한다. 
- 이 문제들은 소프트웨어 혹은 네트워크 설정 이슈, 파일시스템의 깨짐 등등을 포함하여 체크하게 된다. 
- [Amazon troubleshotting tips](http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/TroubleshootingInstances.html) 에서는 공통적인 에러에 대해서 원인가 가능한 솔루션을 제공한다. 

### Events

- Events 들은 인스턴스 라이프사이클에서 변경에 대해 스케줄 된다. 
- AWS는 문제가 검출되거나 인스턴스 호스트에 대해서 표준 관리가 필요한 경우에 이벤트가 시작 된다. 
- Event가 포함하는 것
  - 인스턴스 정지: 
    - 이는 오직 EBS 지원 인스턴스에 적용된다. 
    - 이는 자신의 데이터를 유지할 수 있고 재시작이 가능하다. 
    - 재시작되면 인스턴스는 새로운 컴퓨터에서 호스팅된다. 
  - 인스턴스 폐기:
    - 이는 인스턴스를 종료하고 연결된 볼륨도 삭제된다. 
  - 인스턴스 재부팅: (다시 오직 EBS 지원 인스턴스에 적용) 혹은 호스트 컴퓨터
  - 시스템관리, 인스턴스의 성능 혹은 가용성에 영향을 준다. 
- AWS는 인스턴스에 대한 이벤트가 예약된 경우 사용자에게 알린다. 
- 그러나 CloudWatch 의 이벤트 스트림을 사용하여 이벤트를 추적하고 성능을 저하시키거나 데이터 가용성에 영향을 미칠 수 있는 EC2인프라에 대한 예정된 변경사항을 모니터링 할 수 있다. 
- 인스턴스 스토어 볼륨이 EBS 지원 인스턴스에 연결되어 있더라도 해당 볼륨에 저장된 모든 데이터가 손실되기 때문에 특히 중요하다. 
- EC2 이벤트를 주시하면 현재 인스턴스가 종료되거나 중지되기 전에 데이터를 새 인스턴스로 마이그레이션 해야 하는지 여부를 결정하는 데 도움이 된다. 

### Memory metrics

- 많은 사용 케이스에서 크고, 높은 성능의 데이터베이스 그리고 인메모리 어플리케이션에서 메모리 메트릭은 인프라 스트럭쳐에서 인프라를 주시하고 문제 및 성능 병목 현상을 식별하는 데 특히 중요하다. 
- 그러나 아마존 클라우드워치는 인스턴스에 대한 시스템 레벨의 메모리 메트릭을 리포팅 하지 않는다. 
- 추가적으로 메트릭은 [파트2](https://www.datadoghq.com/blog/collecting-ec2-metrics) 에서 EC2 인프라의 리소스 사용량에 대한 완전한 가시성을 확보하기 위해 메모리 지표를 수집하는 방법을 다룬다. 
