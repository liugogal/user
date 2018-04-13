package com.huatuo.user.service;

import com.huatuo.user.dataobject.UserInfo;

public interface UserService {

    /**
     * 通过openid查询
     * @param openid
     * @return
     */
    UserInfo findByOpenid(String openid);

}
