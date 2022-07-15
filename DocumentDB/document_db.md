# DocumentDB 

- from: https://aws.amazon.com/ko/documentdb/faqs/

## 일반 

### Amazon DocumentDB (MongoDB호환)

- MongoDB 워크로드를 지원하는 빠르고 확장 가능하며, 가용성이 뛰어난 완전관리형 문서 데이터베이스 서비스 
- JSON 데이터 저장/쿼리/인덱싱 가능 
- 기존 mongoDB 애플리케이션 코드, 드라이버 및 도구 사용 가능 
- AWS DMS(Data Migration Service) 이용하여 마이그레이션 가능

### MongoDB 호환

- DocumentDB는 MongoDB 3.6 / 4.0 API와 상호작용 가능
- DocumentDB는 대부분의 MongoDB 기능을 사용할 수 있으나, 지원하지 않는것도 있음 

### 라이선스

- MongoDB SSPL 라이선스 제한은 없음 내부적으로 MongoDB SSPL 코드를 활용하지 않음

### MongoDB --> DocumentDB 마이그

- DMS 이용하여 마이그레이션을 쉽게 수행할 수 있음. 
- mongodump/mongorestore, mongoexport/mongoimport 를 통해서 수행가능

### 클라이언트 드라이버

- MongoDB 3.4 이상과 호환되는 대부분의 MongoDB와 호환됨

### DocumentDB 는 ACID 트랜잭션 지원하는가?

- MongoDB 4.0 호환성 지원
- 그러므로 문서, 명령문, 컬렉션, 데이터베이스에 걸쳐 (원자성, 일관성, 격리, 내구성) 지원함

### Amazon DocumentDB는 MongoDB의 EOL을 따르는가? 

- 아니다. 
- Amazon DocumentDB는 MongoDB와는 다른 수명주기를 따름 그러므로 서로 상관이 없음 

### 자신의 Amazon DocumentDB 클러스터에 엑세스 방법

- VPC 내에 배포된 클러스터는 동일한 EC2 인스턴스 또는 서비스에 직접 엑세스 가능함
- VPC 피어링을 통해서도 접속이 가능함 
- mongo shell, MongoDB 드라이버를 이용하여 접속 가능 
- 접속시에는 인증 과정이 필요함

### Amazon DocuemntDB 를 사용하려면 Amazon RDS 권한 및 리소스가 필요한 이유

- 인스턴스 수명 주기관리, Amazon Key Management Service(KMS) 를 사용한 저장 중 암호화, 보안그룹 관리와 같은 특정 관리 기능의 경우 Amazon DocumentDB 는 RDS및 Neptune 과 공유되는 운용기술 활용 
- describe-db-instance 및 describe-db-cluster AWS CLI API 사용하는 경우 
- "--filter Name=engine,Values=docdb" 파라미터를 사용하여 Amazon DocumentDB 리소스를 필터링 하는 것이 좋음 

### Amazon DocumentDB 제공하는 인스턴스 유형

- 리젼별로 사용 가능한 인스턴스 유형 
- Amazon DocumentDB 요금페이지 참조 
- https://aws.amazon.com/ko/documentdb/pricing/

### Amazon DocumentDB 사용하기 

- https://aws.amazon.com/ko/documentdb/getting-started/

### SAL가 존재하는가? 

- https://aws.amazon.com/ko/documentdb/sla/

##  성능

### DocumentDB 에서 어떤 유형의 성능을 기대하나?

- Amaon DocumentDB는 미리 쓰기 로그만 유지하며, 전체 버퍼 페이지 동기화를 작성할 필요가 없음
- 안정성을 저하하지 않는 이러한 최적화 덕분에, 쓰기 속도는 일반적인 데이티어베이스보다 더 빠름
- 최대 15개의 읽기 전용 복제본, 초당 수백만건의 읽기 가능 

## 요금

### 사용료, 사용 리젼정보

- https://aws.amazon.com/ko/documentdb/pricing/

### 프리티어

- 1개월 무료 평가판 사용 가능
- 월 750시간의 t3.medium 인스턴스
- 3천만 단위의 IO, 5GB의 스토리지, 5GB의 백업 스토리지를 30일동안 무료로 사용가능
- 무료기간 만료시, 클러스터 종료 혹은 표준 온디멘드로 계속 실행가능

## Amazon DocuentDB 는 스토리지 몰륨의 각 청크를 6가지 방법으로 3개의 가용 영역에 복제, 실제 요금은 3배? 6배? 인가?

- 소토리지 복제 요금이 이미 포함된 가격임
- 데이터 크기 기준으로 요금 부과, 복제 스토리지에 대한 비용은 별도 발생하지 않음 

## DocumentDB에서 I/O는 어떻게 계산되는가?

- IO는 Amazon DocumentDB 에서 Solid State Drive(SSD) 기반의 가상화된 스토리지 계층에 대해 수행되는 I/O 작업
- 페이지 읽기 작업은 1건의 I/O로 계산
- 버퍼 캐시에 없는 페이지 가져올때에는 스토리지 계층에서 읽기 수행
- 각 페이지는 8Kb
- 비용절감, 읽기/쓰기 트래픽을 위해 불필요한 I/O 작업 제거
- 쓰기  I/O는 안정적인 쓰기를 위해 

