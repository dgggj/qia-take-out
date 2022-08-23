package com.che.qia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.che.qia.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaoluyouqu
 * #Description OrdersMapper
 * #Date: 2022/8/23 10:18
 */
@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {
}
