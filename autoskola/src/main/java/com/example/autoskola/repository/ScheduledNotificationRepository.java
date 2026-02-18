package com.example.autoskola.repository;

import com.example.autoskola.model.ScheduledNotification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduledNotificationRepository extends JpaRepository<ScheduledNotification, Long> {

    List<ScheduledNotification> findByCandidate_Id(long candidateId);

    ScheduledNotification findById(long id);

    ScheduledNotification save(ScheduledNotification scheduledNotification);
}
