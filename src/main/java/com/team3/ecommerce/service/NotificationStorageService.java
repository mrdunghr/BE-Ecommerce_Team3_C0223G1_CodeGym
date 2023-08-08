package com.team3.ecommerce.service;

import com.team3.ecommerce.entity.Notification;
import com.team3.ecommerce.repository.NotificationStorageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationStorageService {
    @Autowired
    private NotificationStorageRepository notifRepository;

    public Notification createNotificationStorage(Notification notificationStorage) {
        return notifRepository.save(notificationStorage);
    }

    public Notification getNotificationsByID(Integer id) {
        return notifRepository.findById(id).orElseThrow(() -> new RuntimeException("Thông báo không tìm thấy: " + id));
    }

    public List<Notification> getNotificationsByUserIDNotRead(Integer customerID) {
        return notifRepository.findNotificationByCustomerToIdAndDeliveredFalse(customerID);
    }

    public List<Notification> getNotificationsByUserID(Integer customerID) {
        return notifRepository.findNotificationByCustomerToId(customerID);
    }

    public Notification changeNotifStatusToRead(Integer notifID) {
        Notification notification = notifRepository.findById(notifID)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy: " + notifID));
        notification.setReader(true);
        return notifRepository.save(notification);
    }

    public void clear() {
        notifRepository.deleteAll();
    }
}
