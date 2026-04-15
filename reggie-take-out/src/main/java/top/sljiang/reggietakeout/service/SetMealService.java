package top.sljiang.reggietakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.sljiang.reggietakeout.dto.SetmealDto;
import top.sljiang.reggietakeout.entity.Setmeal;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setmealDto);

    void removeWithDish(List<Long> ids);
}
