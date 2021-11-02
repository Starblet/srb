package com.starblet.srb.sms.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author starblet
 * @create 2021-08-29 21:42
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@ConfigurationProperties(prefix = "short.message")
// 实现InitializingBean接口是为了在初始化属性之后，在方法afterPropertiesSet()中设置自定义属性
public class ShortMessageProperties implements InitializingBean {

    private String host;
    private String path;
    private String method;
    private String appCode;
    private String templateId;

    public static String HOST;
    public static String PATH;
    public static String METHOD;
    public static String APP_CODE;
    public static String TEMPLATE_ID;

    @Override
    public void afterPropertiesSet() throws Exception {
        HOST = host;
        PATH = path;
        METHOD = method;
        APP_CODE = appCode;
        TEMPLATE_ID = templateId;
    }
}
