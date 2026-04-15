package top.sljiang.reggietakeout.utils;

import java.util.Random;

/**
 * 验证码生成工具类
 */
public class ValidateCodeUtils {

    private static final Random random = new Random();

    /**
     * 生成数字验证码
     * @param length 长度 4 或 6
     * @return 数字验证码字符串
     */
    public static String generateValidateCode(int length) {
        if (length != 4 && length != 6) {
            throw new IllegalArgumentException("验证码长度只能为4或6");
        }

        int code;
        if (length == 4) {
            code = 1000 + random.nextInt(9000); // 1000 ~ 9999
        } else {
            code = 100000 + random.nextInt(900000); // 100000 ~ 999999
        }

        return String.valueOf(code);
    }

    // 测试
    public static void main(String[] args) {
        System.out.println("4位验证码：" + generateValidateCode(4));
        System.out.println("6位验证码：" + generateValidateCode(6));
    }
}
