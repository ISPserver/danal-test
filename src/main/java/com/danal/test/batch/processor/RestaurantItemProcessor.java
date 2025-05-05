package com.danal.test.batch.processor;

import com.danal.test.domain.restaurant.Restaurant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class RestaurantItemProcessor implements ItemProcessor<Restaurant, Restaurant> {

    @Override
    public Restaurant process(Restaurant item) {
        try {
            // 데이터 유효성 검사 및 전처리
            validateAndProcessData(item);
            return item;
        } catch (Exception e) {
            log.error("Error processing restaurant data: {}", item.getId(), e);
            throw e;
        }
    }

    private void validateAndProcessData(Restaurant item) {
        // 필수 필드 검증
        if (item.getId() == null) {
            throw new IllegalArgumentException("Restaurant ID cannot be null");
        }

        if (item.getBusinessName() != null) {
            item.setBusinessName(item.getBusinessName().trim());
        }

        if (item.getStreetAddress() != null) {
            item.setStreetAddress(item.getStreetAddress().trim());
        }

        if (item.getParcelAddress() != null) {
            item.setParcelAddress(item.getParcelAddress().trim());
        }

        if (item.getMaleEmployeeCount() != null && item.getMaleEmployeeCount() < 0) {
            item.setMaleEmployeeCount(0);
        }

        if (item.getTotalEmployeeCount() != null && item.getTotalEmployeeCount() < 0) {
            item.setTotalEmployeeCount(0);
        }
    }
} 