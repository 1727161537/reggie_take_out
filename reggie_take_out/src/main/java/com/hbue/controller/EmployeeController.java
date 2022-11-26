package com.hbue.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hbue.common.R;
import com.hbue.entity.Employee;
import com.hbue.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

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
        session.setMaxInactiveInterval(1000 * 60 * 60);
        return R.success(emp);
    }

    /**
     * 退出登录
     *
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

    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        //设置初始密码123456,需要进行md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置员工的创建时间和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置员工的创建人
        //获取当前登录用户的id
        Long employeeID = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(employeeID);
        employee.setUpdateUser(employeeID);
        //调用service层的方法,来保存数据
        employeeService.save(employee);
        return R.success("新增员工成功");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        //构造分页构造器
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        wrapper.like(!StringUtils.isEmpty(name), Employee::getName, name);
        //添加排序规则
        wrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo, wrapper);
        return R.success(pageInfo);
    }

    /**
     * 通用修改功能
     * 1.修改用户的状态
     * 2.修改用户的信息
     *
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Employee employee, HttpServletRequest request) {
        //获取当前用户的id
        Long employeeId = (Long) request.getSession().getAttribute("employee");
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(employeeId);
        //修改employee的数据
        employeeService.updateById(employee);
        //返回结果
        return R.success("员工信息修改成功!");
    }

    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        Employee employee = employeeService.getById(id);
        //如果存在该员工
        if (employee != null){
            //返回结果
            return R.success(employee);
        }
        return R.error("没有找到该员工");
    }

}
