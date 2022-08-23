package com.che.qia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.che.qia.entity.Orders;

/**
 * @author xiaoluyouqu
 * #Description OrdersService
 * #Date: 2022/8/23 10:20
 */
public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);

}
