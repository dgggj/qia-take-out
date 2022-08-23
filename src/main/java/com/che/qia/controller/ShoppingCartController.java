package com.che.qia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.che.qia.common.BaseContext;
import com.che.qia.common.R;
import com.che.qia.entity.ShoppingCart;
import com.che.qia.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author xiaoluyouqu
 * #Description 购物车
 * #Date: 2022/8/22 22:16
 */
@RestController
@Slf4j
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    private ShoppingCartService shoppingCartService;
    /**
     * @description:添加购物车
     * @author: che
     * @date: 2022/8/22 22:17
     * @param:
     * @return:
     **/
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        //设置用户id，指定是哪个用户的购物车数据
        shoppingCart.setUserId(BaseContext.getThreadLocal());

        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,shoppingCart.getUserId());

        if(shoppingCart.getDishId()!=null){
            queryWrapper.eq(ShoppingCart::getDishId,shoppingCart.getDishId());
        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        //查询当前菜品或者套餐是否在购物车中
        if(cartServiceOne!=null){
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(cartServiceOne);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne=shoppingCart;//这个因为不在购物车中，保存后该条数据id才能赋值。为了统一输出
        }
        //如果已经存在就在原来的数量基础上加1

        //如果不存在就加到购物车，数量默认加1

        //
        return R.success(cartServiceOne);
    }
    /**
     * @description:
     * @author: che
     * @date: 2022/8/22 22:54
     * @param:
     * @return:
     **/
    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getThreadLocal());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> delete(){
        Long threadLocal = BaseContext.getThreadLocal();
        LambdaQueryWrapper<ShoppingCart> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,threadLocal);
        shoppingCartService.remove(queryWrapper);
        return R.success("删除成功");
    }



    @Autowired
    public void setShoppingCartService(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }
}
