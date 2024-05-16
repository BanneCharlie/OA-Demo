package com.banne.template.common.utils;

import org.springframework.stereotype.Component;

@Component
public class SnowflakeIdGeneratorUtil {

        private static final long START_TIMESTAMP = 1620000000000L; // 设置一个起始时间戳，这里设为2021-05-03 00:00:00

        private static final long WORKER_ID_BITS = 5L;
        private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);

        private static final long SEQUENCE_BITS = 10L;

        private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;
        private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;

        private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

        private long sequence = 0L;
        private long lastTimestamp = -1L;

        public synchronized long nextId(long workerId) {
            if (workerId > MAX_WORKER_ID || workerId < 0) {
                throw new IllegalArgumentException(String.format("Worker ID must be between 0 and %d", MAX_WORKER_ID));
            }

            long timestamp = System.currentTimeMillis();

            if (timestamp < lastTimestamp) {
                throw new RuntimeException("Clock moved backwards. Refusing to generate id for " + (lastTimestamp - timestamp) + " milliseconds");
            }

            if (lastTimestamp == timestamp) {
                sequence = (sequence + 1) & SEQUENCE_MASK;
                if (sequence == 0) {
                    timestamp = tilNextMillis(lastTimestamp);
                }
            } else {
                sequence = 0L;
            }

            lastTimestamp = timestamp;

            return ((timestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT) |
                    (workerId << WORKER_ID_SHIFT) |
                    sequence;
        }

        private long tilNextMillis(long lastTimestamp) {
            long timestamp = System.currentTimeMillis();
            while (timestamp <= lastTimestamp) {
                timestamp = System.currentTimeMillis();
            }
            return timestamp;
        }


}

