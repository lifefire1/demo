package com.example.demo.controller;
import com.example.demo.service.RabbitMqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class IndexController {

    private final RabbitMqService rabbitMqService;

    @Autowired
    public IndexController(RabbitMqService rabbitMqService) {
        this.rabbitMqService = rabbitMqService;
    }

    @GetMapping("/chat")
    public String showIndexPage() {
        return "index";
    }

    @PostMapping("/uploadAudio")
    public String uploadAudio(@RequestParam("audioFile") MultipartFile audioFile) {
        if (!audioFile.isEmpty()) {
            try {
                // Сохраняем аудиофайл на сервере
                String uploadDir = "uploads/"; // Замените путь на реальный
                byte[] audioData = audioFile.getBytes();
                Path path = Paths.get(uploadDir + audioFile.getOriginalFilename());
                Files.write(path, audioData);

                // Отправляем аудиофайл в RabbitMQ
                rabbitMqService.sendAudioToQueue(audioData);

            } catch (IOException e) {
                e.printStackTrace();
                // Обработка ошибок сохранения файла
            }
        } else {
            System.out.println("Файл не найден или пустой.");
        }

        // Редирект обратно на страницу чата или куда-либо еще
        return "redirect:/chat";
    }
}
