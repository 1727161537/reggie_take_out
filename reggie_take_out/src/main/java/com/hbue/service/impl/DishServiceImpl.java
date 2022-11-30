package com.hbue.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbue.dto.DishDto;
import com.hbue.entity.Dish;
import com.hbue.entity.DishFlavor;
import com.hbue.mapper.DishMapper;
import com.hbue.service.DIshFlavorService;
import com.hbue.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DIshFlavorService dIshFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到菜品表
        this.save(dishDto);

        //获取菜品id
        Long id = dishDto.getId();
        //获取菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.stream().map((item)->{
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到dish_flavor表
        dIshFlavorService.saveBatch(flavors);
    }
}
