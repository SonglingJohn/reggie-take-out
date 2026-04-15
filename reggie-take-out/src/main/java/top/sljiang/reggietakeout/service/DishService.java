package top.sljiang.reggietakeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sljiang.reggietakeout.dto.DishDto;
import top.sljiang.reggietakeout.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
   public void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    void updateStatusByIds(Integer status, String ids);
}
