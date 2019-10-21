# Spring5-JCOSample
JCO 관련하여 테스트 및 활용하기 위한 샘플

## 사용된 버전

>java : 1.8.X  
>spring : 5.0.X  
>jco : 3.0.X  
>junit : 4.X  


## 특이사항

자세한 사항은 나의 블로그 https://rmsdud2435.github.io/geunny-blog/blog/spring/Spring 참고

### root-context.xml

jcoProps의 빈 객체에 대해 본인이 접근하려는 SAP 정보 작성  

### SAPService

excute메소드에 대해 첫 parameter는 RFC명, parameter는 input값, 세번째 parameter는 SapMapper의 구조체가 들어온다.  


### mappingRequestObjectToSapData 메소드

RFC의 input값에 대한 구조체 만드는 메소드이다. 테이블 형식, 파라미터 형식, 구조체 형식에 따라서 주석을 풀어서 사용된다.  

### mappingResponseSapDataToObject 메소드

RFC의 input값에 대한 구조체 만드는 메소드이다. 테이블 형식, 파라미터 형식, 구조체 형식에 따라서 주석을 풀어서 사용하면 된다.  
 
### verifySapResult 메소드

일반적으로 SAP통신 후 해당 통신에 대해 제대로 실행되었는지 값을 내려준다. 그 값을 확인하여 예외처리하는 로직을 넣어서 사용하면 된다.  
