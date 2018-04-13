package com.huatuo.user.controller;

import com.huatuo.user.VO.ResultVO;
import com.huatuo.user.constant.CookieConstant;
import com.huatuo.user.constant.TokenConstant;
import com.huatuo.user.dataobject.UserInfo;
import com.huatuo.user.enums.ResultEnum;
import com.huatuo.user.enums.RoleEnum;
import com.huatuo.user.service.UserService;
import com.huatuo.user.utils.CookieUtil;
import com.huatuo.user.utils.ResultVOUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/login")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping(value = "/buyer")
    public ResultVO buyerLogin(@RequestParam String openid, HttpServletResponse response) {
        //1、从数据库中查找openid
        UserInfo userInfo = userService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVOUtil.error(ResultEnum.LOGIN_FAIL);
        }

        //2、判断角色
        if (RoleEnum.BUYER.getCode() != userInfo.getRole()) {
            return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
        }
        //3、设置cookie  openid=abc
        CookieUtil.set(response, CookieConstant.OPENID, userInfo.getOpenid(), CookieConstant.expire);

        return ResultVOUtil.success();

    }

    @GetMapping(value = "/seller")
    public ResultVO sellerLogin(@RequestParam String openid, HttpServletResponse response, HttpServletRequest request) {


        //判断是否登录,如果有token的cookie 并且token在redis上能够找到，则直接返回登录成功
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if((cookie != null) &&
                !StringUtils.isEmpty(stringRedisTemplate.opsForValue().get(String.format(TokenConstant.TOKEN_TEMPLATE, cookie.getValue())))){
            return ResultVOUtil.success();
        }


        //1、从数据库中查找openid
        UserInfo userInfo = userService.findByOpenid(openid);
        if (userInfo == null) {
            return ResultVOUtil.error(ResultEnum.LOGIN_FAIL);
        }

        //2、判断角色
        if (RoleEnum.SELLER.getCode() != userInfo.getRole()) {
            return ResultVOUtil.error(ResultEnum.ROLE_ERROR);
        }

        //3、写入redis设置 key=UUID(token_UUID)  value=openid
        String token = UUID.randomUUID().toString();
        Integer expire = CookieConstant.expire;
        stringRedisTemplate.opsForValue().set(String.format(TokenConstant.TOKEN_TEMPLATE, token),
                openid,
                expire,
                TimeUnit.SECONDS
        );


        //4、设置cookie  token=UUID
        CookieUtil.set(response, CookieConstant.TOKEN, token, CookieConstant.expire);

        return ResultVOUtil.success();
    }


}
