package com.che.qia.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.che.qia.common.CustomException;
import com.che.qia.dto.SetmealDto;
import com.che.qia.entity.Setmeal;
import com.che.qia.entity.SetmealDish;
import com.che.qia.mapper.SetmealMapper;
import com.che.qia.service.SetmealDishService;
import com.che.qia.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoluyouqu
 * #Description SetmealServiceImpl
 * #Date: 2022/8/20 10:40
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
    private SetmealDishService setmealDishService;
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes=setmealDishes.stream().map((item->{
            item.setSetmealId(setmealDto.getCategoryId());
            return item;
        })).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * @param ids
     * @description:删除套餐和它关联的数据
     * @author: che
     * @date: 2022/8/21 22:39
     * @param:
     * @return:
     */
    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {
        //判断当前状态，停售的才能被删除
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);
        int count = this.count(queryWrapper);
        if(count>0){
            throw new CustomException("套餐正字售卖中");
        }
        //如果可以删除，先删除套餐中的数据
        this.removeByIds(ids);
        //删除关系表中数据
        LambdaQueryWrapper<SetmealDish> queryWrapper1=new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        setmealDishService.remove(queryWrapper1);
    }

    @Autowired
    public void setSetmealDishService(SetmealDishService setmealDishService) {
        this.setmealDishService = setmealDishService;
    }
}
