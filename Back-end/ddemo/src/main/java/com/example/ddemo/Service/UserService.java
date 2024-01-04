package com.example.ddemo.Service;

import com.example.ddemo.entity.NewUser;
import com.example.ddemo.exception.ServiceException;
import com.example.ddemo.mapper.UserMapper;
import com.example.ddemo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    // 验证用户账户是否合法
    public NewUser login(NewUser user) {
        // 根据用户名查询数据库的用户信息
        NewUser dbUser = userMapper.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (dbUser == null) {
            // 抛出一个自定义的异常
            throw new ServiceException("用户名或密码错误");
        }
        if (!user.getPassword().equals(dbUser.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }
        String token = TokenUtils.genToken(String.valueOf(dbUser.getId()), dbUser.getPassword());
//        System.out.println(dbUser.getId());
        dbUser.setToken(token);
        return dbUser;
    }

}
