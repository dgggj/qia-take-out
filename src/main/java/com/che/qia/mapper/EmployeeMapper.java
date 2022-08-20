package com.che.qia.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.che.qia.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
