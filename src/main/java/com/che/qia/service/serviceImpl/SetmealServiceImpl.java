package com.che.qia.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.che.qia.entity.Setmeal;
import com.che.qia.mapper.SetmealMapper;
import com.che.qia.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author xiaoluyouqu
 * #Description SetmealServiceImpl
 * #Date: 2022/8/20 10:40
 */
@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {
}
