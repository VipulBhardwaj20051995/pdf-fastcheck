package com.example.welcomepage.controller;

import com.example.welcomepage.dto.PdfMetadataDto;
import com.example.welcomepage.service.PdfMetadataService;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/api/pdf")
public class PdfMetadataController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PdfMetadataController.class);

    @Autowired
    private PdfMetadataService pdfMetadataService;

    @GetMapping("/upload")
    public String showUploadForm(Model model) {
        return "metadata";
    }

    @PostMapping("/data")
    public String extractPdfMetadata(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "password", required = false) String password,
            Model model) {
        try {
            PdfMetadataDto metadataDto;
            if (password != null && !password.isEmpty()) {
                metadataDto = pdfMetadataService.extractPdfMetadata(file, password);
            } else {
                metadataDto = pdfMetadataService.extractPdfMetadata(file);
            }

            model.addAttribute("metadata", metadataDto);
            return "metadata";
        } catch (InvalidPasswordException e) {
            model.addAttribute("error", "Please check password");
            return "metadata";
        } catch (IOException e) {
            LOGGER.error("Error extracting PDF metadata: {}", e.getMessage(), e);
            return "error";
        }
    }
}
