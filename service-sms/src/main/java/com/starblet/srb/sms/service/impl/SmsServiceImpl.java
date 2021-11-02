package com.starblet.srb.sms.service.impl;

import com.google.gson.Gson;
import com.starblet.common.exception.Assert;
import com.starblet.common.exception.BusinessException;
import com.starblet.common.result.ResponseEnum;
import com.starblet.srb.sms.service.SmsService;
import com.starblet.srb.sms.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author starblet
 * @create 2021-08-31 23:05
 */

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {

    @Override
    public void send(String host, String path, String method, String appCode, String phoneNum, String templateId,String code) {
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appCode);
        Map<String, String> querys = new HashMap<String, String>();
        querys.put("mobile", phoneNum);

        // 要发送的验证码
//        StringBuilder builder = new StringBuilder();
//        for (int i = 0; i < 6; i++) {
//            builder.append(new Random().nextInt(10));
//        }
//        String code = builder.toString();

        querys.put("tag", code);
        querys.put("templateId", templateId);
        Map<String, String> bodys = new HashMap<>();

        Gson gson = new Gson();

        try {
            log.info("调用第三方短信服务");
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            System.out.println(response.toString());
            //获取response的body
            System.out.println(EntityUtils.toString(response.getEntity()));
//            String data = EntityUtils.toString(response.getEntity());

            // 将JSON数据转化为map集合
            /*
            {
                "msg": "templateId 无效",
                "success": false,
                "code": 400,
                "data": {}
            }
             */
//            HashMap<String,String> resultMap = gson.fromJson(data, HashMap.class);
//            String resultCode = resultMap.get("code");
//            String msg = resultMap.get("msg");

            // 处理信息
//            Assert.equals("200",resultCode, ResponseEnum.ALIYUN_SMS_ERROR);

        } catch (Exception e) {
//            log.info("短信发送失败");
            throw new BusinessException(ResponseEnum.ALIYUN_SMS_ERROR,e);
        }
    }

}
