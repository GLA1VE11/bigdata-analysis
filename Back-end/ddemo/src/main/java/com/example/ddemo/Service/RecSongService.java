package com.example.ddemo.Service;

import com.example.ddemo.entity.Recommend;
import com.example.ddemo.entity.Song;
import com.example.ddemo.mapper.RankMapper;
import com.example.ddemo.mapper.RecommendMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RecSongService {
    private static final int RECOMMENDED_SONGS_LIMIT = 30;

    @Autowired
    private RecommendMapper recommendMapper;

    @Autowired
    private RankMapper rankMapper;

    public List<String> getRecommendedSongIds(String userID) {
        int userId = Integer.parseInt(userID);
        // 获取用户推荐的Recommend对象
        Recommend recommendation = recommendMapper.findRecommendationByUserId(userId);
        if (recommendation == null) {
            return Collections.emptyList();
        }

        // 从Recommend对象中提取所有推荐歌曲ID
        List<String> userRecommendedSongIds = Stream.of(
                recommendation.getRecommendedSong1(), recommendation.getRecommendedSong2(), recommendation.getRecommendedSong3(),
                recommendation.getRecommendedSong4(), recommendation.getRecommendedSong5(), recommendation.getRecommendedSong6(),
                recommendation.getRecommendedSong7(), recommendation.getRecommendedSong8(), recommendation.getRecommendedSong9(),
                recommendation.getRecommendedSong10())
                .filter(Objects::nonNull)
                .map(Object::toString)
                .collect(Collectors.toList());

        System.out.println(userRecommendedSongIds);

        // 如果用户推荐的歌曲不足30首，补充Top 50歌曲
        if (userRecommendedSongIds.size() < RECOMMENDED_SONGS_LIMIT) {
            List<String> topSongIds = rankMapper.findTopSong(50).stream()
                    .map(Song::getSongId)
                    .filter(id -> !userRecommendedSongIds.contains(id))
                    .collect(Collectors.toList());

            for (String id : topSongIds) {
                if (userRecommendedSongIds.size() >= RECOMMENDED_SONGS_LIMIT) {
                    break;
                }
                userRecommendedSongIds.add(id);
            }
        }

        return userRecommendedSongIds;
    }
}
