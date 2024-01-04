package com.example.ddemo.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.ddemo.entity.MarsUser;
import com.example.ddemo.entity.NewUser;
import com.example.ddemo.entity.Song;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MarsUserMapper extends BaseMapper<MarsUser> {
    @Select("SELECT * FROM mars_tianchi_user WHERE username = #{username} AND password = #{password}")
    MarsUser findUserByUsernameAndPassword(String username, String password);

    @Select("SELECT *" +
            "FROM mars_tianchi_songs s " +
            "JOIN mars_tianchi_user_action ua ON s.song_id = ua.song_id " +
            "WHERE ua.user_id = #{userId} AND ua.action_type = 1")
    List<Song> getSongsPlayedByUser(String userId);

    @Select("SELECT *" +
            "FROM mars_tianchi_songs s " +
            "JOIN mars_tianchi_user_action ua ON s.song_id = ua.song_id " +
            "WHERE ua.user_id = #{userId} AND ua.action_type = 2")
    List<Song> getSongsCollectedByUser(String userId);
}
