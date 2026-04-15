package top.sljiang.reggietakeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.sljiang.reggietakeout.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
