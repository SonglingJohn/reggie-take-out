package top.sljiang.reggietakeout.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.sljiang.reggietakeout.common.BaseContext;
import top.sljiang.reggietakeout.common.R;
import top.sljiang.reggietakeout.entity.ShoppingCart;
import top.sljiang.reggietakeout.service.ShoppingCartService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info("添加购物车",shoppingCart);

        //设置用户ID，指定当前是哪个用户的
        Long userId= BaseContext.getCurrentId() ;
        shoppingCart.setUserId(userId);

        //查询当前菜品或者是套餐是否在购物车中
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,userId);
        Long dishId = shoppingCart.getDishId();
        if(dishId!=null){
            //添加的是菜品
            queryWrapper.eq(ShoppingCart::getDishId,dishId);

        }else{
            //添加的是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);

        //如果已经存在，则数量加一
        if (cartServiceOne!=null){
            Integer number = cartServiceOne.getNumber();
            cartServiceOne.setNumber(number+1);
            shoppingCartService.updateById(cartServiceOne);

        }else{
            //如果不存在，则添加到购物车，数量默认是1
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            cartServiceOne = shoppingCart;
        }


        return R.success(cartServiceOne);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("查看购物车");
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping("/sub")
    public R<String> delete(@RequestBody ShoppingCart shoppingCart) {
        log.info("删除购物车", shoppingCart.toString());
        //获取当前用户ID
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId, userId);
        Long dishId = shoppingCart.getDishId();
        //如果这是菜品
        if(dishId!=null){
            queryWrapper.eq(ShoppingCart::getDishId, dishId);
        }else{
            //如果这是套餐
            queryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart cartServiceOne = shoppingCartService.getOne(queryWrapper);
        //判断购物车中是否有该菜品的数量
        if (cartServiceOne == null) {
            return R.error("购物车中无此商品");
        }

        if(cartServiceOne.getNumber()>1){
            //如果>1，则数量减一
            cartServiceOne.setNumber(cartServiceOne.getNumber()-1);
            shoppingCartService.updateById(cartServiceOne);
        }else{
            //否则，则删除
            shoppingCartService.removeById(cartServiceOne);
        }

        return R.success("删除成功");
    }



    @DeleteMapping("/clean")
    public R<String> clean(){
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");
    }
}
