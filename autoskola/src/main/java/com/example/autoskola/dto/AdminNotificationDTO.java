package com.example.autoskola.dto;

import com.example.autoskola.model.ScheduledNotifType;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class AdminNotificationDTO {
    private Long id;
    private String text;
    private Long userId;
    private String userName;
    private String userLastName;
    private ScheduledNotifType type;
    private LocalDateTime createdAt;
}
