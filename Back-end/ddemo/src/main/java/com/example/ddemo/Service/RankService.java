package com.example.ddemo.Service;

import com.example.ddemo.entity.Artist;
import com.example.ddemo.entity.Song;
import com.example.ddemo.mapper.RankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RankService {
    @Autowired
    RankMapper rankMapper;

    public List<Artist> topArtist(Integer top){
        return rankMapper.findTopArtist(top);
    }

    public List<Song> topSong(Integer top) {
        return rankMapper.findTopSong(top);
    }

    public List<Song> topDownload(Integer top){
        return rankMapper.findTopSongsByDownloads(top);
    }

    public List<Song> topCollect(Integer top){
        return rankMapper.findTopSongsByCollections(top);
    }
}
