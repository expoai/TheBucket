package com.expoai.bucket.controller;

import com.expoai.bucket.annotation.LogExecutionTime;
import com.expoai.bucket.service.NotificationService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @LogExecutionTime
    public SseEmitter subscribe(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        return notificationService.createEmitter();
    }

    @PostMapping("/send")
    @LogExecutionTime
    public ResponseEntity<Void> sendNotification(@RequestParam String message) {
        notificationService.sendNotification(message);
        return ResponseEntity.ok().build();
    }
}
