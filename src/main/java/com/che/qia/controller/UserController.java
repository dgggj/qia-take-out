package com.che.qia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.che.qia.common.R;
import com.che.qia.entity.User;
import com.che.qia.service.UserService;

import com.che.qia.utils.SMSUtils;
import com.che.qia.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @author xiaoluyouqu
 * #Description UserController
 * #Date: 2022/8/22 10:29
 */
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    /**
     * @description:发送手机验证码
     * @author: che
     * @date: 2022/8/22 10:31
     * @param:
     * @return:
     **/

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone=user.getPhone();
        if(StringUtils.isNotEmpty(phone)){
        //随机生成4位验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            //调用阿里云提供的短信服务api完成发送短信任务
//            SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);

            session.setAttribute(phone,code);
            log.info(code);
            return R.success("发送成功");

        //需要将生成的验证码保存到session中
        }
        return R.error("发送失败");

    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        //获取手机号
        String phone =map.get("phone").toString();
        System.out.println("phone"+phone);
        //获取验证码
        String code=map.get("code").toString();
        //获取session保存的验证码
        String codeInSession = session.getAttribute(phone).toString();
        System.out.println(codeInSession);
        if(codeInSession!=null&&codeInSession.equals(code)){
            LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user==null){
                user=new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            log.info("执行到这了");
            return R.success(user);
        }
        return R.error("登录失败");
        //比对成功，则登录成功

        //判断手机号对应的用户是否为新用户，如果是新用户则自动完成注册
    }



    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
