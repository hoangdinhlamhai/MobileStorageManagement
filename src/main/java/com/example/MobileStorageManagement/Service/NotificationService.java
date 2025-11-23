package com.example.MobileStorageManagement.Service;

import com.example.MobileStorageManagement.DTO.NotificationRequest;
import com.example.MobileStorageManagement.Entity.Notification;
import com.example.MobileStorageManagement.Entity.ReceiveNotifications;
import com.example.MobileStorageManagement.Entity.ReceiveNotificationsId;
import com.example.MobileStorageManagement.Entity.User;
import com.example.MobileStorageManagement.Repository.NotificationRepository;
import com.example.MobileStorageManagement.Repository.ReceiveNotificationsRepository;
import com.example.MobileStorageManagement.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReceiveNotificationsRepository receiveNotificationsRepository;

    public List<Notification> getAll(){
        return notificationRepository.findAll();
    }

    public Optional<Notification> getById(Integer id){
        return notificationRepository.findById(id);
    }

    @Transactional
    public Notification save(Notification notification, NotificationRequest req) {
        // 1. Lưu thông báo
        Notification savedThongBao = notificationRepository.save(notification);

        // 2. Lấy danh sách user cần gửi
        List<User> targetUsers = new ArrayList<>();

        if (Boolean.TRUE.equals(req.getSendToAll())) {
            targetUsers = userRepository.findAll();
        } else if (req.getRoles() != null && !req.getRoles().isEmpty()) {
            targetUsers = userRepository.findByRole_RoleNameIn(req.getRoles());
        } else if (req.getUserIds() != null && !req.getUserIds().isEmpty()) {
            targetUsers = userRepository.findAllById(req.getUserIds());
        }

        // 3. Tạo các bản ghi nhận thông báo
        List<ReceiveNotifications> list = new ArrayList<>();
        for (User tk : targetUsers) {
            ReceiveNotificationsId id = new ReceiveNotificationsId();
            id.setUserId(tk.getUserId());
            id.setNotificationId(savedThongBao.getNotificationId());

            ReceiveNotifications ntb = new ReceiveNotifications();
            ntb.setId(id);
            ntb.setUser(tk);
            ntb.setNotification(savedThongBao);
            ntb.setIsRead(false);
            list.add(ntb);
        }
        receiveNotificationsRepository.saveAll(list);

        return savedThongBao;
    }

    public void delete(Integer id){
        notificationRepository.deleteById(id);
    }
}
