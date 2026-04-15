package top.sljiang.reggietakeout.utils;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailCodeUtil {

    private final JavaMailSender mailSender;

    // 发件人邮箱（必须和配置里的 username 一致）
    private static final String FROM = "3088367412@qq.com";

    /**
     * 发送邮箱验证码
     */
    public void sendEmailCode(String toEmail, String code) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom(FROM);
        helper.setTo(toEmail);
        helper.setSubject("登录验证码");
        helper.setText("您的验证码是：" + code + "，5分钟内有效！", true);

        mailSender.send(message);
    }
}
