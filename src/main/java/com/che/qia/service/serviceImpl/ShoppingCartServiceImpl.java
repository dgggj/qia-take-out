package com.che.qia.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.che.qia.entity.ShoppingCart;
import com.che.qia.mapper.ShoppingCartMapper;
import com.che.qia.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * @author xiaoluyouqu
 * #Description ShoppingCartServiceImpl
 * #Date: 2022/8/22 22:15
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
