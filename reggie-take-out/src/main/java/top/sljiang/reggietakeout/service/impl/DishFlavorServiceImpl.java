package top.sljiang.reggietakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sljiang.reggietakeout.entity.DishFlavor;
import top.sljiang.reggietakeout.mapper.DishFlavorMapper;
import top.sljiang.reggietakeout.service.DishFlavorService;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService{
}
