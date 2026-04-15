package top.sljiang.reggietakeout.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import top.sljiang.reggietakeout.dto.OrdersDto;
import top.sljiang.reggietakeout.entity.Orders;

public interface OrdersService extends IService<Orders> {

    void submit(Orders orders);

    Page<OrdersDto> userPage(Page<OrdersDto> ordersDtoPage);
}
