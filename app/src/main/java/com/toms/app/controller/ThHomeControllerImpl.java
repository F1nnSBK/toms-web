package com.toms.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ThHomeControllerImpl {

    @GetMapping("/")
    public String home() {
        return "base";
    }

}
