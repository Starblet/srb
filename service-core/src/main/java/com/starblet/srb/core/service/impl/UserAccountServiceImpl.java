package com.starblet.srb.core.service.impl;

import com.starblet.srb.core.pojo.entity.UserAccount;
import com.starblet.srb.core.mapper.UserAccountMapper;
import com.starblet.srb.core.service.UserAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账户 服务实现类
 * </p>
 *
 * @author starblet
 * @since 2021-07-28
 */
@Service
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccount> implements UserAccountService {

}
