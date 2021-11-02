package com.starblet.srb.sms;

import com.starblet.common.util.RandomUtils;
import com.starblet.srb.sms.service.SmsService;
import com.starblet.srb.sms.util.ShortMessageProperties;
import com.starblet.srb.sms.util.SmsProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootVersion;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.SpringVersion;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author starblet
 * @create 2021-08-29 15:56
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UtilsTests {

    @Resource
    private SmsService smsService;

    @Test
    public void testProperties(){
        System.out.println(SmsProperties.KEY_ID);
        System.out.println(SmsProperties.KEY_SECRET);
        System.out.println(SmsProperties.REGION_Id);
        System.out.println(SmsProperties.TEMPLATE_CODE);
        System.out.println(SmsProperties.SIGN_NAME);
    }

    @Test
    public void testMessage() {
        System.out.println(ShortMessageProperties.APP_CODE);
        System.out.println(ShortMessageProperties.HOST);
        System.out.println(ShortMessageProperties.METHOD);
        System.out.println(ShortMessageProperties.PATH);
        System.out.println(ShortMessageProperties.TEMPLATE_ID);
    }

    @Test
    public void testSendMessage() {

        String code = RandomUtils.getSixBitRandom();
        System.out.println(code);
        smsService.send(ShortMessageProperties.HOST,ShortMessageProperties.PATH,ShortMessageProperties.METHOD,
                ShortMessageProperties.APP_CODE,"17766452323",ShortMessageProperties.TEMPLATE_ID, code);
    }

    @Test
    public void test() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now); // 2021-09-15T22:32:55.207
        String format = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        System.out.println( format);  // 2021-09-15 22:29:55

//        LocalDateTime parse = LocalDateTime.parse("yyyy-MM-dd HH:mm:ss");
        LocalDateTime parse = LocalDateTime.parse("2021-09-15T22:45:04.498");
        System.out.println(parse);
    }


}


