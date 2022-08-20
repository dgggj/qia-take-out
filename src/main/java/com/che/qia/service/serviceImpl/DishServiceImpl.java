package com.che.qia.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.che.qia.entity.Dish;
import com.che.qia.mapper.DishMapper;
import com.che.qia.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiaoluyouqu
 * #Description DishServiceImpl
 * #Date: 2022/8/20 10:18
 */
@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
