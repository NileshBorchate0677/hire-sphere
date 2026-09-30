package com.hiresphere.hiresphere.Auth.Dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSessionResponseDto {
    private Long id;
    private LocalDateTime createdAt;
    private boolean isCurrent;
}
