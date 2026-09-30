package com.hiresphere.hiresphere.Notification.Service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hiresphere.hiresphere.Auth.Entity.Users;
import com.hiresphere.hiresphere.Auth.Repository.UserRepository;
import com.hiresphere.hiresphere.Notification.Dto.NotificationResponseDto;
import com.hiresphere.hiresphere.Notification.Entity.Notification;
import com.hiresphere.hiresphere.Notification.Repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    private Users getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void createNotification(Users user, String title, String message, String type, Long relatedId) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRelatedId(relatedId);
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponseDto> getMyNotifications() {
        Users user = getLoggedInUser();
        return notificationRepository.findByUserOrderByCreatedAtDesc(user)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount() {
        Users user = getLoggedInUser();
        return notificationRepository.countByUserAndIsReadFalse(user);
    }

    @Override
    public void markAsRead(Long notificationId) {
        Users user = getLoggedInUser();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized to update this notification");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    @Override
    public void markAllAsRead() {
        Users user = getLoggedInUser();
        notificationRepository.markAllAsReadForUser(user);
    }

    @Override
    public void deleteNotification(Long notificationId) {
        Users user = getLoggedInUser();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized to delete this notification");
        }

        notificationRepository.delete(notification);
    }

    @Override
    public void deleteAllNotifications() {
        Users user = getLoggedInUser();
        notificationRepository.deleteByUser(user);
    }

    private NotificationResponseDto mapToDto(Notification n) {
        return NotificationResponseDto.builder()
                .id(n.getId())
                .title(n.getTitle())
                .message(n.getMessage())
                .type(n.getType())
                .relatedId(n.getRelatedId())
                .isRead(n.getIsRead())
                .createdAt(n.getCreatedAt())
                .build();
    }
}
