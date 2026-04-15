package top.sljiang.reggietakeout.dto;


import lombok.Data;
import top.sljiang.reggietakeout.entity.Setmeal;
import top.sljiang.reggietakeout.entity.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
