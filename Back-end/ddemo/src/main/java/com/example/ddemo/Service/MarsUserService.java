package com.example.ddemo.Service;

import com.example.ddemo.entity.MarsUser;
import com.example.ddemo.entity.NewUser;
import com.example.ddemo.entity.Song;
import com.example.ddemo.exception.ServiceException;
import com.example.ddemo.mapper.MarsUserMapper;
import com.example.ddemo.mapper.UserMapper;
import com.example.ddemo.utils.TokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarsUserService {
    @Autowired
    MarsUserMapper marsuserMapper;
    // 验证用户账户是否合法
    public MarsUser login(MarsUser user) {
        // 根据用户名查询数据库的用户信息
        MarsUser dbUser = marsuserMapper.findUserByUsernameAndPassword(user.getUsername(), user.getPassword());
        if (dbUser == null) {
            // 抛出一个自定义的异常
            throw new ServiceException("用户名或密码错误");
        }
        if (!user.getPassword().equals(dbUser.getPassword())) {
            throw new ServiceException("用户名或密码错误");
        }
        String token = TokenUtils.genToken(String.valueOf(dbUser.getUserId()), dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    public List<Song> query_play_history(String userId){
        return marsuserMapper.getSongsPlayedByUser(userId);
    }

    public List<Song> query_collect_history(String userId) {
        return marsuserMapper.getSongsCollectedByUser(userId);
    }
}
