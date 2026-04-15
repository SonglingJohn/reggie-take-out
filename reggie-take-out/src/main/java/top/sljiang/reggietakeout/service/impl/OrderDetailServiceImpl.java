package top.sljiang.reggietakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sljiang.reggietakeout.entity.OrderDetail;
import top.sljiang.reggietakeout.mapper.OrderDetailMapper;
import top.sljiang.reggietakeout.service.OrderDetailService;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
