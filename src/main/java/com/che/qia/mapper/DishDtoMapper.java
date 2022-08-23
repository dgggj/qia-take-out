package com.che.qia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.che.qia.dto.DishDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author xiaoluyouqu
 * #Description DishDtoMapper
 * #Date: 2022/8/20 22:54
 */
@Mapper
public interface DishDtoMapper extends BaseMapper<DishDto> {
    List<DishDto>  selectDishInformation();
}
