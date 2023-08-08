package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Notification;
import com.team3.ecommerce.repository.NotificationStorageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class PushNotificationService {
    @Autowired
    private NotificationStorageRepository notificationStorageRepository;

    private List<Notification> getNotifs(Integer customerId) {
        List<Notification> notifs = notificationStorageRepository.findNotificationByCustomerToIdAndDeliveredFalse(customerId);
        for (Notification notification : notifs) {
            notification.setDelivered(true);
        }
        notificationStorageRepository.saveAll(notifs);
        return notifs;
    }

    public Flux<ServerSentEvent<List<Notification>>> getNotificationsByUserToID(Integer customerID) {

        if (customerID != null && customerID > 0) {
            return Flux.interval(Duration.ofSeconds(1))
                    .publishOn(Schedulers.boundedElastic())
                    .map(sequence -> ServerSentEvent.<List<Notification>>builder().id(String.valueOf(sequence))
                            .event("user-list-event").data(getNotifs(customerID))
                            .build());
        }

        return Flux.interval(Duration.ofSeconds(1)).map(sequence -> ServerSentEvent.<List<Notification>>builder()
                .id(String.valueOf(sequence)).event("user-list-event").data(new ArrayList<>()).build());
    }
    public List<Notification> getCustomerNotify(Integer id){
        return notificationStorageRepository.findNotificationByCustomerToId(id);
    }
}
