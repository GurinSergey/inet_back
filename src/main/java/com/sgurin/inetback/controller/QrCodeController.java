package com.sgurin.inetback.controller;

import com.google.zxing.WriterException;
import com.sgurin.inetback.service.QrCodeGeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/qrcode")
public class QrCodeController {
    private final QrCodeGeneratorService qrCodeGeneratorService;

    @Autowired
    public QrCodeController(QrCodeGeneratorService qrCodeGeneratorService) {
        this.qrCodeGeneratorService = qrCodeGeneratorService;
    }

    @GetMapping(value = "/generate/{text}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] generateQRCode(@PathVariable String text) throws WriterException, IOException {
        int width = 300;
        int height = 300;

        return qrCodeGeneratorService.generateQRCodeImage(text, width, height);
    }
}
