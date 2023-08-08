package com.team3.ecommerce.controller;

import com.team3.ecommerce.entity.Notification;
import com.team3.ecommerce.service.PushNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/push-notifications")
@CrossOrigin("*")
public class PushNotificationController {
    @Autowired
    private PushNotificationService pushNotificationService;

    @GetMapping("/{customerId}")
    public Flux<ServerSentEvent<List<Notification>>> streamLastMessage(@PathVariable Integer customerId) {
        return pushNotificationService.getNotificationsByUserToID(customerId);
    }
}
