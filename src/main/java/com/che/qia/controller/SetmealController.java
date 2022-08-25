package com.che.qia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.che.qia.common.R;
import com.che.qia.dto.SetmealDto;
import com.che.qia.entity.Category;
import com.che.qia.entity.Dish;
import com.che.qia.entity.Setmeal;
import com.che.qia.entity.SetmealDish;
import com.che.qia.service.CategoryService;
import com.che.qia.service.DishService;
import com.che.qia.service.SetmealDishService;
import com.che.qia.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoluyouqu
 * #Description SetmealController
 * #Date: 2022/8/21 19:37
 */
@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetmealController {
    private SetmealService setmealService;
    private SetmealDishService setmealDishService;
    private CategoryService categoryService;
    private DishService dishService;

    /**
     * @description:新增套餐，同时保证套餐和菜品的关系
     * @author: che
     * @date: 2022/8/21 19:54
     * @param:
     * @return:
     **/
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }
    /**
     * @description:查询套餐信息
     * @author: che
     * @date: 2022/8/21 20:31
     * @param:
     * @return:
     **/
    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        //分页构造器
        Page<Setmeal> setmealPage = new Page<>(page,pageSize);
        Page<SetmealDto>setmealDtoPage=new Page<>(page,pageSize);
        //添加查寻条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        Page<Setmeal> page1 = setmealService.page(setmealPage, queryWrapper);
        BeanUtils.copyProperties(page1,setmealDtoPage,"rewords");
        List<Setmeal> records = setmealPage.getRecords();
        List<SetmealDto> list=records.stream().map((item->{
            SetmealDto setmealDto = new SetmealDto();
            //对象拷贝
            BeanUtils.copyProperties(item,setmealDto);
            Category byId = categoryService.getById(item.getCategoryId());
            if(byId!=null){
                setmealDto.setCategoryName(byId.getName());
            }
            return setmealDto;
        })).collect(Collectors.toList());
        setmealDtoPage.setRecords(list);
        return R.success(setmealDtoPage);
    }
    /**
     * @description:删除套餐
     * @author: che
     * @date: 2022/8/21 22:32
     * @param:
     * @return:
     **/
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        setmealService.deleteWithDish(ids);
        return R.success("删除成功");
    }
    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }
    @PostMapping("/status/{status}")
    public R<String> status(@PathVariable("status") Integer status, @PathParam("ids") Long ids){
       Setmeal setmeal= setmealService.getById(ids);
       log.info("status:{},ids:{}",status,ids);
        if(status==0){
            setmeal.setStatus(status);
            setmealService.updateById(setmeal);
        }else if(status==1){
            LambdaQueryWrapper<SetmealDish> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(SetmealDish::getSetmealId,ids);
           List<SetmealDish> list= setmealDishService.list(queryWrapper);
           List<Dish>dishes=list.stream().map((item)->{
               Long id=item.getDishId();
               return dishService.getById(id);
           }).collect(Collectors.toList());
           for(Dish dish:dishes){
               if(dish.getStatus()==0){
                   return R.error("该套餐中有菜品已停售");
               }
           }
           setmeal.setStatus(status);
           setmealService.updateById(setmeal);
        }else {
            return R.error("状态码出错");
        }
        return R.success("更改状态成功");
    }

    @Autowired
    public void setDishService(DishService dishService) {
        this.dishService = dishService;
    }

    @Autowired
    public void setSetmealService(SetmealService setmealService) {
        this.setmealService = setmealService;
    }
    @Autowired
    public void setSetmealDishService(SetmealDishService setmealDishService) {
        this.setmealDishService = setmealDishService;
    }
    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}
