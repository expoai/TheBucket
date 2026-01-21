package com.expoai.bucket.service;


import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class NotificationService {
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    
    public SseEmitter createEmitter() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));
        
        emitters.add(emitter);
        return emitter;
    }
    
    public void sendNotification(String message) {
        List<SseEmitter> deadEmitters = new ArrayList<>();
        
        emitters.forEach(emitter -> {
            try {
                emitter.send(SseEmitter.event()
                    .name("task-added")
                    .data(message));
            } catch (Exception e) {
                deadEmitters.add(emitter);
            }
        });
        
        emitters.removeAll(deadEmitters);
    }
}