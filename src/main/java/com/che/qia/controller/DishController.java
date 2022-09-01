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
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
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
    private RedisTemplate redisTemplate;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        String key="dish_"+dishDto.getCategoryId()+"_1";
        redisTemplate.delete(key);
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
        Dish dish=dishService.getById(dishDto.getId());
        dishService.updateWithFlavor(dishDto);
        String key="dish_"+dishDto.getCategoryId()+"_1";
        String key2="dish_"+dish.getCategoryId()+"_1";
        redisTemplate.delete(key);
        if(dish.getCategoryId()!=(dishDto.getCategoryId())){
            System.out.println("嗷嗷");
           redisTemplate.delete(key2);
        }

        return R.success("更新成功");
    }

    @PostMapping("/status/{status}")
    public R<String> updateStatus(@PathVariable("status") Integer status,@RequestParam("ids") Long ids){
        Dish byId = dishService.getById(ids);
        byId.setStatus(status);
        dishService.updateById(byId);
        String key="dish_"+byId.getCategoryId()+"_1";
        redisTemplate.delete(key);
        return R.success("禁用成功");
    }
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long ids){
        Dish dish=dishService.getById(ids);
        dishService.removeById(ids);
        String key="dish_"+dish.getCategoryId()+"_1";
        redisTemplate.delete(key);
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
    public R<List<DishDto>> list(@RequestBody Dish dish){
//        return listWithPassThough(dish);
        return listWithMutex(dish);
    }
    //缓存穿透
    public R<List<DishDto>> listWithPassThough(Dish  dish){
        List<DishDto> dishDtoList=null;
        System.out.println(dish.toString());
        //先从redis中获取缓存数据
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        dishDtoList=(List<DishDto>)redisTemplate.opsForValue().get(key);
        if(dishDtoList!=null){
            return R.success(dishDtoList);
        }
        //如果不存在，需要查询数据库，将查询到菜品数据缓存到Redis
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!=null,Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        dishDtoList=list.stream().map((item->{
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
        redisTemplate.opsForValue().set(key,dishDtoList,60, TimeUnit.MINUTES);
        return R.success(dishDtoList);
    }
    //互斥锁
    public R<List<DishDto>> listWithMutex(Dish dish){
        List<DishDto> dishDtoList=null;
        System.out.println(dish.toString());
        //先从redis中获取缓存数据
        String key="dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        dishDtoList=(List<DishDto>)redisTemplate.opsForValue().get(key);
        //判断是否命中
        if(dishDtoList!=null){
            return R.success(dishDtoList);
        }
        //未命中，使用互斥锁解决逻辑穿透问题。重建问题
        //1。获取互斥锁
        String lockKey="lock:dish:"+dish.getCategoryId();
        try {


            boolean flag = tryLock(lockKey);
            //判断是否获取成功
            if (!flag) {
                Thread.sleep(50);
                listWithPassThough(dish);
            }
            //失败休眠，再重试
            //

            //如果不存在，需要查询数据库，将查询到菜品数据缓存到Redis
            LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
            queryWrapper.eq(Dish::getStatus, 1);
            queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
            List<Dish> list = dishService.list(queryWrapper);
            dishDtoList = list.stream().map((item -> {
                DishDto dishDto = new DishDto();
                BeanUtils.copyProperties(item, dishDto);
                Category category = categoryService.getById(item.getCategoryId());
                if (category != null) {
                    String categoryName = category.getName();
                    dishDto.setCategoryName(categoryName);
                }
                LambdaQueryWrapper<DishFlavor> queryWrapper2 = new LambdaQueryWrapper<>();
                queryWrapper2.eq(DishFlavor::getDishId, item.getId());
                List<DishFlavor> dishFlavor = dishFlavorService.list(queryWrapper2);
                dishDto.setFlavors(dishFlavor);
                return dishDto;
            })).collect(Collectors.toList());
            Thread.sleep(200);//模拟重建延时
            redisTemplate.opsForValue().set(key, dishDtoList, 60, TimeUnit.MINUTES);
        }catch (InterruptedException e){
            e.printStackTrace();
        }finally {
            //释放互斥锁
            unLock(lockKey);
        }

        return R.success(dishDtoList);
    }
    public boolean tryLock(String key){
       boolean flag= redisTemplate.opsForValue().setIfAbsent(key,"1",10,TimeUnit.SECONDS);
       return BooleanUtils.isTrue(flag);

    }
    public void unLock(String key){
        redisTemplate.delete(key);
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
    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
