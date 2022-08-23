package com.che.qia.vo;

import com.che.qia.entity.Dish;
import lombok.Data;

/**
 * @author xiaoluyouqu
 * #Description DishVo
 * #Date: 2022/8/20 22:25
 */
@Data
public class DishVo extends Dish {
    private String categoryName;
}
