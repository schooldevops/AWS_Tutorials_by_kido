# Docker

- Docker 는 앱을 배포하기 위한 개발 플랫폼이다. 
- App은 컨테이너에 패키지 되며, 어떠한 OS에서든지 실행이 가능하다. 
- App 은 동일하게 실행된다. 
  - 어떤 머신이든지 실행가능
  - 호환성 이슈 없음
  - 예상가능한 행위
  - 노력이 적게 든다. 
  - 쉽게 관리할 수 있고, 배포 가능하다. 
  - 어떠한 언어든지 상관이 없고 어떠한 OS, 기술이든지 수용한다. 
- 컨테이너 스케일 업/다운이 쉽다. 
- Docker은 리포지토리에 저장된다. 
- https://hub.docker.com 이 공식 이포지토리허브이다. 여기에서 대부분 컨테이너 이미지를 찾을 수 있다. 
- ECR 은 Private Docker Repository 이다. 

## ECS란

- Elastic Container Service
- AWS에서 Docker 커넽이너를 실행하도록 한다. 
- EC2 인스턴스를 프로비전하고, 인프라를 관리해야한다. 
- AWS 는 컨테이너의 시작/정지를 수행한다. 
- 어플리케이션 로드 밸런서와 통합할 수 있다. 

## Fargate

- AWS에서 Docker 커넽이너를 실행하도록 한다. 
- 프로비저닝이 필요 없고, 인프라 EC2를 관리할 필요가 없다. 
- serveless 서비스
- AWS는 필요한 CPU/RAM 을 지정하면 이를 기반으로 적절히 실행한다. 

## Docker 명령

- docker run
- docker container ls
- docker inspect container_id
- docker stop container_id
- docker start coontainer_id
- docker rm container_id
- docker image ls
- docker image rm image_id
- docker build -t container-name:tag .

## ECS Clusters Overview

- ECS Clusters 는 EC2 인스턴스들의 논리적인 그룹이다. 
- EC2 인스턴스들은 ECS Agent 가 수행한다. (Docker Container)
- ECS Agents 인스턴스를 ECS Cluster에 등록한다. 
- EC2 인스턴스는 특정 AMI를 실행한다. (Amazon Machine Images), 이는 특정 ECS에 대해 만들어진다. 

## ECS + IAM Role

- EC2Role
  - EC2 Hosts에 있는 ECS Agent가 ECS와 ECR사이에 통신하도록 허용한다. 
- ECSRole
  - 리소스 관리를 위해 ECS 인증을 위한 롤이다. 
- ECSTaskExecutionRole
  - ECS Tasks를 첨부하는 롤이다. 
- AutoscalingRole
  - AWS Autoscaling 이 상태를 검사하고, 타겟의 화장을 조정하는 역할을 한다. 