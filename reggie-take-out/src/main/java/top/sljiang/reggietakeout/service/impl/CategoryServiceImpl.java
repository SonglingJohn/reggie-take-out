package top.sljiang.reggietakeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import top.sljiang.reggietakeout.common.CustomException;
import top.sljiang.reggietakeout.entity.Category;
import top.sljiang.reggietakeout.entity.Dish;
import top.sljiang.reggietakeout.entity.Setmeal;
import top.sljiang.reggietakeout.mapper.CategoryMapper;
import top.sljiang.reggietakeout.service.CategoryService;
import top.sljiang.reggietakeout.service.DishService;
import top.sljiang.reggietakeout.service.SetMealService;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private DishService dishService;
    @Resource
    private SetMealService setMealService;

    /**
     * 删除分类信息
     * 在删除前会检查分类是否关联菜品或套餐，如果存在关联则抛出业务异常
     *
     * @param id 分类 ID
     */
    @Override
    public void remove(Long id) {

        // 查询当前分类是否关联了菜品，如果已经关联，则抛出一个业务异常
        LambdaQueryWrapper<Dish> DishQueryWrapper =new LambdaQueryWrapper<>();
        DishQueryWrapper.eq(Dish::getCategoryId, id);
       Long count = dishService.count(DishQueryWrapper);

        if (count > 0) {
            //已经关联菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        // 查询当前分类是否关联了套餐，如果已经关联，则抛出一个业务异常
        LambdaQueryWrapper<Setmeal> MealQueryWrapper =new LambdaQueryWrapper<>();
        MealQueryWrapper.eq(Setmeal::getCategoryId, id);
        Long count1 = setMealService.count(MealQueryWrapper);

        if (count > 0) {
            //已经关联套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        //正常删除分类
        super.removeById(id);

    }
}
