package com.che.qia.controller;

import com.che.qia.common.R;
import com.che.qia.entity.Orders;
import com.che.qia.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoluyouqu
 * #Description OrdersController
 * #Date: 2022/8/23 10:47
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    private OrdersService ordersService;
    /**
     * @description:用户下单
     * @author: che
     * @date: 2022/8/23 10:54
     * @param:
     * @return:
     **/
    @PostMapping("/submit")
    public R<String> submit(@RequestBody Orders orders){
        ordersService.submit(orders);
        return null;

    }



    @Autowired
    public void setOrdersService(OrdersService ordersService) {
        this.ordersService = ordersService;
    }
}
