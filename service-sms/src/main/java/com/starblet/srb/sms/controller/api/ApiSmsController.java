package com.starblet.srb.sms.controller.api;

import com.starblet.common.exception.Assert;
import com.starblet.common.exception.BusinessException;
import com.starblet.common.result.R;
import com.starblet.common.result.ResponseEnum;
import com.starblet.common.util.RandomUtils;
import com.starblet.common.util.RegexValidateUtils;
import com.starblet.srb.sms.client.CoreUserInfoClient;
import com.starblet.srb.sms.service.SmsService;
import com.starblet.srb.sms.util.ShortMessageProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author starblet
 * @create 2021-09-06 19:42
 */
@RestController
@RequestMapping("/api/sms")
@Api(tags = "短信管理")
//@CrossOrigin //跨域
@Slf4j
public class ApiSmsController {

    @Resource
    private SmsService smsService;

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private CoreUserInfoClient coreUserInfoClient;

    @ApiOperation("获取验证码")
    @GetMapping("/send/{mobile}")
    public R send(
            @ApiParam(value = "手机号",required = true)
            @PathVariable String mobile
    ) {
        // 在发送之前校验手机号
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);

        // 校验手机号是否正确
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);

        // 判断手机号是否被注册
        // 调用了service-core中的接口
        boolean result = coreUserInfoClient.checkMobile(mobile);
        Assert.isTrue(result == false,ResponseEnum.MOBILE_EXIST_ERROR);

        // 生成验证码
        String code = RandomUtils.getSixBitRandom();
        log.info("code = " + code);

        // 发送短信
        // 由于在service层已经做过错误处理，这里就不用进行catch
//        smsService.send(
//                ShortMessageProperties.HOST,
//                ShortMessageProperties.PATH,
//                ShortMessageProperties.METHOD,
//                ShortMessageProperties.APP_CODE,
//                mobile,
//                ShortMessageProperties.TEMPLATE_ID,
//                code
//                );

        // 将验证码存入redis，设置5分钟超时时间
//        redisTemplate.opsForValue().set("srb:sms:code:" + mobile, "111111", 5, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set("srb:sms:code:" + mobile, code, 1, TimeUnit.MINUTES);

        // 给前端返回发送成功
        return R.success().message("短信发送成功");
    }


}
