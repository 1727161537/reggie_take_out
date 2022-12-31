package com.hbue.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hbue.common.R;
import com.hbue.entity.User;
import com.hbue.service.UserService;
import com.hbue.utils.SMSUtils;
import com.hbue.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 发送验证码
     * @param user
     * @param session
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session){
        //获取手机号
        String phone = user.getPhone();
        //判断手机号是否为空
        if (!StringUtils.isEmpty(phone)){
            //生成随机的4为验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            SMSUtils.sendMessage("瑞吉外卖","",phone,code);

            //将生成的验证码存入session中
//            session.setAttribute(phone,code);
            session.setAttribute(phone,"1234");
            return R.success("手机短信验证码发送成功!");
        }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map,HttpSession session){
        //获取手机号
        String phone = (String) map.get("phone");
        //获取验证码
        String code = (String) map.get("code");
        //从session中获取到保存的验证码
        String sessionCode = (String) session.getAttribute(phone);
        //进行验证码的校对
        if (sessionCode != null && sessionCode.equals(code)){
            //比对成功,则登录成功
            //构造条件
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            //如果用户不存在
            if(user == null){
                //则表示当前注册的手机号为新用户,自动完成注册
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            //登录成功后,将用户id存入到session中
            session.setAttribute("user",user.getId());
            return R.success(user);
        }
        return R.error("登录失败");
    }

}
