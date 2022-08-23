package com.che.qia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.che.qia.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author xiaoluyouqu
 * #Description UserMapper
 * #Date: 2022/8/22 10:26
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
