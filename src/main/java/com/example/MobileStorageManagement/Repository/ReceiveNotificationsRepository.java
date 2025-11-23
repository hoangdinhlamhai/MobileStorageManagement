package com.example.MobileStorageManagement.Repository;

import com.example.MobileStorageManagement.Entity.Notification;
import com.example.MobileStorageManagement.Entity.ReceiveNotifications;
import com.example.MobileStorageManagement.Entity.ReceiveNotificationsId;
import com.example.MobileStorageManagement.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiveNotificationsRepository extends JpaRepository<ReceiveNotifications, ReceiveNotificationsId> {
    List<ReceiveNotifications> findByUser(User user);
    List<ReceiveNotifications> findByNotification(Notification notification);
}
