package com.hbue.dto;

import com.hbue.entity.Setmeal;
import com.hbue.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
