package com.team3.ecommerce.repository;

import com.team3.ecommerce.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationStorageRepository extends JpaRepository<Notification, Integer> {

    Optional<Notification> findById(Integer id);

    List<Notification> findNotificationByCustomerToId(Integer id);

    List<Notification> findNotificationByCustomerToIdAndDeliveredFalse(Integer id);

}