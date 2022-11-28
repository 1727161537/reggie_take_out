package com.hbue.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbue.common.CustomException;
import com.hbue.entity.Category;
import com.hbue.entity.Dish;
import com.hbue.entity.Setmeal;
import com.hbue.mapper.CategoryMapper;
import com.hbue.service.CategoryService;
import com.hbue.service.DishService;
import com.hbue.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {

    @Autowired
    private DishService dishService;


    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 根据id删除分类
     *
     * @param id
     */
    @Override
    public void remove(Long id) {
        //添加查询条件,根据分类id查询菜品数据
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count = dishService.count(dishLambdaQueryWrapper);
        //如果数量大于0,即菜品绑定了该分类
        if (count > 0) {
            //抛出业务异常
            throw new CustomException("当前分类下关联了菜品,不能删除");
        }

        //查询当前分类是否关联了套餐,若关联则抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        //如果数量大于0,即表示菜品绑定了套餐
        if (count1 > 0) {
            throw new CustomException("该分类绑定了套餐,无法删除");
        }
        //如果都没有绑定,则删除该分类
        categoryService.removeById(id);
    }
}
