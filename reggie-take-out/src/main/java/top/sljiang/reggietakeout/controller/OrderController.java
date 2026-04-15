package top.sljiang.reggietakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import top.sljiang.reggietakeout.common.BaseContext;
import top.sljiang.reggietakeout.common.R;
import top.sljiang.reggietakeout.dto.OrdersDto;
import top.sljiang.reggietakeout.entity.OrderDetail;
import top.sljiang.reggietakeout.entity.Orders;
import top.sljiang.reggietakeout.service.OrderDetailService;
import top.sljiang.reggietakeout.service.OrdersService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单
     *
     * @param orders
     * @return
     */
    @PostMapping("submit")
    public R<String> submit(@RequestBody Orders orders) {
        log.info("订单数据：{}", orders);
        ordersService.submit(orders);
        return R.success("下单成功");
    }

    @GetMapping("/userPage")
    public R<Page<OrdersDto>> userPage(int page, int pageSize) {
        Page<OrdersDto> ordersDtoPage = new Page<>(page, pageSize);


        return R.success(ordersService.userPage(ordersDtoPage));
    }


}
