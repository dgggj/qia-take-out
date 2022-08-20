package com.che.qia.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.che.qia.entity.DishFlavor;
import com.che.qia.mapper.DishFlavorMapper;
import com.che.qia.service.DishFlavorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiaoluyouqu
 * #Description DishFlavorServiceImpl
 * #Date: 2022/8/20 19:30
 */
@Service
@Slf4j
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
