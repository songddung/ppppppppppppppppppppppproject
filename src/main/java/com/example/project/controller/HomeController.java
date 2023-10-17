package com.example.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping({"/","/index"})
    public String home(){
        return "/index";
    }

        @GetMapping("random/randomtime")
    public String randomtime(){
      return "random/randomtime";
    }
}
