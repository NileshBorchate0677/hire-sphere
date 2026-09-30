package com.hiresphere.hiresphere.Notification.Service;

import java.util.List;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Notification.Dto.NotificationResponseDto;

public interface NotificationService {

    void createNotification(Users user, String title, String message, String type, Long relatedId);

    List<NotificationResponseDto> getMyNotifications();

    long getUnreadCount();

    void markAsRead(Long notificationId);

    void markAllAsRead();

    void deleteNotification(Long notificationId);

    void deleteAllNotifications();
}
