package com.hbue.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hbue.dto.DishDto;
import com.hbue.entity.Dish;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(Long id);

    //更新菜品信息的同时更新口味信息
    public void updateWithFlavor(DishDto dishDto);
}
