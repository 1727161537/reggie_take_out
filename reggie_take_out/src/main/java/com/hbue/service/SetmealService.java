package com.hbue.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hbue.dto.SetmealDto;
import com.hbue.entity.Setmeal;

public interface SetmealService  extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);
}
