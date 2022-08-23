package com.che.qia.dto;

import com.che.qia.entity.Setmeal;
import com.che.qia.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
