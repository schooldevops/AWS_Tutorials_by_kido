# ECS를 위한 기본 지식 Overview

- ECS는 Elastic Container Service 의 약자
- ECS는 컨테이너 (Docker가 가장 잘 알려져 있는 컨테이너)를 관리하고 서비스 해주는 AWS 제품

## ECS 아키텍처

![ecs](imgs/ecsproductpage.png)

- Amazon Elastic Container Registery (ECR)
  - 이미지를 빌드하고 ECR에 등록하여 사용할 수 있다. 
- Amazon Elastic Container Service (ECS)
  - 컨테이너를 매니지 하고 컨테이너 서비스를 관리한다. 
- 주요 업무
  - Define application
    - 어플리케이션을 정의한다. 
    - 이때 어떠한 이미지를 사용할지, 어떤 리소스가 필요한지등을 기술한다. 
  - ECS 2가지 타입
    - Amazon EC2
      - EC2를 이용하여 컨테이너 서비스를 런치할 수 있다. 
    - AWS Fargate
      - Fargate는 Serverless 로 직접 서버를 관리하지 않고 컨테이너를 런치할 수 있다. 
  - Manage container
    - ECS의 주요 기능으로 어플리케이션 수평 확장 (스케일링)
    - 컨테이너의 가용성을 관리한다. 

## Container 이해

### Container

- 컨테이너는 표준화된 방법으로 어플리케이션을 패키징한다. 이때 코드, 설정, 의존성 라이브러리, 어플리케이션등을 하나의 객체로 관리할 수 있도록 한다. 
- 컨테이너는 서버의 OS 자원을 공유하고, 리소스 격리 프로세스로 실행되어 특정 환경에 관계 없이 빠르고, 안정적이며, 일관된 배포를 지원한다. 
- 랩탑이든 실제 제품 서버이든 어플리케이션 실행 관경은 항상 동일하게 유지한다. 
- 보안, 환경변수등만 개발/스테이지/운영등으로 다르게 처리하고, 실행환경은 동일하게 유지할 수 있어 일관된 서비스 환경을 지원한다 

<br/>

- 컨테이너의 이점
  - 컨테이너는 어플리케이션 코드, 설정, 의존성 라이브러리 등을 쉽게 하나의 객체로 패키지 할 수 있다. 
  - 동일한 실행환경과 Docker Runtime만 존재하면 언제 어디서든지 어플리케이션을 실행할 수 있다. 
  - 컨테이너를 활용하면 쉽게 동일한 시스템 사양으로 어플리케이션을 스케일 아웃 할 수 있다. 
  - 빠른 개발/빌드/테스트/실행을 지원한다. 
  - 시행시 워크로드를 다른 워크로드와 격리 가능하다. (즉, 하나의 머신에 여러 컨테이너를 각각 독럽적으로 실행이 가능하다.)

### Docker

- Docker 는 어플리케이션을 빌드, 테스트, 배포를 빠르게 도와주는 소프트웨어 플랫폼이다. 
- 도커는 컨테이너라고 부르는 표준화된 유닛으로 패키지 해주고, 라이브러리, 시스템 도구, 코드, 실행환경등을 포함하여 동일하게 수행해준다. (Window, Linux, macOS 등에 상관없이 수행된다.)

## ECS의 이해

- ECS (Amazon Elastic Container Service) 는 높은 확장성, 고성능 컨테이너 오케스트레이션 서비스이다. 
- Docker 와 같은 컨테이너를 쉽게 실행하게 해주고, AWS상에서 Scale Out/In이 가능하다. 
- ECS를 사용하면, 물리적인 머신을 공수하고, 운영체제의 설치와 소프트웨어의 설치등의 작업 없이 쉽게 확장이 가능하다. 
- 또한 컨테이너의 스케일 아웃/인등을 손쉽게 수행할 수 있다. 
- 다양한 AWS의 리소스와 통합 사용할 수 있다. 

### ECS Cluster

- ECS cluster는 태스크와 서비스들의 논리적인 그룹이다. 
- 클러스터의 런치 타입은 EC2와 Fargate가 있다. 이들을 각각 혹은 혼합해서 클러스터 구성이 가능하다.
- 하나의 계정에 단일 혹은 여러개의 클러스터를 생성하여 독립적으로 운영이 가능하다. 

### Task 정의

- Amazon ECS상에서 어플리케이션을 수행하기 위해서 작업정의 (Task Definition) 을 우선 해야한다. 
- 작업 정의는 텍스트 파일 형식이며, JSON 포맷으로 작성이 된다. 
- 작업 정의에는 하나 혹은 여러개의 컨테이너를 기술하고, 수행되어야할 어플리케이션 수, 리소스 용량등을 설정한다. 

- 작업 정의에는 또한 어플리케이션이 취하는 다양한 파라미터를 기술할 수 있다. 

- Task Definition 샘플

