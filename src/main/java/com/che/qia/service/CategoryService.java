package com.che.qia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.che.qia.entity.Category;

/**
 * @author xiaoluyouqu
 * #Description CategoryService
 * #Date: 2022/8/19 19:11
 */
public interface CategoryService extends IService<Category> {

    public void remove(Long id);
}
