package top.sljiang.reggietakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sljiang.reggietakeout.entity.Dish;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
