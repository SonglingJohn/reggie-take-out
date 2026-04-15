package top.sljiang.reggietakeout.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sljiang.reggietakeout.common.CustomException;
import top.sljiang.reggietakeout.dto.SetmealDto;
import top.sljiang.reggietakeout.entity.Setmeal;
import top.sljiang.reggietakeout.entity.SetmealDish;
import top.sljiang.reggietakeout.mapper.SetMealMapper;
import top.sljiang.reggietakeout.service.SetMealDishService;
import top.sljiang.reggietakeout.service.SetMealService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {

    @Resource
    private SetMealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {


        this.save(setmealDto);

        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);



    }

    @Override
    public void removeWithDish(List<Long> ids) {
        //构造查询条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        //查询状态为1（起售状态）的套餐
        queryWrapper.in(Setmeal::getId, ids);
        queryWrapper.eq(Setmeal::getStatus,1);

       int count = (int) this.count(queryWrapper);

        if (count > 0){
            throw new CustomException("当前套餐正在售卖中，不能删除");
        }

        //如果可以删除先删除套餐表中的数据
        this.removeByIds(ids);


        //删除关系表中的数据
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);
        log.info("queryWrapper:"+queryWrapper1);
        setmealDishService.remove(queryWrapper1);
    }
}
