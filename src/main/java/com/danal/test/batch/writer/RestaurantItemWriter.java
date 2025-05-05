package com.danal.test.batch.writer;

import com.danal.test.domain.restaurant.Restaurant;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;

import javax.sql.DataSource;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RestaurantItemWriter implements ItemWriter<Restaurant> {

    JdbcBatchItemWriter<Restaurant> delegate;

    public RestaurantItemWriter(DataSource dataSource) {
        String sql = "INSERT INTO restaurant (open_service_name, open_service_id, local_government_code, management_number, permission_date, cancel_date, business_status_code, business_status, detail_business_status_code, detail_business_status, closed_date, suspended_start_date, suspended_end_date, reopen_date, phone, site_area, parcel_zip_code, parcel_address, street_address, street_zip_code, business_name, last_update_date, data_update_type, data_update_date, business_type, x_coordinate, y_coordinate, sanitation_business_type, male_employee_count, female_employee_count, location_area, grade_division, water_supply_facility_type, total_employee_count, hq_employee_count, factory_office_employee_count, factory_sales_employee_count, factory_production_employee_count, building_ownership_type, security_amount, monthly_rent, multiple_use_facility, total_facility_scale, traditional_restaurant_designation_number, traditional_restaurant_main_dish, homepage) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        this.delegate = new JdbcBatchItemWriterBuilder<Restaurant>()
                .dataSource(dataSource)
                .sql(sql)
                .itemPreparedStatementSetter((item, ps) -> {
                    ps.setString(1, item.getOpenServiceName());
                    ps.setString(2, item.getOpenServiceId());
                    ps.setString(3, item.getLocalGovernmentCode());
                    ps.setString(4, item.getManagementNumber());
                    ps.setString(5, item.getPermissionDate());
                    ps.setString(6, item.getCancelDate());
                    ps.setString(7, item.getBusinessStatusCode());
                    ps.setString(8, item.getBusinessStatus());
                    ps.setString(9, item.getDetailBusinessStatusCode());
                    ps.setString(10, item.getDetailBusinessStatus());
                    ps.setString(11, item.getClosedDate());
                    ps.setString(12, item.getSuspendedStartDate());
                    ps.setString(13, item.getSuspendedEndDate());
                    ps.setString(14, item.getReopenDate());
                    ps.setString(15, item.getPhone());
                    ps.setString(16, item.getSiteArea());
                    ps.setString(17, item.getParcelZipCode());
                    ps.setString(18, item.getParcelAddress());
                    ps.setString(19, item.getStreetAddress());
                    ps.setString(20, item.getStreetZipCode());
                    ps.setString(21, item.getBusinessName());
                    ps.setString(22, item.getLastUpdateDate());
                    ps.setString(23, item.getDataUpdateType());
                    ps.setString(24, item.getDataUpdateDate());
                    ps.setString(25, item.getBusinessType());
                    ps.setObject(26, item.getXCoordinate(), java.sql.Types.DOUBLE);
                    ps.setObject(27, item.getYCoordinate(), java.sql.Types.DOUBLE);
                    ps.setString(28, item.getSanitationBusinessType());
                    ps.setObject(29, item.getMaleEmployeeCount(), java.sql.Types.INTEGER);
                    ps.setObject(30, item.getFemaleEmployeeCount(), java.sql.Types.INTEGER);
                    ps.setString(31, item.getLocationArea());
                    ps.setString(32, item.getGradeDivision());
                    ps.setString(33, item.getWaterSupplyFacilityType());
                    ps.setObject(34, item.getTotalEmployeeCount(), java.sql.Types.INTEGER);
                    ps.setObject(35, item.getHqEmployeeCount(), java.sql.Types.INTEGER);
                    ps.setObject(36, item.getFactoryOfficeEmployeeCount(), java.sql.Types.INTEGER);
                    ps.setObject(37, item.getFactorySalesEmployeeCount(), java.sql.Types.INTEGER);
                    ps.setObject(38, item.getFactoryProductionEmployeeCount(), java.sql.Types.INTEGER);
                    ps.setString(39, item.getBuildingOwnershipType());
                    ps.setObject(40, item.getSecurityAmount(), java.sql.Types.INTEGER);
                    ps.setObject(41, item.getMonthlyRent(), java.sql.Types.INTEGER);
                    ps.setString(42, item.getMultipleUseFacility());
                    ps.setString(43, item.getTotalFacilityScale());
                    ps.setString(44, item.getTraditionalRestaurantDesignationNumber());
                    ps.setString(45, item.getTraditionalRestaurantMainDish());
                    ps.setString(46, item.getHomepage());
                })
                .build();
        this.delegate.afterPropertiesSet();
    }

    @Override
    public void write(Chunk<? extends Restaurant> items) throws Exception {
        try {
            delegate.write(items);
        } catch (Exception e) {
            log.error("Error writing restaurant data: {}", e.getMessage());
            throw e;
        }
    }
}