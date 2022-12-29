package com.hbue.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.hbue.dto.SetmealDto;
import com.hbue.entity.Setmeal;

import java.util.List;

public interface SetmealService  extends IService<Setmeal> {

    void saveWithDish(SetmealDto setmealDto);

    /**
     * 删除套餐,同时删除套餐和菜品的关联数据
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
