package com.hbue.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbue.entity.ShoppingCart;
import com.hbue.mapper.ShoppingCartMapper;
import com.hbue.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {


}
