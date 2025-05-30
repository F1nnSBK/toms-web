package com.toms.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

public interface ThController {
    @GetMapping("/")
    ModelAndView home();
}
