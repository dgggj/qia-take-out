package com.che.qia.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

public class SimpleRedisLock implements ILock{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public boolean tryLock(long timeoutSec) {
        return false;
    }

    @Override
    public void unlock() {

    }
}
