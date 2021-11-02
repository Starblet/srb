package com.starblet.srb.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starblet.common.exception.Assert;
import com.starblet.common.result.ResponseEnum;
import com.starblet.common.util.MD5;
import com.starblet.srb.core.mapper.UserAccountMapper;
import com.starblet.srb.core.mapper.UserLoginRecordMapper;
import com.starblet.srb.core.pojo.entity.UserAccount;
import com.starblet.srb.core.pojo.entity.UserInfo;
import com.starblet.srb.core.mapper.UserInfoMapper;
import com.starblet.srb.core.pojo.entity.UserLoginRecord;
import com.starblet.srb.core.pojo.query.UserInfoQuery;
import com.starblet.srb.core.pojo.vo.LoginVO;
import com.starblet.srb.core.pojo.vo.RegisterVO;
import com.starblet.srb.core.pojo.vo.UserInfoVO;
import com.starblet.srb.core.service.UserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starblet.srb.core.util.JwtUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 * 用户基本信息 服务实现类
 * </p>
 *
 * @author starblet
 * @since 2021-07-28
 */
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private UserAccountMapper userAccountMapper;

    @Resource
    private UserLoginRecordMapper userLoginRecordMapper;

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void register(RegisterVO registerVO) {

        // 判断用户是否被注册
        boolean userNotRegisted = userNotRegisted(registerVO.getMobile());
        Assert.isTrue(userNotRegisted,ResponseEnum.MOBILE_EXIST_ERROR);

        // 插入用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserType(registerVO.getUserType()); // 设置类型
        userInfo.setNickName(registerVO.getMobile()); // 设置昵称，默认为手机号
        userInfo.setName(registerVO.getMobile()); // 设置名字，默认为手机号
        userInfo.setMobile(registerVO.getMobile()); // 设置手机号
        userInfo.setPassword(MD5.encrypt(registerVO.getPassword())); // 设置加密的密码
        userInfo.setStatus(UserInfo.STATUS_NORMAL); //正常
        // 设置头像
        //https://srb-star.oss-cn-shanghai.aliyuncs.com/avatar/20210911/1d4d3c83-dd86-49e2-98e2-0cb22a952764.jpg
        userInfo.setHeadImg("https://srb-star.oss-cn-shanghai.aliyuncs.com/avatar/20210911/1d4d3c83-dd86-49e2-98e2-0cb22a952764.jpg");
        baseMapper.insert(userInfo);

        // 创建会员账号
        UserAccount userAccount = new UserAccount();
        userAccount.setUserId(userInfo.getId());
        userAccountMapper.insert(userAccount);

    }

    @Override
    public boolean userNotRegisted(String mobile) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",mobile);
        Integer count = baseMapper.selectCount(userInfoQueryWrapper);
//        Assert.isTrue(count == 0, ResponseEnum.MOBILE_EXIST_ERROR);
        if (count == 0) {
            return true;
        }
        return false;
    }

    @Override
    public UserInfoVO login(LoginVO loginVO, String ip) {

        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();
        Integer userType = loginVO.getUserType();

        // 查询数据库获取信息
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper
                .eq("mobile",mobile)
                .eq("user_type",userType);
        UserInfo userInfo = baseMapper.selectOne(userInfoQueryWrapper);

        // 判断手机是否被注册
        Assert.notNull(userInfo,ResponseEnum.LOGIN_MOBILE_ERROR);

        // 判断密码是否正确
        Assert.equals(MD5.encrypt(password),userInfo.getPassword(),ResponseEnum.LOGIN_PASSWORD_ERROR);

        // 判断用户是否被禁用
        Assert.equals(userInfo.getStatus(),UserInfo.STATUS_NORMAL,ResponseEnum.LOGIN_LOKED_ERROR);

        // 记录登录日志
        UserLoginRecord userLoginRecord = new UserLoginRecord();
        userLoginRecord.setUserId(userInfo.getId());
        userLoginRecord.setIp(ip);
        // LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        userLoginRecord.setCreateTime(LocalDateTime.now());
        userLoginRecordMapper.insert(userLoginRecord);

        // 生成token
        String token = JwtUtils.createToken(userInfo.getId(), userInfo.getName());

        // 创建返回对象UserInfoVO并填充
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setMobile(mobile);
        userInfoVO.setToken(token);
        userInfoVO.setName(userInfo.getName());
        userInfoVO.setHeadImg(userInfo.getHeadImg());
        userInfoVO.setUserType(userInfo.getUserType());
        userInfoVO.setNickName(userInfo.getNickName());

        // 返回
        return userInfoVO;
    }

    @Override
    public IPage<UserInfo> listPage(Page<UserInfo> pageParam, UserInfoQuery userInfoQuery) {

        if (userInfoQuery == null) {
            return baseMapper.selectPage(pageParam,null);
        }

        String mobile = userInfoQuery.getMobile();
        Integer userType = userInfoQuery.getUserType();
        Integer status = userInfoQuery.getStatus();

        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper
                .eq(StringUtils.isNotBlank(mobile),"mobile",mobile)
                .eq(status != null,"status",status)
                .eq(userType != null,"user_type",userType);

        return baseMapper.selectPage(pageParam,userInfoQueryWrapper);
    }

    @Override
    public void lock(Long id, Integer status) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setStatus(status);
        baseMapper.updateById(userInfo);
    }

    @Override
    public boolean checkMobile(String mobile) {
        QueryWrapper<UserInfo> userInfoQueryWrapper = new QueryWrapper<>();
        userInfoQueryWrapper.eq("mobile",mobile);

        Integer count = baseMapper.selectCount(userInfoQueryWrapper);
        return count > 0;
    }


}
