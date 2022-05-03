# Graviton2 소개 

- AWS 인스턴스 타입 
  - Categories * Capabilities * Options = 475+ 개 인스턴스 타입 
- Intel, AMD, AWS Graviton 등 다양한 프로세서 

## 반도체 

- AWS Nitro System / AWS Nitro SSD
  - Hypervisor, network, sotrage, security
- AWS Graviton2 / AWS Graviton3
  - powerful and efficient, moreden application
- AWS Inferentia / AWs Traiium
  - Machine learning

## 그라비톤 프로세서

- 64bit Arm 프로세서 코어 기반 AWS 커스텀 반도체
- Cloud-native 워크로드 고려한 최적화
- 고객을 대신하여 신속하게 혁신하고 개발하기 반복 

## 다양한 워크로드 

- 웹/게임 서비스
- 오픈소스 데이터베이스 
- 소성능 컴퓨팅
- 인메모리 캐시
- 미디어 인코딩 
- 전력 디자인 자동화 
- 분석
- 마이크로서비스 

## 그라비톤 2 기반 EC2 인스턴스

- M6g, M6gd : 일반 목적 워크로드 
- T4g: 버스터블 일반 목적 워크로드 
- C6g, C6gd, C6gn: 컴퓨팅 인텐시브 워크로드 
- R6g, R6gd, X2gd: 메모리
- Im4gn, Is4gen: 스토리지 
- G5g: GPU 기반 그래픽, 머신러닝 워크로드 

- 서울리젼 지원 T4g, M6g, C6g, R6g, G5g 지원

## G5g 인스턴스 - GPU가속용 

- 스트리밍용, 시간당 비용이 최대 30% 저렴 
- x86기반 GPU 인스턴스에 비해 기계학습 추론시 더 낮은 비용으로 사용
- NVIDIA T4G Tensor Core GPU 탑재 인스턴스 

## 구성요소 

- 그라비톤 프로세서 : 고성능, 낮은비용 
- 니트로 시큐리티 칩 : 마더보드 통합 , 하드웨어 장비 보호 
- 니트로 카드 : EBS, 네트워크 어댑터, 모니터링/시큐리티 
- 니트로 하이퍼바이저 : 가벼운 하이퍼바이저, 메모리/CPU 할당, 베어메탈과 같은 성능 

## 그라비톤 이전시 고려사항 

- 