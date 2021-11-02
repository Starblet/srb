package com.starblet.srb.core.controller.api;


import com.starblet.common.exception.Assert;
import com.starblet.common.result.R;
import com.starblet.common.result.ResponseEnum;
import com.starblet.common.util.RegexValidateUtils;
import com.starblet.srb.core.pojo.entity.UserInfo;
import com.starblet.srb.core.pojo.vo.LoginVO;
import com.starblet.srb.core.pojo.vo.RegisterVO;
import com.starblet.srb.core.pojo.vo.UserInfoVO;
import com.starblet.srb.core.service.UserInfoService;
import com.starblet.srb.core.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户基本信息 前端控制器
 * </p>
 *
 * @author starblet
 * @since 2021-07-28
 */
@Api(tags = "会员接口")
@RestController
@RequestMapping("/api/core/userInfo")
@Slf4j
//@CrossOrigin
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @Resource
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "会员注册")
    @PostMapping("/register")
    public R register(@RequestBody RegisterVO registerVO) {

        String mobile = registerVO.getMobile();
        String code = registerVO.getCode();
        String password = registerVO.getPassword();

        // 校验手机号
        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);

        // 校验密码
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);

        // 校验验证码
        String codeGen = (String) redisTemplate.opsForValue().get("srb:sms:code:" + mobile);
        Assert.notEmpty(codeGen,ResponseEnum.CODE_EXPIRE);
        Assert.equals(code,codeGen,ResponseEnum.CODE_ERROR);

        // 调用接口保存到数据库
        userInfoService.register(registerVO);
        return R.success().message("注册成功");
    }

    @PostMapping("/login")
    @ApiOperation("会员登录")
    public R login(@RequestBody LoginVO loginVO, HttpServletRequest request) {
        String mobile = loginVO.getMobile();
        String password = loginVO.getPassword();

        Assert.notEmpty(mobile, ResponseEnum.MOBILE_NULL_ERROR);
        Assert.isTrue(RegexValidateUtils.checkCellphone(mobile),ResponseEnum.MOBILE_ERROR);
        Assert.notEmpty(password,ResponseEnum.PASSWORD_NULL_ERROR);

        String ip = request.getRemoteAddr();

        UserInfoVO userInfoVO = userInfoService.login(loginVO,ip);

        return R.success().data("userInfo",userInfoVO);
    }

    @ApiOperation("校验令牌")
    @GetMapping("/checkToken")
    public R checkToken(HttpServletRequest request) { // token是从请求头中取
        String token = request.getHeader("token");
        boolean tokenResult = JwtUtils.checkToken(token);

        if (tokenResult) {
            return R.success();
        }else {
            return R.setResult(ResponseEnum.LOGIN_AUTH_ERROR);
        }

    }

    @ApiOperation("校验手机号是否被注册")
    @GetMapping("/checkMobile/{mobile}")
    public boolean checkMobile(@PathVariable String mobile) {
        return userInfoService.checkMobile(mobile);
    }

}

