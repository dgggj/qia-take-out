package com.che.qia.controller;


import com.che.qia.service.DishFlavorService;
import com.che.qia.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoluyouqu
 * #Description DishFlavorController
 * #Date: 2022/8/20 19:32
 */
@RestController
@RequestMapping("/dish")
public class DishFlavorController {
    private DishService dishService;
    private DishFlavorService dishFlavorService;




    @Autowired
    public void setDishService(DishService dishService) {
        this.dishService = dishService;
    }
    @Autowired
    public void setDishFlavorService(DishFlavorService dishFlavorService) {
        this.dishFlavorService = dishFlavorService;
    }
}
