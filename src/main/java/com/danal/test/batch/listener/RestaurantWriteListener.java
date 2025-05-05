package com.danal.test.batch.listener;

import com.danal.test.domain.restaurant.Restaurant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Component
public class RestaurantWriteListener implements ItemWriteListener<Restaurant> {

    private final AtomicLong totalProcessed = new AtomicLong();

    @Override
    public void afterWrite(Chunk<? extends Restaurant> items) {
        long count = totalProcessed.addAndGet(items.size());
        if (count % 10000 == 0) {
            log.info("현재까지 {}건 처리 완료", count);
        }
    }
}
