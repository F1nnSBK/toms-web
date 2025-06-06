package com.toms.app.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class FileUploadControllerImpl implements FileUploadController {


    @Value("${upload.dir}")
    private String uploadDir;

    @GetMapping("/upload")
    @Override
    public String upload() {
        return "upload";
    }

    @PostMapping("/upload")
    @Override
    public String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {

        if(file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Bitte wählen Sie eine Datei zum Upload aus");
            redirectAttributes.addFlashAttribute("uploadStatus", "error");
            return "redirect:/admin/upload";
        }

        try{
            Path uploadPath = Paths.get(uploadDir);
            if(!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String orgFileName = file.getOriginalFilename();
            String fileExtension = "";
            int dotIndex = orgFileName.lastIndexOf(".");
            if(dotIndex > 0 && dotIndex < orgFileName.length() - 1) {
                fileExtension = orgFileName.substring(dotIndex + 1);
            }
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            Path filePath = uploadPath.resolve(uniqueFileName);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            redirectAttributes.addFlashAttribute("message",
                    "Datei '" + orgFileName + "' wurde erfolgreich hochgeladen als '" + uniqueFileName + "'!");
            redirectAttributes.addFlashAttribute("uploadStatus", "success");
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("message",
                    "Fehler beim Hochladen der Datei: " + e.getMessage());
            redirectAttributes.addFlashAttribute("uploadStatus", "error");
            e.printStackTrace();
        }
        return "redirect:/admin/upload";

    }

}
