package top.sljiang.reggietakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.sljiang.reggietakeout.common.R;
import top.sljiang.reggietakeout.entity.Category;
import top.sljiang.reggietakeout.service.CategoryService;

import java.util.List;
@Slf4j
@RequestMapping("/category")
@RestController
public class CategoryController {

    @Resource
    private CategoryService categoryService;

//    @RequestMapping("/list")
//    public R<List<Category>> list() {
//        return R.success(categoryService.list());
//    }
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        Page<Category> pageInfo = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> queryWrapper =new LambdaQueryWrapper<>();
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }
    @DeleteMapping
    public R<String> delete(Long ids) {
        log.info("删除分类，id为：{}", ids);
        categoryService.remove(ids);
        return R.success("删除成功");
    }

    /**
     * 修改分类
     */
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息：{}", category);
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    /**
     * 根据 type 查询分类
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category  category) {
        //条件构造器
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }







}
