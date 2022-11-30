package com.hbue.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hbue.dto.DishDto;
import com.hbue.entity.Dish;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);
}
