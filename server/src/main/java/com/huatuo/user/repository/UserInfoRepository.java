package com.huatuo.user.repository;


import com.huatuo.user.dataobject.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo,String>{

    /**
     * 通过openid查询
     * @param openid
     * @return
     */
    UserInfo findByOpenid(String openid);

}
