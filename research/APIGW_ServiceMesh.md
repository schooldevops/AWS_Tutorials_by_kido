# Do I Need an API Gateway if I Use a Service Mesh?

from: https://blog.christianposta.com/microservices/do-i-need-an-api-gateway-if-i-have-a-service-mesh/

- API Gateway가 북/남 트래픽이라면 service mesh는 동/서 트래픽이다. 
- API Gateway는 비즈니스 기능을 관리, Service mesh 는 서비스와 서비스 사이의 커뮤니케이션 담당
- 다른 사람들은 API 게이트웨이가 서비스 메시를 수행하는 특정 기능을 지적했으며, 그 중 일부는 더 이상 그렇지 않을 수 있다. 

## What is the confusion

- API Management, Kubernetes Ingress 및 API Gateway의 차이점을 평가한 API Gateway의 Identity Crisis에 대해서 저자가 글을 씀
- https://blog.christianposta.com/microservices/api-gateways-are-going-through-an-identity-crisis/
- 기사의 끝에 서비스 메시가 방정식에 어떻게 들어맞는지 설명하려고 했음, 그것들이 어떻게 다른지 또는 언제 둘 중 하나를 사용해야 하는지에 대한 세부 사항이 없음


- 혼란 발생 이유:
  - 사용된 기술에서 오버랩이 있음 (프록시기능)
  - 능력치에 대한 오버랩 (트래픽 컨트롤, 라우팅, 메트릭 수집, 보안/정책 강제 등)
  - 서비스 메시가 API관리를 대체한다는 믿음
  - 서비스 메시의 능력에 대한 잘못된 이해
  - 서비스 메시 몇개는 자체 게이트웨이가 있다는 점

- 마지막 기호가 특히 혼란을 줌
- 만약 서비스 메시가 단지 동/서 트래픽 (영역내에서), 만을 위한 것이라면 Istio 와 같은 일부 서비스 메시에 북쪽/남쪽 (그리고 메시의 일부) 용 인그레스 게이트웨이가 있는 이유가 무엇인가?
- 예를 들어 Istio Ingress Gateway 문서에서 
  - gateway는 수신/발신 HTTP/TCP연결을 수신하는 메시지의 에지에서 동작하는 로드밸런서를 설명한다. 

- 우리 API의 HTTP아닌가? 
- Istio의 게이트웨이(bwt는 놀라운 Envoy Proxy프로젝트를 기반으로함) 를 사용하여 클러스터/메시로 HTTP요청을 받을 수 있다면 충분하지 않는가? 

## 가정

- 이 기사의 나머지 부분에서 "서비스 메시"라고 할때 Istio와 Istio 게이트웨이를 가정한다. 
- 나는 이 시나리오를 선택할 것이다 왜냐하면 이는 중복과 혼란을 가장 잘 보여주는 시나리오 이기 때문이다. 
- 다른 서비스 메시들은 또한 Gateway를 가진다. 
- 또 다른 서비스 메시는 명시적으로 Gateway를 가지지 않는다. 

## 오럽랩 되는 지점

- API Gateway와 Service Mesh의 기능이 겹치는 영영을 인식하는 것이 비즈니스의 첫 번째 순서이다. 
- 이 둘다 어플리케이션 트래픽을 핸들하기 때문에 중복은 놀라운 일이 아니다. 
- 다은 리스트는 오버랩되는 기능이다. 
  - 텔레미터리 수집
  - 분산 트레이싱
  - 서비스 디스커버리
  - 로드 밸런싱
  - TLS 터미네이션/조직
  - JWT 검증
  - 트래픽 분할
  - 카나리 릴리즈
  - 트래픽 쉐도잉
  - 레이트 리밋

## 차이나는 부분

- 서비스 메시는 API게이트웨이 보다 낮은 수준에서 작동하며 아키텍처내 모든 개별 서비스에서 작동한다. 
- 서비스메시는 아키텍처 토폴로지 (클라이언트 로드밸런싱, 서비스 검색, 라우팅), 구현해야하는 탄력성 메커니즘(시간초과, 재시도, 경로차단), 수집해야하는 원격 측정에 대해 서비스 클라이언트에 더 자세한 정보를 제공한다. 
- (메트릭, 추적) 및 참여하는 보안흐름(mTLS, RBAC) 등
- 이러한 구현 상세의 모든것은 어플리케이션에 제공된다. 이는 몇가지 사이더카 프로세스에 의해서 제공되며 대표적으로 Envoy가 있다. 
- ServiceMeshCon의 저자의 글을 확인하자. https://www.slideshare.net/ceposta/the-truth-about-the-service-mesh-data-plane

- 서비스메시의 목적은 문제 해결에 있다. 위에서 설명한 L7에서 투명하게 수행하여 모든 서비스/애플리케이션에 대해 일반적으로 해겨하는 것이다. 
- 다른말로 서비스 메시는 서비스에 혼합되기를 원한다. (실제로 서비스 코드에 코딩되지 않음)

- 결론: 서비스 메시는 나머지 아키텍처 구현에 대해 서비스/클라이언트에 더 자세한 정보/충실도를 제공한다. 

![img01](https://blog.christianposta.com/images/mesh-details.png)

- 반면 API Gateway는 다른 롤을 가진다. "추상 세부정보" 및 구현 분리라는역할을 한다.
- API Gateway는 애플리케이션 아키텍처의 모든 서비스에 걸쳐 응집력 있는 추상화를 제공하는 동시에 특정 API를 대신하여 일부 에지/경게 문제를 해결한다.

![img02](https://blog.christianposta.com/images/abstract-api.png)

