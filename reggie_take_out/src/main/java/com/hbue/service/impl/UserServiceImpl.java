package com.hbue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbue.entity.User;
import com.hbue.mapper.UserMapper;
import com.hbue.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {


}
