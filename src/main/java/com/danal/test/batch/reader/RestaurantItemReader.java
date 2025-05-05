package com.danal.test.batch.reader;

import com.danal.test.domain.restaurant.Restaurant;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

@Slf4j
@StepScope
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RestaurantItemReader implements ItemReader<Restaurant> {

    final FlatFileItemReader<Restaurant> delegate;
    int fromId;
    int toId;
    int lineCount = 0;

    public RestaurantItemReader(@Value("#{stepExecutionContext[fromId]}") int fromId,
                                @Value("#{stepExecutionContext[toId]}") int toId) {
        this.fromId = fromId;
        this.toId = toId;
        this.delegate = createReader();
        try {
            this.delegate.open(new ExecutionContext());
        } catch (Exception e) {
            throw new RuntimeException("Failed to open reader", e);
        }
    }

    @Override
    public Restaurant read() throws Exception {
        while (true) {
            Restaurant item = delegate.read();
            lineCount++;

            if (lineCount >= fromId && lineCount <= toId) {
                return item;
            }
        }
    }

    private FlatFileItemReader<Restaurant> createReader() {
        BeanWrapperFieldSetMapper<Restaurant> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Restaurant.class);

        return new FlatFileItemReaderBuilder<Restaurant>()
                .name("restaurantItemReader")
                .resource(new ClassPathResource("static/csv/fulldata_07_24_04_P_normal_restaurant.csv"))
                .delimited()
                .delimiter(",")
                .quoteCharacter('\"')
                .strict(false)
                .names(
                        "id", "openServiceName", "openServiceId", "localGovernmentCode", "managementNumber",
                        "permissionDate", "cancelDate", "businessStatusCode", "businessStatus",
                        "detailBusinessStatusCode", "detailBusinessStatus", "closedDate",
                        "suspendedStartDate", "suspendedEndDate", "reopenDate", "phone", "siteArea",
                        "parcelZipCode", "parcelAddress", "streetAddress", "streetZipCode", "businessName",
                        "lastUpdateDate", "dataUpdateType", "dataUpdateDate", "businessType",
                        "xCoordinate", "yCoordinate", "sanitationBusinessType", "maleEmployeeCount",
                        "femaleEmployeeCount", "locationArea", "gradeDivision", "waterSupplyFacilityType",
                        "totalEmployeeCount", "hqEmployeeCount", "factoryOfficeEmployeeCount",
                        "factorySalesEmployeeCount", "factoryProductionEmployeeCount", "buildingOwnershipType",
                        "securityAmount", "monthlyRent", "multipleUseFacility", "totalFacilityScale",
                        "traditionalRestaurantDesignationNumber", "traditionalRestaurantMainDish", "homepage"
                )
                .fieldSetMapper(fieldSetMapper)
                .encoding("MS949")
                .linesToSkip(1)
                .build();
    }
}