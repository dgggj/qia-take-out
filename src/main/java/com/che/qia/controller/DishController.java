package com.che.qia.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.che.qia.common.R;
import com.che.qia.dto.DishDto;
import com.che.qia.entity.Category;
import com.che.qia.entity.Dish;
import com.che.qia.entity.DishFlavor;
import com.che.qia.service.CategoryService;
import com.che.qia.service.DishFlavorService;
import com.che.qia.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author xiaoluyouqu
 * #Description DishFlavorController
 * #Date: 2022/8/20 19:32
 */
@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    private DishService dishService;
    private DishFlavorService dishFlavorService;
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize,String name){
        Page<Dish> dishPage = new Page<>(page,pageSize);
        Page<DishDto> dishDtoPage=new Page<>();
        LambdaQueryWrapper<Dish> dishDtoLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishDtoLambdaQueryWrapper.like(name!=null,Dish::getName,name);
        dishDtoLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,dishDtoLambdaQueryWrapper);
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Category byId = categoryService.getById(item.getCategoryId());
            if(byId!=null){
                dishDto.setCategoryName(byId.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);
        return R.success(dishDtoPage);
    }
    /**
     * @description:根据id查询菜品
     * @author: che
     * @date: 2022/8/21 13:05
     * @param:
     * @return:
     **/
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable("id") Long id){
        return R.success(dishService.getByIdWithFlavor(id));
    }

    /**
     * @description:修改菜品
     * @author: che
     * @date: 2022/8/21 14:02
     * @param:
     * @return:
     **/
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        dishService.updateWithFlavor(dishDto);
        return R.success("更新成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") Integer status,@RequestParam("ids") Long ids){
        Dish byId = dishService.getById(ids);
        byId.setStatus(status);
        dishService.updateById(byId);
        return R.success("禁用成功");
    }
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long ids){
        dishService.removeById(ids);
        return R.success("删除成功");
    }

    /**
     * @description:根据分类id查询菜品
     * @author: che
     * @date: 2022/8/21 19:45
     * @param:
     * @return:
     **/
//    @GetMapping("/list")
//    public R<List<Dish>> list(Dish dish){
//        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
//        queryWrapper.eq(Dish::getStatus,1);
//        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
//        List<Dish> list = dishService.list(queryWrapper);
//        return R.success(list);
//    }
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtoList=list.stream().map((item->{
            DishDto dishDto=new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Category category = categoryService.getById(item.getCategoryId());
            if(category!=null){
                String categoryName=category.getName();
                dishDto.setCategoryName(categoryName);
            }
            LambdaQueryWrapper<DishFlavor>queryWrapper2=new LambdaQueryWrapper<>();
            queryWrapper2.eq(DishFlavor::getDishId,item.getId());
            List<DishFlavor> dishFlavor = dishFlavorService.list(queryWrapper2);
            dishDto.setFlavors(dishFlavor);
            return dishDto;
        })).collect(Collectors.toList());
        return R.success(dishDtoList);
    }






    @Autowired
    public void setDishService(DishService dishService) {
        this.dishService = dishService;
    }
    @Autowired
    public void setDishFlavorService(DishFlavorService dishFlavorService) {
        this.dishFlavorService = dishFlavorService;
    }
    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }
}