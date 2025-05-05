# 대용량 CSV 데이터 처리 배치 애플리케이션

공공데이터포털의 전국일반음식점표준데이터를 MySQL 데이터베이스에 무결성을 보장하며 효율적으로 적재하는 Spring Batch 기반의 배치 애플리케이션

## 개요

본 프로젝트는 약 210만 건의 대규모 CSV 데이터를 고성능으로 처리하기 위한 배치 시스템 구현 과제로, 병렬 처리, 예외 복구, 실시간 로깅 및 테스트 기반의 품질 검증을 목표로 함.

- [공공데이터 링크](https://www.data.go.kr/data/15096283/standard.do)
- 데이터 항목 예시: 번호, 개방서비스명, 개방자치단체코드, 위도, 경도, 도로명 주소 등

---

## 기술 스택

- JDK 17
- Spring Boot 3.4
- Spring Batch 5.2
- MySQL 8.0.28
- Gradle 8.13

---

## 주요 기능

### 대용량 CSV 파일 파싱 및 DB 저장
- 약 2,129,830건의 데이터를 청크 단위로 처리
- `FlatFileItemReader`로 CSV를 스트리밍 방식으로 읽어 메모리 효율성 확보

### 병렬 파티셔닝 처리
- `SimplePartitioner`를 통해 8개의 파티션으로 분할
- `ThreadPoolTaskExecutor`를 활용한 멀티 스레드 처리
- 각 파티션은 독립적으로 파일 영역을 읽고 처리

### 무결성 보장 및 예외 복구
- 잘못된 데이터는 최대 10건까지 스킵 처리하며, 개별 로깅
- 일시적 장애는 최대 3회까지 재시도
- 실패한 Job/Step/Item에 대한 상세 예외 메시지 출력

### 실시간 로깅 및 진행 상황 추적
- Job/Step/Item 단위 실행 로그 기록 (`JobExecutionListener`, `StepExecutionListener`, `ItemWriteListener` 등)
- 처리 건수 실시간 누적 로그 (10,000건 단위)
- 실패 항목별 예외 메시지 출력

### 테스트 코드 작성
- `JobLauncherTestUtils`를 사용한 통합 테스트
- CSV 파싱 및 DB 적재 검증
- 파티셔닝 환경에서도 전체 처리 건수 검증

---

## 실행 가이드

1. 공공데이터 CSV 파일 다운로드
   - Github Repository에 100MB 이상의 파일을 업로드할 수 없기 때문에, 직접 다운로드 후 사용 필요 
   - [전국일반음식점표준데이터](https://www.data.go.kr/data/15096283/standard.do)에서 CSV 파일 다운로드
   - 다운로드 한 파일을 `fulldata_07_24_04_P_normal_restaurant.csv`로 명칭 변경 후, `src/main/resources/static/csv` 디렉토리에 저장
 
2. MySQL 도커 컨테이너 실행
   ```bash
   docker-compose -f ./docker-env/docker-compose_local.yml up -d
   ```


3. 애플리케이션 배치 실행
   TestApplication Main Method 실행 시, `Job`이 자동으로 실행 

## 테스트 실행
   RestaurantBatchTest 클래스 내 `@Test` 어노테이션이 붙은 메서드 실행

## 데이터베이스 스키마
테스트 용이기 때문에 varchar(255)로 설정하였으나, 실제 환경에서는 적절한 데이터 길이를 명시하여 Latency를 줄일 수 있음.

```sql
create table restaurant
(
    factory_office_employee_count             int          null comment '공장사무직직원수',
    factory_production_employee_count         int          null comment '공장생산직직원수',
    factory_sales_employee_count              int          null comment '공장판매직직원수',
    female_employee_count                     int          null comment '여성종사자수',
    hq_employee_count                         int          null comment '본사직원수',
    male_employee_count                       int          null comment '남성종사자수',
    monthly_rent                              int          null comment '월세액',
    security_amount                           int          null comment '보증액',
    total_employee_count                      int          null comment '총직원수',
    x_coordinate                              double       null comment '좌표정보x(epsg5174)',
    y_coordinate                              double       null comment '좌표정보y(epsg5174)',
    id                                        bigint auto_increment
        primary key,
    building_ownership_type                   varchar(255) null comment '건물소유구분명',
    business_name                             varchar(255) null comment '사업장명',
    business_status                           varchar(255) null comment '영업상태명',
    business_status_code                      varchar(255) null comment '영업상태구분코드',
    business_type                             varchar(255) null comment '업태구분명',
    cancel_date                               varchar(255) null comment '인허가취소일자',
    closed_date                               varchar(255) null comment '폐업일자',
    data_update_date                          varchar(255) null comment '데이터갱신일자',
    data_update_type                          varchar(255) null comment '데이터갱신구분',
    detail_business_status                    varchar(255) null comment '상세영업상태명',
    detail_business_status_code               varchar(255) null comment '상세영업상태코드',
    grade_division                            varchar(255) null comment '등급구분명',
    homepage                                  varchar(255) null comment '홈페이지',
    last_update_date                          varchar(255) null comment '최종수정시점',
    local_government_code                     varchar(255) null comment '개방자치단체코드',
    location_area                             varchar(255) null comment '영업장주변구분명',
    management_number                         varchar(255) null comment '관리번호',
    multiple_use_facility                     varchar(255) null comment '다중이용업소여부',
    open_service_id                           varchar(255) null comment '개방서비스아이디',
    open_service_name                         varchar(255) null comment '개방서비스명',
    parcel_address                            varchar(255) null comment '소재지전체주소',
    parcel_zip_code                           varchar(255) null comment '소재지우편번호',
    permission_date                           varchar(255) null comment '인허가일자',
    phone                                     varchar(255) null comment '소재지전화',
    reopen_date                               varchar(255) null comment '재개업일자',
    sanitation_business_type                  varchar(255) null comment '위생업태명',
    site_area                                 varchar(255) null comment '소재지면적',
    street_address                            varchar(255) null comment '도로명전체주소',
    street_zip_code                           varchar(255) null comment '도로명우편번호',
    suspended_end_date                        varchar(255) null comment '휴업종료일자',
    suspended_start_date                      varchar(255) null comment '휴업시작일자',
    total_facility_scale                      varchar(255) null comment '시설총규모',
    traditional_restaurant_designation_number varchar(255) null comment '전통업소지정번호',
    traditional_restaurant_main_dish          varchar(255) null comment '전통업소주된음식',
    water_supply_facility_type                varchar(255) null comment '급수시설구분명'
);
```

## 모니터링 및 로깅

- Job/Step 실행 상태 모니터링
- 상세한 로그 기록
- 예외 발생 시 스택 트레이스 기록
- 처리 건수 및 성능 메트릭 기록

## 예외 처리 전략

1. **스킵 처리**
   - 최대 10건까지 스킵 허용
   - 스킵된 데이터는 로그에 기록

2. **재시도 처리**
   - 최대 3회까지 재시도
   - 데이터베이스 교착상태 등의 일시적 오류에 대응

## 성능 메트릭

- 처리 시간: 약 30분 (2,129,830건 기준)
- 처리 속도: 약 1,183건/초
- 분당 처리 건수: 약 71,000건
- CPU 사용률: 70-80%

## 향후 개선 필요시 계획

1. **성능 최적화**
   - 파티션 크기 동적 조정
   - 데이터베이스 인덱스 최적화
   - Column Length 조정 

2. **모니터링 강화**
   - Prometheus/Grafana/ELK 같은 모니터링 도구 추가
   - 상세한 성능 메트릭 수집

3. **안정성 개선**
   - 장애 복구 메커니즘 강화
   - 데이터 검증 프로세스 추가