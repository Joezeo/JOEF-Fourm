package com.joezeo.community.service.impl;

import com.joezeo.community.exception.CustomizeErrorCode;
import com.joezeo.community.exception.CustomizeException;
import com.joezeo.community.exception.ServiceException;
import com.joezeo.community.mapper.UserMapper;
import com.joezeo.community.pojo.User;
import com.joezeo.community.pojo.UserExample;
import com.joezeo.community.provider.UCloudProvider;
import com.joezeo.community.service.UserService;
import com.joezeo.community.utils.AvatarGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UCloudProvider uCloudProvider;

    @Autowired
    private UserMapper userMapper;


    @Override
    public void createOrUpadate(User user) {
        if(user == null){
            log.error("函数createOrUpdate()：参数user异常，=null");
            throw new ServiceException("参数异常");
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> memUser = userMapper.selectByExample(userExample);

        if(memUser == null || memUser.size() == 0){ // 执行插入操作
            // 随机生成头像
            InputStream avatar = new AvatarGenerator().getARandomAvatar();
            String avatarUrl = uCloudProvider.uploadAvatar(avatar, "image/jpeg", "avatar-" + user.getAccountId() + ".jpg");
            user.setAvatarUrl(avatarUrl);

            int count = userMapper.insert(user);
            if (count != 1) {
                log.error("函数createOrUpdate：保存新用户失败");
                throw new CustomizeException(CustomizeErrorCode.CREATE_NEW_USER_FAILD);
            }
        } else { // 执行更新操作
            user.setId(memUser.get(0).getId());
            int count = userMapper.updateByPrimaryKeySelective(user);
            if (count != 1) {
                log.error("函数createOrUpdate：更新用户信息失败");
                throw new CustomizeException(CustomizeErrorCode.UPDATE_USER_FAILD);
            }
        }
    }

    @Override
    public User queryUserByToken(String token) {
        // 前方已经保证token！=null 或空，不必再做判断
//        if (token == null || "".equals(token)) {
//            throw new ServiceException("参数token为空");
//        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andTokenEqualTo(token);
        List<User> user = userMapper.selectByExample(userExample);
        if (user == null || user.size() == 0) {
            return null;
        }
        return user.get(0);
    }

    @Override
    public User queryByAccountid(String accountId) {
        if (accountId == null || "".equals(accountId)) {
            log.error("函数queryByAccoundid：参数accountId为空");
            throw new ServiceException("参数异常");
        }

        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(accountId);
        List<User> user = userMapper.selectByExample(userExample);
        if (user == null) {
            log.error("函数queryByAccountid：获取user信息失败，该accounid不存在，可能前端传参有问题");
            throw new ServiceException("获取user失败");
        }
        return user.get(0);
    }
}
