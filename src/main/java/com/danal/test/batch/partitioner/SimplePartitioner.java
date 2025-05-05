package com.danal.test.batch.partitioner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class SimplePartitioner implements Partitioner {

    private static final int TOTAL_LINE_COUNT = 2_129_830;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        Map<String, ExecutionContext> result = new HashMap<>();
        int targetSize = TOTAL_LINE_COUNT / gridSize;

        int start = 1;
        for (int i = 0; i < gridSize; i++) {
            int end = (i == gridSize - 1) ? TOTAL_LINE_COUNT : start + targetSize - 1;

            ExecutionContext context = new ExecutionContext();
            context.putInt("fromId", start);
            context.putInt("toId", end);
            result.put("partition" + i, context);
            log.info("Partition {}: fromId={}, toId={}", i, start, end);

            start = end + 1;
        }
        return result;
    }
}
