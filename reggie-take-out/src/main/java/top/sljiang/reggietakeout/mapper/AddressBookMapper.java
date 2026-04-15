package top.sljiang.reggietakeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.sljiang.reggietakeout.entity.AddressBook;

@Mapper
@Repository
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
