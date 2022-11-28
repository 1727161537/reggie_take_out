package com.hbue.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbue.entity.Category;

public interface CategoryService extends IService<Category> {


    /**
     * 根据id来删除分类
     * 可以解决分类与其他数据绑定的问题
     * @param id
     */
    public void remove(Long id);
}
