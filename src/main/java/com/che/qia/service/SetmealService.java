package com.che.qia.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.che.qia.dto.SetmealDto;
import com.che.qia.entity.Setmeal;

import java.util.List;

/**
 * @author xiaoluyouqu
 * #Description SetmealService
 * #Date: 2022/8/20 10:39
 */
public interface SetmealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);
    /**
     * @description:删除套餐和它关联的数据
     * @author: che
     * @date: 2022/8/21 22:39
     * @param:
     * @return:
     **/

    void deleteWithDish(List<Long> ids);
}
