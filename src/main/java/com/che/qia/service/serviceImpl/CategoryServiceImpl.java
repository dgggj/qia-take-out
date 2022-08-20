package com.che.qia.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.che.qia.common.CustomException;
import com.che.qia.entity.Category;
import com.che.qia.entity.Dish;
import com.che.qia.entity.Setmeal;
import com.che.qia.mapper.CategoryMapper;
import com.che.qia.service.CategoryService;
import com.che.qia.service.DishService;
import com.che.qia.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author xiaoluyouqu
 * #Description CategoryServiceImpl
 * #Date: 2022/8/19 19:13
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private DishService dishService;
    private SetmealService setmealService;

    /**
     * @description:根据id删除分类，删除前需要判断
     * @date: 2022/8/20 10:43
     * @param:
     * @return:
     **/
    @Override
    public void remove(Long id) {
        //查询的当前分类是否关联了菜品，如果关联，抛出一个异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper=new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count = dishService.count(dishLambdaQueryWrapper);
        if(count>0){
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        //查询的当前分类是否关联了菜品，如果关联，抛出一个异常
       LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper=new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if(count1>0){
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        super.removeById(id);

        //正常删除

    }
    @Autowired
    public void setDishService(DishService dishService) {
        this.dishService = dishService;
    }
    @Autowired
    public void setSetmealService(SetmealService setmealService) {
        this.setmealService = setmealService;
    }
}
