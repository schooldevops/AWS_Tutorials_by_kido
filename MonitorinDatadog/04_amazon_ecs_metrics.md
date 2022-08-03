# Amazon ECS

- Amazon Elastic Container Service(ECS) 는 Docker 컨테이너를 관리하기 위한 서비스이다. 
- 팀은 ECS를 사용하여 Amazon EC2 인스턴스나 AWS Fargate 컴퓨팅 리소스에서 마이크로 서비스용 컨테이너를 자동으로 확장하고 예약할 수 있다. 
- EC2는 컨테이너 인프라에 대한 더 많은 제어를 제공하는 반면 Fargate는 인프라를 프로비저닝 한다. 

## Metrics to watch

- ECS는 컨테이너의 배포 및 관리를 자동화하므로 ECS 클러스터의 상태를 모니터링 하여 컨테이너가 예상대로 생성, 프로비저닝 및 종료되는지 확인하고 싶을 것이다. 
- 또한 ECS작업과 워크로드의 리소스 사용량을 추적하고 싶을 것이다 .

### Desired task count vs. running task count per service

- ECS는 실행할 컨테이너 이미지, 각 컨테이너에 할당할 리소스 수 등을 지정하는 일련의 지침에 따라 작업을 사용하여 컨테이너를 프로비저닝 하고 제어한다. 
- ECS는 실행중인 작업을 자동으로 관리하고 서비스를 지원하는 데 필요한 새 작업을 시작하여 원하는 수의 작업이 항상 실행되도록 한다. 
- 만약 실행중인 작업수가 원하는 작업 수보다 지속적으로 낮은 경우 해당 작업이 실행되지 않은 이유를 조사해야한다. 

### MemoryUtilization

- 메모리 사용률을 추적하면 ECS 인프라를 적절하게 확장했는지 확인할 수 있다. 
- 예를 들어 컨테이너 수준 메모리 사용률을 모니터링하여 하드 메모리 제한을 초과하지 않는지 확인해야한다. 
- ECS는 제한을 초과하는 모든 컨테이너를 종료한다. 
- 메모리 사용률이 지속적으로 높으면 작업이 계속 실행되도록 이 하드 제한을 늘리거나 제거하도록 작업 정의를 업데이트해야 할 수 있다. 

### CPUUtilization

- 메모리와 마찬가지로 컨테이너 수준 CPU 사용률 지표는 EC2 또는 Fargate에서 ECS 작업을 실행하는지 여부에 관계없이 너무 많은 리소스를 소비하는 컨테이너를 식별하는 데 유용할 수 있다. 
- 예를 들어 특정 컨테이너가 리소스를 많이 사용하고 있는 경우 ECS 작업의 다른 컨테이너가 작업을 완료하는 데 충분한 리소스를 갖도록 컨테이너 수준 CPU 제한을 구성해야 할 수 있다. 

## Further reading

- [Key ECS metrics to monitor](https://www.datadoghq.com/blog/amazon-ecs-metrics/)
- [Tools for ECS monitoring](https://www.datadoghq.com/blog/ecs-monitoring-tools/)
- [Monitoring ECS with Datadog](https://www.datadoghq.com/blog/monitoring-ecs-with-datadog/)


