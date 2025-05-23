package com.toms.app.controller;

import org.springframework.web.bind.annotation.GetMapping;

public interface ThHomeController {
    @GetMapping("/")
    String home();
}
