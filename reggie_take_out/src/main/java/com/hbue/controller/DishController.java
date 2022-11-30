package com.hbue.controller;


import com.hbue.common.R;
import com.hbue.dto.DishDto;
import com.hbue.service.DIshFlavorService;
import com.hbue.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {


    @Autowired
    private DishService dishService;

    @Autowired
    private DIshFlavorService dIshFlavorService;

    /**
     * 保存菜品信息
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        dishService.saveWithFlavor(dishDto);
        return R.success("保存菜品信息成功");
    }

}
