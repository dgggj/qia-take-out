package com.che.qia.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.che.qia.entity.OrderDetail;
import com.che.qia.mapper.OrderDetailMapper;
import com.che.qia.service.OrderDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiaoluyouqu
 * #Description OrderDetailServiceImpl
 * #Date: 2022/8/23 10:25
 */
@Service
@Slf4j
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>implements OrderDetailService {
}
