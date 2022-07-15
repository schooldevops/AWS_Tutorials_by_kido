# Monitoring best practices with Amazon ElastiCache for Redis using Amazon CloudWatch

from: https://aws.amazon.com/ko/blogs/database/monitoring-best-practices-with-amazon-elasticache-for-redis-using-amazon-cloudwatch/

- 모니터링은 신뢰성, 사용성, 리소스의 성능관리에 매우 중요한 파트이다.
- 여기에서는 어떻게 Redis의 상태를 관리하고, Amazon Cloud Watch 를 이용하여 캐시가 망가지는 것을 방지하고, 다른 외부 툴을 사용하는 것에 대해서 알아본다. 
- 또한 확장 요구사항을 확인하고 예측하는 방법에 대해서 논의한다. 

## Benefits of using CloudWatch with ElastiCache

- ElastiCache 는 CloudWatch 를 통해서 향상된 가시성을 제공한다. 
- 이때 핵심 성능 지표를 리소스와 연관하여 알아내게 된다. 
- 추가적으로 CloudWatch 는 메트릭의 임계치를 설정하고 넘어서면 알람을 보내거나, 이를 방지하고자 하는 액션이 필요할때 노티를 보내는 역할을 한다. 

<br/>

- 시간의 흐름을 모니터링 하는 것은 워크로드가 성정하는 것을 검출할 수 있도록 해준다. 
- 데이터포인트는 최대 455일(15개월) 동안 사용할 수 있고, CloudWatch 메트릭의 확장된 시간 범위를 관찰하여 얻은 정보는 리소스 사용성에 대한 예측에 도움을 준다. 

## Monitoring your resources

- Elasticache redis의 헬스 상태는 CPU, memory, 그리고 네트워크영역에서 핵심 컴포넌트의 사용성에 의해서 결정된다. 
- 이 값들이 임계값을 넘어가면 latency를 증가시키거나, 전체 성능저하를 일으키게 된다. 
- 이 값들이 낮은 사용성을 나타낸다면 다른말로 리소스를 오버프로비져닝하면 코스트 최적화가 필요하게 된다. 

<br/>

- Elasticache 는 메트릭을 제공하여, 클러스터 모니터링을 수행하고, 쓰기에 대한 처리등을 모니터링 하기 위해서 18가지 클라우드 워치 메트릭을 확인할 수 있다. 
- https://aws.amazon.com/about-aws/whats-new/2020/06/announcing-new-cloudwatch-metrics-elasticache-redis/ 

<br/>

- 클라우드 워치 메트릭은 Elasticache 에서 2가지 카테고리로 나눠진다. 
- 엔진 레벨 메트릭 (이는 Redis INFO command로 생성한다.) https://redis.io/commands/info/
- 호스트 레벨 메트릭 (이는 Elasticache 노드의 운영체제로 부터 얻을 수 있다.)
- 이 메트릭들은 60초 간격으로 측정되고 퍼블리싱 된다. 
- 비록 클라우드 워치가 각 메트릭에 대한 주기와 통계를 선택할 수 있지만, 모든 조합이 유용하지는 않다. 
- 예를 들어 CPU의 평균, 최소, 최대 값은 유용하다. 
- 그러나 이 통계값의 합은 의미가 없다. 

## CPUs

- Redis는 스냅샷이나 UNLINK와 같은 부수 작업에는 서로 다른 CPU를 이용할 수 있다. 
- 명령을 실행할때에는 단일 쓰레드로 수행된다. 
- 다른말로 한번에 하나의 프로세스가 실행된다. 

<br/>

- 왜냐하면 Redis는 단일 쓰레드로 수행되기 때문이다. 
- Elasticache는 EngineCPUUtilization 메트릭을 제공하여 Redis가 처리하는 로드에 대해서 더욱 정교한 가시성을 제공한다. 
- 그리고 Redis워크로드에 대해서 더욱 명확하게 해준다. 

