package com.example.ddemo.controller;

import com.example.ddemo.Service.RankService;
import com.example.ddemo.common.Result;
import com.example.ddemo.entity.Artist;
import com.example.ddemo.entity.Song;
import com.example.ddemo.mapper.RankMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RankController {
    @Autowired
    private RankMapper rankMapper;
    @Autowired
    private RankService rankService;

    @GetMapping("/top_artist")
    public Result query_top_artist(@RequestParam(defaultValue = "10") Integer top) {
        List<Artist> artistList = rankService.topArtist(top);
        return Result.success(artistList);
    }
    @GetMapping("/top_song")
    public Result query_top_song(@RequestParam(defaultValue = "10") Integer top) {
        List<Song> songList = rankService.topSong(top);
        return Result.success(songList);
    }

    @GetMapping("/top_collect")
    public Result query_top_collect(@RequestParam(defaultValue = "10") Integer top) {
        List<Song> songList = rankService.topCollect(top);
        return Result.success(songList);
    }

    @GetMapping("/top_download")
    public Result query_top_download(@RequestParam(defaultValue = "10") Integer top) {
        List<Song> songList = rankService.topDownload(top);
        return Result.success(songList);
    }
}
