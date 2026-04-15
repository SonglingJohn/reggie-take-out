package top.sljiang.reggietakeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.sljiang.reggietakeout.entity.AddressBook;
import top.sljiang.reggietakeout.mapper.AddressBookMapper;
import top.sljiang.reggietakeout.service.AddressBookService;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
   implements AddressBookService {
}
