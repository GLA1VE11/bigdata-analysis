package com.example.ddemo.controller;

import com.example.ddemo.Service.MarsUserService;
import com.example.ddemo.Service.UserService;
import com.example.ddemo.common.Result;
import com.example.ddemo.entity.MarsUser;
import com.example.ddemo.entity.NewUser;
import com.example.ddemo.entity.Song;
import com.example.ddemo.mapper.MarsUserMapper;
import com.example.ddemo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MarsuserController {
    @Autowired
    private MarsUserMapper marsuserMapper;
    @Autowired
    private MarsUserService marsuserService;

    @PostMapping("/login")
    public Result login(@RequestBody MarsUser user) {
        user = marsuserService.login(user);
        return Result.success(user);
    }

    @GetMapping("/play_history")
    public Result play_history(@RequestAttribute("userId") String userId) {
        List<Song> songList = marsuserService.query_play_history(userId);
        return Result.success(songList);
    }

    @GetMapping("/collect_history")
    public Result collect_history(@RequestAttribute("userId") String userId) {
        List<Song> songList = marsuserService.query_collect_history(userId);
        return Result.success(songList);
    }
}