<br/>

- 높은 EngineCPUUtilization 에 대한 허용 오차는 모든 사용 사례에 따라 다르며 보편적인 임계값은 없다. 
- 그러나 모범사례로 EngineCPUUtilization이 항상 90% 미만인지 확인하라. 

<br/>

- 클러스터를 벤치마킹하는 것은 어플리케이션을 이용하고, 기대하는 워크로드를 이용하는 경우 EngineCPUUtilization과 성능의 상관 관계를 파악하는데 도움이 될 수 있다. 
- EngineCPUUtilization에 대해 여러 CloudWatch경보를 설정하는 것이 좋다. 
- 그러면 각 임계값이 충족될 때 (예: 65% WARN, 90% HIGH)성능에 영향을 미치기 전에 알림을 받을 수 있다. 

<br/>

- 만약 클러스터에서 EngineCPUUtilization 이 높으면 다음 스텝으로 이를 해결할 수 있다. 
  - 높은 EngineCPUUtilization 메트릭은 특정 Redis operation에 의해서 발생될 수 있다. Redis 커맨드들은 시간 복잡도가 Big O노테이션을 이용하여 정의된다. 또한 Redis SLOWLOG(https://redis.io/commands/slowlog) 를 이용하여 커맨드가 완료되기까지 시간이 오래 걸린 정도를 확인할 수 있다. Redis KEYS 명령을 과도하게 사용하는 것이 그 예이며 프로덕션 환경에서는 각별히 주의해서 사용해야한다. 
  - Redis 명령 시간 복잡성과 관련하여 최적화되지 않은 데이터 모델은 불필요한 EngineCPUUtilization을 유발할 수 있다. 예를 들어 집합의 카디널리티는 성능 요소일 수 있으며, SMEMBERS, SDIFF, SUNION 및 기타 집합 명령은 집합의 요소 수에 의해 정의된 시간 복잡도를 가진다. 해시의 크기 (필드의 수) 그리고 오퍼레인션의 타입은 또한 EngineCPUUtilization에 영향을 준다. 
  - 만약 Redis 를 노드그룹 (하나 이상의 노드가 있는) 에서 수행되는 경우, 복제본을 사용하여 스냅샷을 생성하는 것이 좋다. 복제에서 스냅샷이 생성되는 동안 메인 노드는 이 태스크에 의해서 영향을 받지 않는다. 그리고 성능저하 없이 요청을 처리할 수 있다. 노드가 SaveInProgress로 스냅샷을 생성하는지 확인하라. 완전한 동기화 케이스에서는 스냅샷은 항상 메인 노드에 존재한다. 더 많은 정보를 위해서 PerformanceImpact of Backup (https://docs.aws.amazon.com/AmazonElastiCache/latest/red-ug/backups.html#backups-performance) 를 확인하라. 
  - 높은 볼륨의 오퍼레이션 또한 높은 EngineCPUUtilization 을 유발한다. 부하를 유발하는 작업 유형을 찾아라. 읽기 작업이 대부분 높은 EngineCPUUtilization을 유발하는 경우 읽기 작업에 기존 읽기 전용 복제본을 사용하고 있는지 확인하라. 이는 ElastiCache reader endpoint 를 이용하며 클러스터 모드를 끄고 Redis 클라이언트 라이브러리를 설정이 필요하다. 혹은 Redis READONLY command 를 클러스터 모드로 이용하라. 만약 이미 Read replica로 부터 읽고 있다면 추가적인 노드를 넣고 (5개의 읽기 복제본으로 올린다.) 복제 그룹에서 혹은 각 샤드에서 읽기를 증가 시킨다. 만약 쓰기 오퍼레인션들이 높은 EngineCPUUtilization 을 올리고 있다면 메인 노드에 성능을 증가 시킬 필요가 있다. 이는 가장 최근의 m5나 r5노드로 업그레이드 할 수 있다. 그리고 최신의 처리 기술을 이용할 수 있다. 만약 이미 가장 마지막 노드 세대를 이용하고 있다면 클러스터 모드 disable에서 cluster mode enable로 변경하는 것을 고려할 수 있다. 이 마이그레이션을 달성하기 위해서 존재하는 클러스터의 백업을 생성하고, 새로운 클래스터 사용 모드 클러스터로 이관하라. 클러스터모드가 사용되면 솔루션은 더 많은 샤드를 넣거나 스케일 아웃을 할 수 있다. 더 많은 샤드를 추가하는 것으로 더 많은 메인 노드들 그리고 결과적으로 더 많은 컴퓨팅 파워를 사용할 수 있다. 

<br/>

- Redis 처리의 로드를 모니터링은 EngineCPUUtilization 메트릭이 핵심이다. 그러나 또한 남아있는 CPU코어를 확인할 수 있다. CPUUtilization을 통해서 전체 호스트에 대한 CPU 사용이 퍼센트를 모니터링 할 수 있다. 예를 들어 연결 설정하는 데 일부 처리가 필요하기 때문에 새로운 연결 볼륨이 높으면 CPUUtilization 메트릭이 높아질 수 있다.

<br/>

- 작은 노드들을 위해서 2개 혹은 몇개의 CPU 코어를 가지는 경우 CPUUtilization 모니터링은 필연적이다. 왜냐하면 스냅샷과 같은 오퍼레이션 그리고 유지 관리 이벤트와 같은 작업에는 컴퓨팅 용량이 필요하고 노드의 CPU코어를 Redis와 공유해야 하기 때문에 CPUUtilization은 EngineCPUUtilization전에 100%에 도달할 수 있다. 

<br/>

- 마지막으로 Elasticache 는 T2, T3 캐시 노드를 제공한다. 
- 이는 크레딧이 소진될 때까지 언제든지 CPU 사용량을 버스트할 수 있는 기능과 함께 기본 수준의 성능을 제공한다. 만약 T2혹은 T3 캐시 노드를 이용하고자 한다면 CPUCreditUsage와 CPUCreditBalance를 함께 봐야한다. 왜냐하면 성능은 기준 수준으로 점차 낮아지기 때문이다. 더 많은 정보를 위해서 https://docs.aws.amazon.com/AWSEC2/latest/UserGuide/burstable-credits-baseline-concepts.html 를 참조하라. 

## Memory

- 메모리는 Redis의 핵심 관점이다. 메모리 사용성에 대해서 이해하는 것은 데이터 유실을 피하고, 데이터 세트의 증가를 수용하려면 클러스터의 메모리 사용율을 이해해야한다. 
- 메모리 사용율에 대한 통계는 Redis INFO 명령의 메모리 섹션에서 확인할 수 있다. (https://redis.io/commands/INFO)
- 가장 중요한 메트릭은 used_memory이다. 할당자를 사용하여 Redis가 할당한 메모리 입니다. 
- CloudWatch 는 used_memory에서 파생된 BytesUsedForCache라는 지표를 제공한다. 이 지표를 사용하여 클러스터의 메모리 사용율을 확인할 수 있다. 
- 18개의 추가적인 CloudWatch 메트릭으로 DatabaseMemoryUsagePercentage를 이용할 수 있다. 그리고 메모리 활용의 퍼센테이지를 볼 수 있고, 이는 현재 메모리 사용을 기반으로 한다. (BytesUsedForCache) 그리고 maxmemory를 볼 수 있다. 
- Maxmemory 셋은 데이터셋의 메모리 최대 량을 설정하며, 클러스터의 maxmemory 는 Redis INFO command 와 Redis Node-Type Specific Parameters 의 메모리 섹션에서 확인할 수 있다. 기본값은 reserved memory 주제에 있다. 이러한 이유로 maxmemory 가 줄어들게 된다. 예를 들어 cache.r5.large 노드 타입은 기본 maxmemory(14037181030 byte) 를 가진다. 그러나 만약 기본 25% 예약 메모리를 이용한다면 가용한 maxmemory는 (10527885772.5 byte) 이다. 

<br/>

- DatabaseMemoryUsagePercentage 가 100%에 달하면 Redis maxmemory policy 가 트리거 되며,선택한 정책 (예: volatile lru)에 따라 제거가 발생할 수 있다. 만약 캐시내에 제거에 적합한 개체가 없는 경우 (제거 정책 일치), 쓰기 작업이 실패하고 Redis 주요 노드는 다음 메시지를 반환한다. 

```go
(error) OOM command not allowed when used memory > 'maxmemory'
```
- Evictions 이 반드시 문제나 성능저하를 나타내지는 않는다. 
- 몇몇 워크로드는 제거를 필요로하거나, 의존한다. 
- 클러스터의 제거 볼륨을 모니터링 하기 위해서 Evictions 메트릭을 사용할 수 있다. 
- 이 메트릭은 또한 Redis INFO command 로 부터 도출되었으며 높은 용량의 제거 작업이 일어나면 EngineCPUUtilization이 높게 나타난다. 

<br/>

- 워크로드가 축출을 경험하도록 설계되지 않은 경우 권장되는 접근 방식은 다양한 수준의 DatabaseMemoryUsagePercentage에서 CloudWatch 경보를 설정하여 필요한 조정 작업을 수행하고 더 많은 메모리 용량을 프로비저닝 해야 할 때 사전에 알림을 받는 것이다 .
- 클러스터 모드 비활성화인경우 스케일업은 더 많은 메모리를 사용할 수 있도록 확장할 수 있다. 그러나 클러스터 모드 enable 된 경우 스케일 아웃으로 점진적으로 메모리 용량을 확대할 수 있다. 이것은 가장 적합한 솔루션이 된다. 

<br/>

- 다른 방법은 데이터셋의 증가를 컨트롤 하는 것은 TTL을 두는 것이다. 
- TTL이 종료되면 키는 더이상 서비스하지 못하고, 만약 클라이언트가 접근을 시도하면 Redis는 삭제되며 이는 패시브 모드이다. 혹은 램덤 키를 주기적으로 테스트하여 삭제하는 액티브 모드가 있다. 
- Redis SCAN 커맨드를 이용할 수 있으며 이는 데이터셋의 부분을 파싱하고 수동 방법을 증폭하여 만료된 키를 삭제할 수 있다. 
- ElastiCache 에서 키 만료는 Reclaimed CloudWatch 메트릭에서 확인이 가능하다. 

<br/>

- 백업 혹은 페일오버가 수행되면 Redis는 추가적인 메모리를 이용하여 쓰기 오퍼레이션을 클러스터에 적재할 수 있다. 클러스터의 데이터는 .rdb파일에 저장된다. 만약 추가적인 메모리를 노드가 사용할 수 있는 용량을 넘어서면, 처리는 느려지며, 페이징이 증가되고, SwapUsage가 늘게 된다. 
- 이것은 왜 우리가 예약 메모리를 이용하는지 알 수 있는 부분이다. 예약 메모리는 백업이나 페일오버와 같은 편의 기능을 위해 사용한다. 더 많은 정보는 https://docs.aws.amazon.com/AmazonElastiCache/latest/red-ug/redis-memory-management.html 를 참조하자. 

<br/> 

- 마지막으로 CloudWatch 알람 구현을 추천한다. 이를 통해 SwapUsage를 수행한다. 
- 이 메트릭은 50MB를 초과할수 없다. 만약 클러스터가 swap를 이용하면 클러스터의 파라미터 그룹에서 검증하라. 

## Network

- 클러스터의 네트워크 대역폭 용량을 결정하는 요소 중 하나는 선택한 노드 유형이다. 노드의 네트워크 용량에 대해서는 https://aws.amazon.com/elasticache/pricing/ 을 참조하라.

<br/>

- 성능을 평가하고 모니터링에서 올바른 임계값을 설정하기 위해 프로덕션으로 이동하기 전에 클러스터를 벤치마킹 하는 것이 좋다. 임시 네트워크 버스팅 용량의 잠재적 사용을 반영하려면 벤치마크를 몇 시간 동안 실행해야한다. 

<br/>

- Elasicache 와 CloudWatch 는 몇가지 호스트 레벨의 메트릭을 제공한다. 이를 통해 네트워크 사용을 모니터링 할 수 있다. 이는 EC2 인스턴스와 유사하다. 
- NetworkBytesIn 과 NetworkBytesOut 들은 네트워크로 부터 읽어들이고, 네트워크로 내보낸 바이트수를 측정한다. 
- NetworkPacketsIn과 NetworkPacketsOut 들은 네트워크를 통해서 읽고 보낸 패킷 수량을 나타낸다. 

<br/>

- 클러스터에 네트워크 용량을 선언한 후에 프로겍트에서 네트워크 사용중 기대되는 가장 높은 스파이크 설정을 기준으로 수행한다. 이 스파이크는 노드의 네트워크 용량 이상을 지정할 수 없다. 
- 각 노드 타입은 버스트 용량을 제공한다. 그러나 기대하지 않은 트래픽 증가에 대한 추가 용량을 유지하는 것을 추천한다. 

<br/>

- 정의한 가장 높은 사용성을 기반으로 클라우드 워치 알람을 생성할 수 있다. 이는 네트워크 사용성이 예상보다 높거나, 리밋에 근접하는 경우 메일을 보낸다. 

<br/>

- 만약 네트워크 사용성이 증가되고, 네트워크 알람을 트리거 하는경우 네트워크 용량을 늘리려면 필요한 조치를 취해야 한다. 
- 올바른 조취를 인식하기 위해서 무엇이 네트워크를 증가시키는지 식별할 필요가 있다. 
- CloudWatch 메트릭을 이용할 수 있으며 오퍼레이션의 증가를 인식하고, 읽기 쓰기 오퍼레이션 카테고리에서 이 증가를 구분한다. 

<br/>

- 만약 네트워크 사용이 증가하는 것이 읽기 오퍼레이션에 의해서 발생한다면 우선 읽기 오퍼레이션에 대한 읽기 복제본을 확인하자. 비활성화된 클러스터 모드용 ElastiCache 리더 엔드 포인트 또는 활성화된 클러스터 모드용 Redis READONLY 명령을 사용하여 Redis 클라이언트 라이브러리에서 재 구성해야한다. 
- 만약 이미 읽기 복제본에서 읽었다면 추가적인 노드를 추가를 복제 그룹 혹은 샤드에 추가하는 것이 필요하다. 

<br/>

- 만약 쓰기 오퍼레이션들이 네트워크 사용성을 증가시키는 경우 primary node에 더 많은 용량을 추가할 필요가 있다. 클러스터 모드를 disable 되는 곳에서는 단순히 노드 타입을 스케일업 할 필요가 있다. 이는 클러스터 모드가 활성화 된경우라면 동일한 스케일업이 가능하다. 

<br/>

- 반면 클러스터 모드가 활성화 되면 읽기 쓰기 양쪽 케이스에 대해서 두번째 옵션을 제공한다. 이 옵션은 더 많은 샤드와 스케일 아웃을 고려하는 것이다. 더 많은 샤드를 추가하는 것으로 데이터셋은 더 많은 프라이머리 노드로 분산하고, 각 노드는 데이터셋의 작은 부분을 책임지며 이를 통해서 노드마다 더 작은 사용성을 유지할 수 있다. 

<br/>

- 수평확장은 대부분의 네트워크 관련 문제를 해결하지만 단축키와 관련된 극단적인 경우가 있다. 
- 바로가기 키는 비례하지 않고 자주 액세스되는 특정 키 또는 키의 하위 집합에 의해 구동되므로 개별 샤드가 다른 샤드 보다 더 많은 트래픽을 경험할 수 있는 시나리오가 된다. 
- 이 키들은 동일한 샤드에 남아 있는 경우 높은 네트워크 사용성을 생성한다. 이 드문 예에서 기존 데이터 세트를 변경하지 않고 확장하는 것이 더 낳은 옵션이다.
- 데이터 모델을 리팩토링 하면 네트워크 활용도를 재조정 하는 데 도움이 될 수 있다. 
- 예를 들어 스트링을 복제하고, 여러개의 엘리먼트를 저장하는 세그먼트 개체를 복제한다. 
- 모든 클러스터 샤드에서 가장 자주 액세스하는 키와 높은 네트워크 사용량을 분산하는 방법에 대한 자세한 내용은 Redis 클러스터 사양을 참조하라. 

## Connections

- CloudWatch 는 클러스터에 커넥션을 설립하기 위해서 2개의 메트릭을 제공한다. 
  - CurrConnections
    - 동시성 개수 그리고 액티브 커넥션은 Redis 엔진에 의해서 등록된다.
    - 이는 connected_clients 속성을 Redis INFO Command에서 도출된 것이다. 
  - NewConnections
    - 커넥션의 총 개수는 연결이 활성 상태인지 닫혀 있는지에 관계없이 지정된 기간동안의 수이다. 
    - 이 메트릭은 REdis INFO Command에서 도출된 것이다. 

- 커넥션을 모니터링 하기 위해서 기억할 것은 Redis는 maxclients라고 불리는 제한이 있다는 것이다. 
- ElastiCache의 기본과 변경 불가능 값은 65,000이다. 
- 다른 말로 65,000 동시 커넥션을 각 노드마다 설정할 수 있다. 

<br/>

- CurrConnections와 NewConnections 메트릭 둘다 이슈를 발견하고, 방지하는 도움을 줄수 있다. 
- 예를 들어 CurrConnections의 지속적인 증가는 가능한 커텍션 65,000 의 소진이 될 수 있다. 
- 이러한 유형의 증가는 응용 프로그램 측의 문제와 연결이 부적절하게 닫혀 서버 측에서 설정된 연결을 나타낼 수 있다. 
- 이 문제를 해결하려면 애플리케이션의 동작을 조사해야 하지만 클러스터가 tcp-keepalive 를 사용하여 잠재적인 데드 피어를 감지하고 종료하는지 확인할 수 있다. 
- tcp-keepalive 의 기본 시간은 300초이다. 이는 redis 3.2.4 이후 부터 적용되었다. 오래된 버젼의 tcp-keepalive는 기본적으로 idsable 되어 있다. 이 값은 parameter group을 통해서 조정할 수 있다. 

<br/>

- 모니터를 위한 중요한 부분은 NewConnections 이다. 그러나 maxclient 제한은 65,000 이지만 메트릭에 적용되지는 않는다. 왜냐하면 이는 특정 시간동안 총 커넥션 생성수이기 때문이다. 
- 이것은 연결이 동시적이라는 의미가 아니다. 한 노드는 1분 데이터 샘플동안 100,000 을 수신할 수 있으며, 2,000 개의 동시연결의 NewConnections 에 도달하지 않을 수 있다. 
- 이 특정 예에서 워크로드는 Redis가 처리할 수 있는 연결의 제한에 도달할 위험이 없다. 
- 빠르게 열리고 닫히는 많은 양의 연결은 노드의 성능에 영향을 줄 수 있다. TCP 커넥션은 몇 밀리 초가 소요되며, 이는 Redis 처리에서 추가적인 비용을 지불해야한다.

<br/>

- 베스트 프랙티스로 어플리케이션은 이미 연결된 커넥션을 재 사용하여, 새로운 커넥션을 생성하는 것을 피해야한다. Redis client 라이브러리를 통해서 커넥션 풀링을 구현하거나 처음부터 개발하거나 할 수 있다.

<br/>

- TLS 핸드쉐이크에 필요한 추가 시간과 CPU 사용율로 인해 클러스터가 ElastiCache 전송 중 암호화 기능을 사용할 때 새 연결의 볼륨을 제어하는 것이 훨씬 더 중요하다. 

## Replication

- 기본 노드는 하나 이상의 읽기 전용 복제본이 있는 경우 복제할 명령 스트림을 보낸다. 
- 데이터의 볼륨은 ReplicationBytes메트릭을 통해서 볼 수 있다.
- 비록 이 메트릭은 복제 그룹의 쓰기 로드를 나타내지만 복제 상태에 대한 통찰력을 제공하지 않는다. 
- 이 목적으로 ReplicationLag 지표를 이용할 수 있다. 이는 복제본이 기본 노드에서 얼마나 뒤쳐저 있는지에 대한 매우 편리한 표현을 제공한다. 
- Redis 5.0.6 부터 이 데이터는 밀리초 단위로 캡처된다. 
- 드물기는 하지만 복제 지연의 급증은 기본 노드 또는 복제본이 복제 속도를 따라갈 수 없음을 나타내므로 ReplicationLag 메트릭을 모니터링 하여 잠재적인 문제를 감지할 수 있다. 
- 이는 복제본이 전체 동기화를 요청해야 할 수 있음을 나타낸다. 
- 이는 기본 노드에서 스냅샷 생성을 포함하는 보다 복잡한 프로세스이며 성능 저하로 이어질 수 있다. 
- ReplicationLag 지표와 SaveInProgress 지표를 결합하여 전체 동기화 시도를 식별할 수 있다. 

<br/>

- 높은 복제 지연은 일반적으로 과도한 쓰기활동, 네트워크 용량 고갈 또는 근본적인 서비스 저하의 부작용이다. 

<br/>

- 클러스터 모드가 비활성화된 단일 기본 노드에 대해 쓰기 활동이 너무 높은 경우 활성화된 클러스터 모드로의 전환을 고려하고 쓰기 작업을 여러 샤드 및 관련 기본 노드에 분산해야한다.
- 만약 복제 지연이 네트워크 고갈로 인해 발생하는 경우 이 게시물의 네트워크 섹션에서 해결 단계를 따를 수 있다. 

## Latency

- 데이터 구조당 집계된 지연 시간을 제공하는 CloudWatch 지표 세트를 사용하여 명령의 지연 시간을 측정할 수 있다. 
- 이러한 지연 메트릭은 Redis INFO 명령의 commandstat 통계를 사용하여 계산된다. 
- 다음 차트에서 우리는 StringBasedCmdsLatency 메트릭을 볼 수 있다. 이는 평균 latency이며 밀리초 단위이다. 문자열 기반의 커맨드로 지정된 시간 범위동안의 커맨드이다. 

![latency](https://d2908q01vomqb2.cloudfront.net/887309d048beef83ad3eabf2a79a64a389ab1c9f/2020/10/09/Pic1Resized-1024x169.png)

- 이 latency 는 네트워크 I/O 시간은 포함되지 않는다. 이는 오직 Redis에 의해서 처리된 오퍼레이션들만 포함된다. 더 많은 정보를 위해서는 https://docs.aws.amazon.com/AmazonElastiCache/latest/red-ug/CacheMetrics.Redis.html 를 참조하자. 

- CloudWatch 메트릭이 툭정 데이터 구조에서 latency의 가리킨다면, Redis SLOWLOG를 이용할 수 있다. 이를 통해 런타임이 더 높은 정확한 명령을 식별할 수 있다. 

<br/>

- 애플리케이션에 높은 지연 시간이 발생하지만, CloudWatch 지표가 Redis엔진 수준에서 낮은 지연 시간을 나타내는 경우 네트워크 지연 시간을 조사해야한다. 
- Redis CLI 는 latency monitoring tool을 제공하며, 네트워크 혹은 어플리케이션의 이슈를 고립시키는데 도움을 줄 수 있다. (min, max, avg 등은 밀리초이다.)

```go
$ redis-cli --latency-history -h mycluster.6advcy.ng.0001.euw1.cache.amazonaws.com
min: 0, max: 8, avg: 0.46 (1429 samples) -- 15.01 seconds range
min: 0, max: 1, avg: 0.43 (1429 samples) -- 15.01 seconds range
min: 0, max: 10, avg: 0.43 (1427 samples) -- 15.00 seconds range
min: 0, max: 1, avg: 0.46 (1428 samples) -- 15.00 seconds range
min: 0, max: 9, avg: 0.44 (1428 samples) -- 15.01 seconds range
```

- 마지막으로 애플리케이션 성능에 영향을 미치고 처리시간을 증가시킬 수 있는 모든 활동에 대해 클라이언트 측을 모니터링 할 수 있다. 

## ElastiCache events and Amazon SNS

- 엘라스틱 로그 이벤트는 리소스와 관련이 있다. 이는 failover, node replacement, scaling operation, scheduled maintenance 등
- 각 이벤트는 날짜와 시간, 소스 이름과 소스 유형, 설명이 포함된다.
- ElastiCache 콘솔에서 또는 Amazon 명령줄 인터페이스 (AWS CLI) describe-events 명령 및 ElastiCache API 를 사용하여 이벤트에 쉽게 액세스할 수 있다. 

- 다음 스크린 샷은 ElastiCache 콘솔에 이벤트 로그를 보여준다. 
  
![sns](https://d2908q01vomqb2.cloudfront.net/887309d048beef83ad3eabf2a79a64a389ab1c9f/2020/10/09/Pic2Resized-1024x294.png)

- 이벤트 모니터링 하면 클러스터의 현재 상태를 파악하고 이벤트에 따라 필요한 조치를 취할 수 있따. 
- ElastiCache 이벤트는 이미 언급한 다양한 구현을 통해 사용할 수 있지만 Amazon Simple Notification Service(Amazon SNS) 를 사용하여 중요한 이벤트에 대한 알림을 보내도록 ElastiCache를 구성하는 것이 좋다. 
- 더 많은 정보를 위해서는 https://docs.aws.amazon.com/AmazonElastiCache/latest/red-ug/ECEvents.SNS.html 룰 참조하자. 

<br/>

- ElastiCache 클러스터에 SNS 토픽을 추가하면, 모든 중요한 이벤트들이 SNS토픽으로 퍼블리시 된다. 또한 메일로 받을 수 있다. 
- ElastiCache 이벤트에 대해서 Amazon SNS에 대해서는 https://docs.aws.amazon.com/AmazonElastiCache/latest/red-ug/ElastiCacheSNS.html 를 참조하자. 

<br/>

- Amazon SNS 를 이용하는 것은 프로그램적으로 ElastiCache 이벤트를 이용할 수 있다. 
- 예를 들어 AWS Lambda 함수는 SNS 토픽을 수신 받을 수 있다. 
- 더 많은 정보를 위해서 https://aws.amazon.com/blogs/database/monitor-amazon-elasticache-for-redis-cluster-mode-disabled-read-replica-endpoints-using-aws-lambda-amazon-route-53-and-amazon-sns/ 를 참조하자. 

## Summary 

- 이 포스트에서 우리는 가장 일반적인 ElastiCache Redis 모니터링의 챌린지에 대해서 논의했다. 
- 이 게시물을 통해 얻은 지식을 바탕으로 이제 정상적인 ElastiCache Redis 리소스를 감지, 진단 및 유지관리할 수 있다. 
  