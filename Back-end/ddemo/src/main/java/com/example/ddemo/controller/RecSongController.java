package com.example.ddemo.controller;

import com.example.ddemo.Service.RecSongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class RecSongController {

    @Autowired
    private RecSongService recSongServicesongService;

    @GetMapping("/recommended")
    public List<String> getRecommendedSongIds(@RequestAttribute("userId") String userId) {
        return recSongServicesongService.getRecommendedSongIds(userId);
    }
}
