package com.che.qia.utils;

public interface ILock{
    //尝试获取锁，timeoutSec表示锁持有时间，过期后自动释放
    boolean tryLock(long timeoutSec);
    //释放锁
    void unlock();
}
