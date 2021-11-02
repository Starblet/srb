package com.starblet.srb.core.service;

import com.starblet.srb.core.pojo.entity.UserLoginRecord;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 用户登录记录表 服务类
 * </p>
 *
 * @author starblet
 * @since 2021-07-28
 */
public interface UserLoginRecordService extends IService<UserLoginRecord> {

    /**
     * 根据用户id展示用户近50次的登录信息
     * @param userId
     * @return
     */
    List<UserLoginRecord> listTop50(Long userId);
}
