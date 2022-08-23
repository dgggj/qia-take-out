package com.che.qia.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author xiaoluyouqu
 * #Description MyMetaObjectHandler
 * #Date: 2022/8/19 13:22
 */
@Component
@Slf4j
public class MyMetaObjectHandler implements MetaObjectHandler {
//    插入操作自动填充
    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insert】");
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("createUser",BaseContext.getThreadLocal());
        metaObject.setValue("updateUser",BaseContext.getThreadLocal());
    }
//更新操作自动填充
    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】");
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",BaseContext.getThreadLocal());

    }
}
