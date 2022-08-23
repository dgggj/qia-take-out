package com.che.qia.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.che.qia.entity.User;
import com.che.qia.mapper.UserMapper;
import com.che.qia.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @author xiaoluyouqu
 * #Description UserServiceImpl
 * #Date: 2022/8/22 10:28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
