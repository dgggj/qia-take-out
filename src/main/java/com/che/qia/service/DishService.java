package com.che.qia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.che.qia.dto.DishDto;
import com.che.qia.entity.Dish;

/**
 * @author xiaoluyouqu
 * #Description DishService
 * #Date: 2022/8/20 10:17
 */
public interface DishService extends IService<Dish> {
    //新增菜品，同时插入菜品对应的口味数据，需要两张表：dish，dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);
}
