package com.expoai.bucket.controller;

import com.expoai.bucket.annotation.LogExecutionTime;
import com.expoai.bucket.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TaskController {

    private final NotificationService notificationService;

    @PostMapping
    @LogExecutionTime
    public ResponseEntity<Void> addTask(@RequestBody Map<String, String> payload) {
        String description = payload.get("description");

        if (description == null || description.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        notificationService.sendNotification("Nouvelle tâche ajoutée: " + description);
        return ResponseEntity.ok().build();
    }
}
