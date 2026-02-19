package com.example.autoskola.service;

import com.example.autoskola.dto.DraftPracticalClassDTO;
import com.example.autoskola.dto.PracticalDTO;
import com.example.autoskola.dto.ScheduledNotifDTO;
import com.example.autoskola.model.Candidate;
import com.example.autoskola.model.PracticalClass;
import com.example.autoskola.model.ScheduledNotifType;
import com.example.autoskola.model.ScheduledNotification;
import com.example.autoskola.repository.ScheduledNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ScheduledNotificationService {

    @Autowired
    private ScheduledNotificationRepository scheduledNotificationRepository;



    public List<ScheduledNotification> findByCandidateId(long candidate_id){
        return scheduledNotificationRepository.findByCandidate_Id(candidate_id);
    }

    public List<ScheduledNotifDTO> getCandidateClassNotif(long candidate_id){
        List<ScheduledNotifDTO> dtos = new ArrayList<>();
        List<ScheduledNotification> notifications = findByCandidateId(candidate_id);
        for (ScheduledNotification notification : notifications) {
            ScheduledNotifDTO dto = new ScheduledNotifDTO();
            dto.setText(notification.getText());
            dto.setType(notification.getType().toString());
            dtos.add(dto);
        }
        return dtos;
    }

    public ScheduledNotification save(ScheduledNotification scheduledNotification){
        return scheduledNotificationRepository.save(scheduledNotification);
    }

    public void sendNotification(String text, Candidate candidate, ScheduledNotifType type){

        ScheduledNotification notif = new ScheduledNotification();
        notif.setText(text);
        notif.setCandidate(candidate);
        notif.setType(type);
        save(notif);

    }

    public void sendCreateNotification( LocalDateTime startTime, Candidate candidate){

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        String day = startTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        String from = startTime.format(timeFormatter);
        String text = "Class Update: New class has been created on " + day + " at " + from;
        sendNotification(text, candidate, ScheduledNotifType.CLASS);

    }

    public void sendUpdateNotification(PracticalClass pclass, PracticalDTO dto){

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String oldDay = pclass.getStartTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String newDay = dto.getStartTime().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        String oldTime = pclass.getStartTime().format(timeFormatter) + " - " + pclass.getEndTime().format(timeFormatter);
        String newTime = dto.getStartTime().format(timeFormatter) + " - " + dto.getEndTime().format(timeFormatter);

        String text;


        if (!oldDay.equals(newDay)) {
            text = String.format(
                    "Class Update: Your session has been moved from %s at %s to %s at %s.",
                    oldDay, oldTime, newDay, newTime
            );
        } else {
            text = String.format(
                    "Class Update: Your session on %s has been rescheduled from %s to %s.",
                    newDay, oldTime, newTime
            );
        }

        sendNotification(text, pclass.getCandidate(),ScheduledNotifType.UPDATE);

    }

    public void sendRequestAcceptedNotification(Candidate candidate, LocalDateTime startTime){
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        String dayDate = String.valueOf(startTime.getDayOfMonth());
        String day = startTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String time = startTime.format(timeFormatter);

        String text = "Request update: Your request has been accepted. Your class is scheduled for "+ dayDate +" "+day+ " at " + time;
        sendNotification(text, candidate,ScheduledNotifType.REQUEST);
    }

    public void sendRequestDeniedNotification(Candidate candidate){

        String text = "Request update: Instructor had no available spots for your requested class, see you next week!!";
        sendNotification(text, candidate,ScheduledNotifType.REQUEST);
    }

    public void sendDeletionNotification(LocalDateTime startTime, Candidate candidate){
        String dayDate = String.valueOf(startTime.getDayOfMonth());
        String day = startTime.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        String text = "Class Update: Class on " + day + " " + dayDate + ". has been deleted";
        sendNotification(text, candidate,ScheduledNotifType.CLASS);
    }


}