```json
{
    "family": "webserver",
    "containerDefinitions": [
        {
            "name": "web",
            "image": "nginx",
            "memory": "100",
            "cpu": "99"
        }
    ],
    "requiresCompatibilities": [
        "FARGATE"
    ],
    "networkMode": "awsvpc",
    "memory": "512",
    "cpu": "256"
}
```

- containerDefinitions 영역에서 컨테이너가 필요한 이미지, 메모리, CPU 등에 대한 리소스 정의를 수행한다. 
- requiresCompatibilities 영역은 ECS 런치타입이 FARGATE라는 것을 알 수 있다.
- 사용할 VPC, 메모리, CPU 등에 대한 리소스를 기술하고 있다. 
- 상세 정보는 [Task Definitions](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/example_task_definitions.html) 을 참조하자. 

### Task와 스케줄링

- 태스크는 Task Definition의 인스턴스화된 객체를 나타낸다. 
- 태스크 정의를 완료한 후, 어플리케이션은 ECS 클러스터에 제출하면 태스크라는 인스턴스가 수행된다. 
- Fargate 런치타입을 사용하는 태스크는 자체 격리 바운더리가 존재하며, 커널, CPU, 메모리, 네트워크를 다른 태스크와 공유하지 않는다. 
- 스케줄러는 클러스터 내에 작업을 배치하는 역할을 수행한다. 
- [Schedule 관련사항 보기](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/scheduling_tasks.html)

### Services

- 서비스는 Amazon ECS 클러스터에서 지정된 수의 작업 정의 인스턴스를 동시에 실행하고 유지할 수 있도록 하는 것을 말한다. 
- 태스크가 특정한 이유로 정지되거나 런치 실패가 되는 경우라면 스케줄러의 정책에 따라 다른 인스턴스에 서비스를 런치할 수 있다. 
- 서비스에서 원하는 작업 수를 유지하는 것 이외에도 선택저긍로 로드 밸런서 뒤에서 서비스를 실행할 수 있다. 
- 로드 밸런서는 관련된 서비스로 부하를 분산 시킨다. 

#### 서비스 스케줄러 전략

- REPLICA
  - 리플리카는 클러스터 상에서 수행되어야할 태스크의 수를 관리하고, 배치한다. 
  - 기본적으로 서비스 스케줄러는 멀티 AZ로 태스크를 분산해서 배치한다. 
  - [리플리카](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ecs_services.html#service_scheduler_replica)]
- DAEMON
  - daemon 스케줄링 전략은 하나의 활성 커네이너 인스턴스에 정확히 하나의 작업을 배포한다. 
  - 서비스 스케줄러는 배치 조건을 검사하고, 조건을 만족하지 못한 태스크를 정지 한다. 
  - 이 전략은 하나의 태스크만 수행하므로 원하는 수의 태스크 값이나, 태스크 배치 전략, Service Auto Scaling 정책이 필요하지 않다. 
  - [데몬](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/ecs_services.html#service_scheduler_daemon)

### Fargate

- Fargate는 ECS의 런치타입중 하나로 서버나 클러스터를 직접 관리하지 않아도 서비스를 실행할 수 있도록 한다. 
- Fargate를 활용하면 프로비저닝, 설정, 클러스터 확장 등의 작업을 수행하지 않아도 된다. 
- 또한 서버 타입, 클러스터 확장, 최적화등의 고민이 필요없다. 
- 관리 영역에서 디자인 영역으로 업무의 변화를 할 수 있도록 한다.

### Service Discovery

- 컨테이너는 기본적으로 불변의 객체이다. 
- 즉 문제가 발생하면 이를 교체하는 방법을 사용한다. 
- 이러한 특성으로 문제가 있는 서비스는 종료하고, 새로운 서비스로 교체하는 전략을 사용하고 있으며, 이러한 작업을 직접 구현하는 것은 어렵다. 
- 그러므로 서비스 디스커버리가 필요하다. 

<br/>

- AWS Cloud Map 은 서비스를 찾는 역할을 하는 AWS 리소스이다. 
- Cloud Map 을 이용하면 어플리케이션의 커스텀 이름을 설정하고, 리소스들의 동적인 변경에 따른 서비스의 위치를 갱신하는 작업을 수행한다. 
- 이를 통해 어플리케이션의 가용성을 증진할 수 있다. 

- Cloud Map 은 ECS와 통합하여 사용이 가능하다. 

![CloudMap](imgs/cloudmapproduct.png)

-  클라우드 맵이 없는 경우
   - Backend 서비스의 변경이 발생한경우 Frontend 서비스에서 직접 접속 엔드포인트를 변경해야한다. 
   - 안정적인 서비스를 유지하기 매우 어렵다.
 - 클라우드 맵을 사용한경우
   - Backend 서비스는 Cloud Map에 자신을 등록한다. 
   - Frontend 서비스는 Cloud Map에 조회하여 어떠한 Backend 서비스의 엔드포인트를 사용할지 결정한다. 
   - Backend 서비스의 변경이 발생하더라도, 최신화된 CloudMap의 정보에 따라 요청 엔드포인트가 결정되므로 안정적으로 서비스가 가능하다. 


