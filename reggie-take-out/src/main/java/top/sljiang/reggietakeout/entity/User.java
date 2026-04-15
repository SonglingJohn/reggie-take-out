package top.sljiang.reggietakeout.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 用户实体类
 */
@Data
// 对应数据库表名，若表名不一致请修改
public class User {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)  // 对应bigint自增主键，若为雪花算法/其他可修改type
    private Long id;

    /**
     * 用户名
     */
    private String name;

    /**
     * 手机号（唯一索引）
     */
    private String phone;

    /**
     * 性别
     */
    private String sex;

    /**
     * 身份证号
     */
    private String idNumber; // 数据库id_number → 驼峰命名

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 状态（0正常/其他自定义），默认值0
     */
    private Integer status;
}