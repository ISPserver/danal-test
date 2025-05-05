package com.danal.test.domain.restaurant;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Comment;

import java.util.Objects;

@Entity
@Table(name = "restaurant")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    Long id;

    @Comment("개방서비스명")
    String openServiceName;
    @Comment("개방서비스아이디")
    String openServiceId;
    @Comment("개방자치단체코드")
    String localGovernmentCode;
    @Comment("관리번호")
    String managementNumber;
    @Comment("인허가일자")
    String permissionDate;
    @Comment("인허가취소일자")
    String cancelDate;
    @Comment("영업상태구분코드")
    String businessStatusCode;
    @Comment("영업상태명")
    String businessStatus;
    @Comment("상세영업상태코드")
    String detailBusinessStatusCode;
    @Comment("상세영업상태명")
    String detailBusinessStatus;
    @Comment("폐업일자")
    String closedDate;
    @Comment("휴업시작일자")
    String suspendedStartDate;
    @Comment("휴업종료일자")
    String suspendedEndDate;
    @Comment("재개업일자")
    String reopenDate;
    @Comment("소재지전화")
    String phone;
    @Comment("소재지면적")
    String siteArea;
    @Comment("소재지우편번호")
    String parcelZipCode;
    @Comment("소재지전체주소")
    String parcelAddress;
    @Comment("도로명전체주소")
    String streetAddress;
    @Comment("도로명우편번호")
    String streetZipCode;
    @Comment("사업장명")
    String businessName;
    @Comment("최종수정시점")
    String lastUpdateDate;
    @Comment("데이터갱신구분")
    String dataUpdateType;
    @Comment("데이터갱신일자")
    String dataUpdateDate;
    @Comment("업태구분명")
    String businessType;
    @Comment("좌표정보x(epsg5174)")
    Double xCoordinate;
    @Comment("좌표정보y(epsg5174)")
    Double yCoordinate;
    @Comment("위생업태명")
    String sanitationBusinessType;
    @Comment("남성종사자수")
    Integer maleEmployeeCount;
    @Comment("여성종사자수")
    Integer femaleEmployeeCount;
    @Comment("영업장주변구분명")
    String locationArea;
    @Comment("등급구분명")
    String gradeDivision;
    @Comment("급수시설구분명")
    String waterSupplyFacilityType;
    @Comment("총직원수")
    Integer totalEmployeeCount;
    @Comment("본사직원수")
    Integer hqEmployeeCount;
    @Comment("공장사무직직원수")
    Integer factoryOfficeEmployeeCount;
    @Comment("공장판매직직원수")
    Integer factorySalesEmployeeCount;
    @Comment("공장생산직직원수")
    Integer factoryProductionEmployeeCount;
    @Comment("건물소유구분명")
    String buildingOwnershipType;
    @Comment("보증액")
    Integer securityAmount;
    @Comment("월세액")
    Integer monthlyRent;
    @Comment("다중이용업소여부")
    String multipleUseFacility;
    @Comment("시설총규모")
    String totalFacilityScale;
    @Comment("전통업소지정번호")
    String traditionalRestaurantDesignationNumber;
    @Comment("전통업소주된음식")
    String traditionalRestaurantMainDish;
    @Comment("홈페이지")
    String homepage;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Restaurant that = (Restaurant) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}