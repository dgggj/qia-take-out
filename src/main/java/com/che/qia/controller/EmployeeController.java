package com.che.qia.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.che.qia.common.R;
import com.che.qia.entity.Employee;
import com.che.qia.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
  
    /**
     * @description:登录
     * @author: jqx
     * @date: 2022/8/18 13:00
     * @param: Employee
     * @return:
     **/
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
        //1将页面提交的密码进行md5加密处理
        String password=employee.getPassword();
        password= DigestUtils.md5DigestAsHex(password.getBytes());

        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        //如果没有查询到，则返回登录失败
        if(emp==null){
            return R.error("用户不存在");
        }
        //密码比对，如果不一致，返回密码错误
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }
        //查看员工状态，如果是已禁用状态，则返回登录失败，已禁用状态
        if(emp.getStatus()==0){
            return R.error("账号已禁用");
        }
        //登录成功将员工的id存入Session中，并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());
        return R.success(emp);
    }
    /**
     * @description:登出
     * @author: jqx
     * @date: 2022/8/18 14:05
     * @param:
     * @return:
     **/
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        //清理Session中保存的当前员工id
        request.getSession().removeAttribute("employee");
        return R.success("退出成功");
    }
    /**
     * @description:新增员工
     * @author: che
     * @date: 2022/8/18 16:26
     * @param: Employee
     * @return:
     **/
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工信息{}",employee.toString());
        //设置初始密码，并且需要md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.save(employee);
        return R.success("添加员工成功");


    }

    /**
     * @description:员工信息分页查询
     * @author: che
     * @date: 2022/8/18 21:20
     * @param:
     * @return: Page
     **/
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page={},pageSize={},name={}",page,pageSize,name);

        //分页构造器
        Page pageInfo = new Page(page, pageSize);

        //条件分页构造器
        LambdaQueryWrapper<Employee> queryWrapper=new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }
    /**
     * @description:更新
     * @author: che
     * @date: 2022/8/18 22:07
     * @param:
     * @return:
     **/

    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("员工信息修改成功");
    }
    /**
     * @description:根据id查询员工信息
     * @author: che
     * @date: 2022/8/18 22:59
     * @param:
     * @return:
     **/

    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable("id") Long id){
        Employee employee = employeeService.getById(id);
        if(employee!=null){
            return R.success(employee);
        }
       return R.error("请求失败");
    }





}
