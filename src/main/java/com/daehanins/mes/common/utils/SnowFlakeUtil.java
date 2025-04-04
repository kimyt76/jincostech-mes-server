package com.daehanins.mes.common.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * Author zzxadi https://github.com/zzxadi/Snowflake-IdWorker
 * @author Exrickx
 */
@Slf4j
public class SnowFlakeUtil {

    private final long id;
    /**
     * time start marker point, as a benchmark, generally take the system's most recent time
     */
    private final long epoch = 1524291141010L;
    /**
     * machine identification digits
     */
    private final long workerIdBits = 10L;
    /**
     * machine ID max: 1023
     */
    private final long maxWorkerId = -1L ^ -1L << this.workerIdBits;
    /**
     * 0, concurrency control
     */
    private long sequence = 0L;
    /**
     * Self-increment in milliseconds
     */
    private final long sequenceBits = 12L;

    /**
     * 12
     */
    private final long workerIdShift = this.sequenceBits;
    /**
     * 22
     */
    private final long timestampLeftShift = this.sequenceBits + this.workerIdBits;
    /**
     * 4095,111111111111,12位
     */
    private final long sequenceMask = -1L ^ -1L << this.sequenceBits;
    private long lastTimestamp = -1L;

    private SnowFlakeUtil(long id) {
        if (id > this.maxWorkerId || id < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", this.maxWorkerId));
        }
        this.id = id;
    }

    public synchronized long nextId() {
        long timestamp = timeGen();
        if (this.lastTimestamp == timestamp) {
            //If the last timestamp is equal to the newly generated one, the sequence is incremented by one (0-4095 loop); for the new timestamp, the sequence starts from 0
            this.sequence = this.sequence + 1 & this.sequenceMask;
            if (this.sequence == 0) {
                // regenerate timestamp
                timestamp = this.tilNextMillis(this.lastTimestamp);
            }
        } else {
            this.sequence = 0;
        }

        if (timestamp < this.lastTimestamp) {
            log.error(String.format("clock moved backwards.Refusing to generate id for %d milliseconds", (this.lastTimestamp - timestamp)));
            return -1;
        }

        this.lastTimestamp = timestamp;
        return timestamp - this.epoch << this.timestampLeftShift | this.id << this.workerIdShift | this.sequence;
    }

    private static SnowFlakeUtil flowIdWorker = new SnowFlakeUtil(1);
    public static SnowFlakeUtil getFlowIdInstance() {
        return flowIdWorker;
    }

    /**
     * Wait for the next millisecond to arrive, guarantee that the number of milliseconds returned is after the parameter lastTimestamp
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * Get the current number of milliseconds in the system
     */
    private static long timeGen() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        for(int i=0;i<100;i++){

            System.out.println(String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId()));
//
//            SnowFlakeUtil snowFlakeUtil = SnowFlakeUtil.getFlowIdInstance();
//            System.out.println(snowFlakeUtil.nextId());
        }
    }
}
