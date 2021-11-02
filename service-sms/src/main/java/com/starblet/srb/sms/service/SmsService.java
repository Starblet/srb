package com.starblet.srb.sms.service;

/**
 * @author starblet
 * @create 2021-08-31 23:05
 */
public interface SmsService {

    void send(String host,String path,String method,String appCode,String phoneNum,String templateId,String code);
}
