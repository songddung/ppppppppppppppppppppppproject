package com.example.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChattController {
	
	@RequestMapping("/mychatt")
	public String chatt() {
		return "chatt";
	}

}
