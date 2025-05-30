package com.toms.app.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

public interface FileUploadController {
    @GetMapping("/upload")
    String upload();

    @PostMapping("/upload")
    String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes);
}
