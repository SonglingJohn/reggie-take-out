package top.sljiang.reggietakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sljiang.reggietakeout.entity.Employee;
import top.sljiang.reggietakeout.mapper.EmployeeMapper;
import top.sljiang.reggietakeout.service.EmployeeService;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
