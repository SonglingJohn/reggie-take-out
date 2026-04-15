package top.sljiang.reggietakeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sljiang.reggietakeout.common.BaseContext;
import top.sljiang.reggietakeout.common.CustomException;
import top.sljiang.reggietakeout.dto.OrdersDto;
import top.sljiang.reggietakeout.entity.*;
import top.sljiang.reggietakeout.mapper.OrdersMapper;
import top.sljiang.reggietakeout.service.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {
    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;


    @Override
    @Transactional
    public void submit(Orders orders) {
        //获取当前用户id
        Long userId = BaseContext.getCurrentId();

        //查询当前用户的购物车数据
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(queryWrapper);
        if (shoppingCarts == null || shoppingCarts.size() == 0) {
            throw new CustomException("购物车为空，不能下单");
        }
        //查询用户数据
        User user = userService.getById(userId);

        //查询地址数据
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        if (addressBook == null){
            throw new CustomException("地址信息有误，不能下单");
        }
        //整理订单所需数据
        long orderId = IdWorker.getId();//订单号

        //计算金额
        AtomicInteger amount = new AtomicInteger(0);
      List<OrderDetail> orderDetails =  shoppingCarts.stream().map(item -> {
          OrderDetail orderDetail = new OrderDetail();
          orderDetail.setOrderId(orderId);
          orderDetail.setName(item.getName());
          orderDetail.setDishId(item.getDishId());
          orderDetail.setSetmealId(item.getSetmealId());
          orderDetail.setDishFlavor(item.getDishFlavor());
          orderDetail.setNumber(item.getNumber());
          orderDetail.setAmount(item.getAmount());
          orderDetail.setImage(item.getImage());
          amount.addAndGet(item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue());
          return orderDetail;


        }).collect(Collectors.toList());
        //向订单表插入数据，一条数据
        orders.setNumber(String.valueOf(orderId));
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress(
                (addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                        + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                        + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                        + (addressBook.getDetail() == null ? "" : addressBook.getDetail())
        );

        this.save(orders);

        //像订单详细表插入数据，多条数据
        orderDetailService.saveBatch(orderDetails);

        //清空购物车数据
        shoppingCartService.remove(queryWrapper);

    }

    @Override
    @Transactional
    public Page<OrdersDto> userPage(Page<OrdersDto> ordersDtoPage) {
//        //获取用户订单数据
//        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
//        List<Orders> orders = this.list(queryWrapper);
//        BeanUtils.copyProperties(orders, ordersDtoPage.getRecords());
//       List<OrdersDto> ordersDtoList  = ordersDtoPage.getRecords().stream().map(item -> {
//           //通过订单id查询订单详细数据
//            LambdaQueryWrapper<OrderDetail> queryWrapper1 = new LambdaQueryWrapper<>();
//            queryWrapper1.eq(OrderDetail::getOrderId, item.getId());
//            List<OrderDetail> orderDetails = orderDetailService.list(queryWrapper1);
//            OrdersDto ordersDto = new OrdersDto();
//            BeanUtils.copyProperties(item, ordersDto);
//            ordersDto.setOrderDetails(orderDetails);
//            return ordersDto;
//        }).collect(Collectors.toList());
//
//       ordersDtoPage.setRecords(ordersDtoList);
//
//       return ordersDtoPage;
        // 1. 获取当前登录用户ID
        Long userId = BaseContext.getCurrentId();

        // 2. 构建查询条件：只查当前用户的订单
        LambdaQueryWrapper<Orders> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Orders::getUserId, userId);
        // 按下单时间倒序（最新的在前）
        queryWrapper.orderByDesc(Orders::getOrderTime);

        // 3. 真正分页查询订单（关键！）
        Page<Orders> ordersPage = this.page(new Page<>(ordersDtoPage.getCurrent(), ordersDtoPage.getSize()), queryWrapper);

        // 4. 把 Orders 转换成 OrdersDto，并设置订单明细
        List<OrdersDto> ordersDtoList = ordersPage.getRecords().stream().map(orders -> {
            OrdersDto ordersDto = new OrdersDto();

            // 复制订单基础信息
            BeanUtils.copyProperties(orders, ordersDto);

            // 查询订单明细
            LambdaQueryWrapper<OrderDetail> detailQueryWrapper = new LambdaQueryWrapper<>();
            detailQueryWrapper.eq(OrderDetail::getOrderId, orders.getId());
            List<OrderDetail> orderDetails = orderDetailService.list(detailQueryWrapper);

            // 设置订单明细到DTO
            ordersDto.setOrderDetails(orderDetails);
            return ordersDto;
        }).collect(Collectors.toList());

        // 5. 把最终数据设置到分页对象
        ordersDtoPage.setRecords(ordersDtoList);
        ordersDtoPage.setTotal(ordersPage.getTotal()); // 设置总条数（必须！）

        return ordersDtoPage;
    }
}
