package top.sljiang.reggietakeout.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.sljiang.reggietakeout.entity.Dish;
import top.sljiang.reggietakeout.entity.DishFlavor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sire
 */
@Data
public class DishDto extends Dish {

    /**
     * 菜品口味
     */
    private List<DishFlavor> flavors = new ArrayList<>();

    /**
     * 菜品名称
     */
    private String categoryName;

    /**
     * 份数
     */
    private Integer copies;
}
