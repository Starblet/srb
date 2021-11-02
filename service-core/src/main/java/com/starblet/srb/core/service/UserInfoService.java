package com.starblet.srb.core.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starblet.srb.core.pojo.entity.UserInfo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starblet.srb.core.pojo.query.UserInfoQuery;
import com.starblet.srb.core.pojo.vo.LoginVO;
import com.starblet.srb.core.pojo.vo.RegisterVO;
import com.starblet.srb.core.pojo.vo.UserInfoVO;

/**
 * <p>
 * 用户基本信息 服务类
 * </p>
 *
 * @author starblet
 * @since 2021-07-28
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户注册
     * @param registerVO
     */
    void register(RegisterVO registerVO);

    /**
     * 校验手机号是否已被注册
     * @param mobile
     * @return
     */
    boolean userNotRegisted(String mobile);

    /**
     * 用户登录
     * @param loginVO
     * @param ip
     * @return
     */
    UserInfoVO login(LoginVO loginVO, String ip);

    /**
     * 会员列表显示
     * @param pageParam
     * @param userInfoQuery
     * @return
     */
    IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery);

    /**
     * 用户状态的改变（锁定和解锁）
     * @param id
     * @param status
     */
    void lock(Long id,Integer status);

    /**
     * 校验手机是否注册
     * @param mobile
     * @return
     */
    boolean checkMobile(String mobile);
}
