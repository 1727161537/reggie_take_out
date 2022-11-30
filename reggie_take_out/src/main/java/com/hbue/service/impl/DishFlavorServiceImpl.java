package com.hbue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbue.entity.DishFlavor;
import com.hbue.mapper.DishFlavorMapper;
import com.hbue.service.DIshFlavorService;
import org.springframework.stereotype.Service;


@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DIshFlavorService {
}
