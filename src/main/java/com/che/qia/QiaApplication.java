package com.che.qia;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j  /*日志*/
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class QiaApplication {
    public static void main(String[] args) {
        SpringApplication.run(QiaApplication.class,args);
        log.info("项目运行成功。。。");
    }
}
