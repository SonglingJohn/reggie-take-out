package top.sljiang.reggietakeout.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Component
public class SpugSmsUtil {

    @Value("${spug.template.id}")
    private String templateId;

    @Value("${spug.api.url}")
    private String apiUrl;

    @Resource
    private RestTemplate restTemplate;

    /**
     * 发送短信验证码
     * @param phone 手机号
     * @param code 验证码
     * @return 接口响应结果
     */
    public String sendSms(String phone, String code) {
        // 拼接完整 URL
        String url = String.format("%s/%s", apiUrl, templateId);

        // 请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 请求体
        Map<String, String> body = Map.of(
                "name", "Java后端登录测试",
                "code", code,
                "targets", phone
        );

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
        // 发送 POST 请求
        return restTemplate.postForObject(url, request, String.class);
    }
}