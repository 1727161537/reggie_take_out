package com.hbue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbue.dto.DishDto;
import com.hbue.entity.Dish;
import com.hbue.entity.DishFlavor;
import com.hbue.mapper.DishMapper;
import com.hbue.service.DIshFlavorService;
import com.hbue.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
        flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        //保存菜品口味数据到dish_flavor表
        dIshFlavorService.saveBatch(flavors);
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        //查询当前菜品对应的口味信息,从dish_flavor表中查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavorList = dIshFlavorService.list(queryWrapper);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }

    /**
     * 更新菜品信息和对应的口味信息
     *
     * @param dishDto
     */
    @Override
    public void updateWithFlavor(DishDto dishDto) {
        //更新菜品信息
        this.updateById(dishDto);
        //清理当前菜品对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dIshFlavorService.remove(queryWrapper);
        //将添加的口味信息重新插入数据库dish_flavor表中
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());
        dIshFlavorService.saveBatch(flavors);
    }
}
