package com.starblet.srb.sms.client.fallback;

import com.starblet.srb.sms.client.CoreUserInfoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 远程服务的备用方案
 *
 * @author starblet
 * @create 2021-10-10 12:48
 */

@Service
@Slf4j
public class CoreUserInfoClientFallback implements CoreUserInfoClient {
    @Override
    public boolean checkMobile(String mobile) {
        return false; //如果远程服务宕机了，就默认手机没被注册，后续也会有手机号的校验
    }
}
