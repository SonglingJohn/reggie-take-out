package top.sljiang.reggietakeout.dto;

import lombok.Data;
import top.sljiang.reggietakeout.entity.OrderDetail;
import top.sljiang.reggietakeout.entity.Orders;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrdersDto extends Orders {
    private List<OrderDetail> orderDetails = new ArrayList<>();
}
