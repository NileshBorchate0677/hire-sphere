package com.hiresphere.hiresphere.Notification.Dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String title;
    private String message;
    private String type;
    private Long relatedId;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
