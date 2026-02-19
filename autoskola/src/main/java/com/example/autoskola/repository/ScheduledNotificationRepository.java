package com.example.autoskola.repository;

import com.example.autoskola.model.ScheduledNotification;
import com.example.autoskola.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduledNotificationRepository extends JpaRepository<ScheduledNotification, Long> {

    List<ScheduledNotification> findByUser(User user);

    ScheduledNotification findById(long id);

    ScheduledNotification save(ScheduledNotification scheduledNotification);
}
