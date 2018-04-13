package com.huatuo.user.service.impl;

import com.huatuo.user.dataobject.UserInfo;
import com.huatuo.user.repository.UserInfoRepository;
import com.huatuo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserInfoRepository userInfoRepository;

    /**
     * 通过openid查询
     *
     * @param openid
     * @return
     */
    @Override
    public UserInfo findByOpenid(String openid) {

        return userInfoRepository.findByOpenid(openid);
    }
}
