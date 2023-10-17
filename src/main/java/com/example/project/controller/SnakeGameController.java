package com.example.project.controller;
// 1012 1626 이기주 : snakegame관련 controller

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SnakeGameController {
    
    @GetMapping("/snakegame")
    public String snakegame(){
        return "game/snakegame";
    }
}
