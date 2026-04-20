package top.sljiang.reggietakeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.sljiang.reggietakeout.common.R;
import top.sljiang.reggietakeout.entity.User;
import top.sljiang.reggietakeout.service.UserService;
import top.sljiang.reggietakeout.utils.EmailCodeUtil;
import top.sljiang.reggietakeout.utils.SpugSmsUtil;
import top.sljiang.reggietakeout.utils.ValidateCodeUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

@Resource
    private UserService userService;
    @Autowired
    private SpugSmsUtil spugSmsUtil;
    @Autowired
    private EmailCodeUtil emailCodeUtil;
    private final RedisTemplate redisTemplate;

    public UserController(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user) throws Exception{
    //获取手机号
    String phone = user.getPhone();
    if (phone == null) {
        return R.error("手机号有误");
    }else if (phone != null) {
        //生成随机的4位验证码
        String code = ValidateCodeUtils.generateValidateCode(4).toString();
        log.info("code={}",code);
        //调用短信api发送验证码
       emailCodeUtil.sendEmailCode(phone,code);
        //保存手机号和验证码到redis,设置有效期5分钟
        redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
        return R.success("验证码发送成功");
    }
        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info("用户信息:{}",map.toString());
        //获取手机号
        String phone = map.get("phone").toString();

        //获取验证码
        String code = map.get("code").toString();

        //和redis中的验证码进行对比
        if(redisTemplate.opsForValue().get(phone).equals(code)){
            //如果比对成功，则登录成功

            //判断当前手机号对应用户是否为新用户，如果为新用户，就自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(User::getPhone,phone);
            User user = userService.getOne(queryWrapper);
            if(user == null){
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user",user.getId());
            session.removeAttribute(phone);

            //如果用户登录成功，删除redis中的验证码
            redisTemplate.delete(phone);
            return R.success(user);

        }





        return R.error("验证码错误");
    }





}
