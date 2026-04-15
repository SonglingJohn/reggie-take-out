package top.sljiang.reggietakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sljiang.reggietakeout.entity.ShoppingCart;
import top.sljiang.reggietakeout.mapper.ShoppingCartMapper;
import top.sljiang.reggietakeout.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
