package top.sljiang.reggietakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.stereotype.Service;
import top.sljiang.reggietakeout.entity.User;
import top.sljiang.reggietakeout.mapper.UserMapper;
import top.sljiang.reggietakeout.service.UserService;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
