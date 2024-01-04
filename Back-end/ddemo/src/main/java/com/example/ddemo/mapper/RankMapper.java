package com.example.ddemo.mapper;

import com.example.ddemo.entity.Artist;
import com.example.ddemo.entity.Song;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RankMapper {
    @Select("SELECT artist_id, SUM(CAST(song_init_plays AS UNSIGNED)) as total_plays " +
            "FROM mars_tianchi_songs " +
            "GROUP BY artist_id " +
            "ORDER BY total_plays DESC " +
            "LIMIT #{top}")
    List<Artist> findTopArtist(Integer top);

//    @Select("SELECT song_id, song_init_plays " +
//            "FROM mars_tianchi_songs " +
//            "ORDER BY song_init_plays DESC " +
//            "LIMIT #{top}")
    @Select("SELECT song_id, COUNT(*) AS song_init_plays " +
        "FROM mars_tianchi_user_action " +
        "WHERE action_type = 1 " +
        "GROUP BY song_id " +
        "ORDER BY song_init_plays DESC " +
        "LIMIT #{top}")
    List<Song> findTopSong(Integer top);

    // 查询下载次数最多的前50首歌曲
    @Select("SELECT song_id, COUNT(*) AS song_init_plays " +
            "FROM mars_tianchi_user_action " +
            "WHERE action_type = 2 " +
            "GROUP BY song_id " +
            "ORDER BY song_init_plays DESC " +
            "LIMIT #{top}")
    List<Song> findTopSongsByDownloads(Integer top);

    // 查询收藏次数最多的前50首歌曲
    @Select("SELECT song_id, COUNT(*) AS song_init_plays " +
            "FROM mars_tianchi_user_action " +
            "WHERE action_type = 3 " +
            "GROUP BY song_id " +
            "ORDER BY song_init_plays DESC " +
            "LIMIT #{top}")
    List<Song> findTopSongsByCollections(Integer top);
}
