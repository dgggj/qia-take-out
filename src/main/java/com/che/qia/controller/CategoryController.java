package com.che.qia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.che.qia.common.R;
import com.che.qia.entity.Category;
import com.che.qia.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author xiaoluyouqu
 * #Description CategoryController
 * #Date: 2022/8/19 19:19
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    private CategoryService service;
    /**
     * @description:新增分类
     * @author: che
     * @date: 2022/8/19 19:31
     * @param:
     * @return:
     **/
    @PostMapping
    public R<String> save(@RequestBody Category category){
        service.save(category);
        return R.success("新增分类成功");
    }

    @GetMapping("/page")
    public R<Page> page(Integer page,Integer pageSize){
        Page<Category> pageInfo = new Page<Category>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);
        service.page(pageInfo,queryWrapper);
        return R.success(pageInfo);

    }
    /**
     * @description:根据id删除
     * @author: che
     * @date: 2022/8/19 21:56
     * @param:
     * @return:
     **/

    @DeleteMapping
    public R<String> delete(Long ids){
        service.remove(ids);
        return R.success("删除成功");
    }
    @Autowired
    public void setService(CategoryService service) {
        this.service = service;
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        service.updateById(category);
        return R.success("修改成功");
    }
}
