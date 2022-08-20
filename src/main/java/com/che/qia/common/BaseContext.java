package com.che.qia.common;

/**
 * @author xiaoluyouqu
 * #Description BaseContext
 * #Date: 2022/8/19 14:30
 */
public class BaseContext {
    /**
     * @description:基于ThreadLocal封装工具类，用户保存和获取当前用户id;
     * @author: che
     * @date: 2022/8/19 14:33
     * @param:
     * @return:
     **/

    private static ThreadLocal<Long> threadLocal=new ThreadLocal();

    public static Long getThreadLocal() {
        return threadLocal.get();
    }

    public static void setThreadLocal(Long id) {
        threadLocal.set(id);
    }
}
