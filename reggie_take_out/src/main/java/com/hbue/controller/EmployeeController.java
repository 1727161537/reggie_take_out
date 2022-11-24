package com.hbue.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hbue.common.R;
import com.hbue.entity.Employee;
import com.hbue.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 管理员登录
     *
     * @param employee
     * @param request
     * @return
     */
    @PostMapping("/login")
    public R<Employee> login(@RequestBody Employee employee, HttpServletRequest request) {
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(wrapper);
        //如果没有查询到结果
        if (emp == null) {
            return R.error("登录失败!");
        }
        //如果密码不一致
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误,登录失败!");
        }
        //如果账号是禁用状态
        if (emp.getStatus() == 0) {
            return R.error("账号已禁用!");
        }
        //登录成功,将员工的id存入session并返回登录成功结果
        HttpSession session = request.getSession();
        session.setAttribute("employee", emp.getId());
        session.setMaxInactiveInterval(1000*60*60);
        return R.success(emp);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理Session中保存的id
        HttpSession session = request.getSession();
        session.removeAttribute("employee");
        return R.success("退出成功！");
    }
}
