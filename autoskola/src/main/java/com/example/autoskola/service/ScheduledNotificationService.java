package com.example.autoskola.service;

import com.example.autoskola.dto.AdminNotificationDTO;
import com.example.autoskola.dto.PracticalDTO;
import com.example.autoskola.dto.ScheduledNotifDTO;
import com.example.autoskola.model.*;
import com.example.autoskola.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class ScheduledNotificationService {

    @Autowired
    private ScheduledNotificationRepository scheduledNotificationRepository;

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CandidateService candidateService;


    public void sendNotification(String text, User user, ScheduledNotifType type){

        ScheduledNotification notif = new ScheduledNotification();
        notif.setText(text);
        notif.setUser(user);
        notif.setType(type);
        notif.setCreatedAt(LocalDateTime.now());
        save(notif);

    }



    public List<ScheduledNotification> findByUser(User user){
        return scheduledNotificationRepository.findByUser(user);
    }


    public List<ScheduledNotifDTO> getCandidateClassNotif(Candidate candidate){
        List<ScheduledNotifDTO> dtos = new ArrayList<>();
        List<ScheduledNotification> notifications = scheduledNotificationRepository.findByUserOrderByCreatedAtDesc(candidate);
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

    @Scheduled(cron = "0 0 7 * * *")
    @Transactional
    public void checkInstructorLicenses() {
        List<Instructor> instructors = instructorRepository.findAll();

        List<User> admins = userRepository.findByRoleName("ROLE_ADMIN");

        for (Instructor instructor : instructors) {
            if (instructor.getDocuments() != null) {
                for (InstructorDocuments doc : instructor.getDocuments()) {
                    if (isExpiringSoon(doc.getExpiryDate(), 30)) {
                        createNotificationForAdmins(
                                admins,
                                String.format("Instructor %s %s - %s expires in %d days",
                                        instructor.getName(),
                                        instructor.getLastname(),
                                        formatDocumentType(doc.getDocumentType()),
                                        daysUntil(doc.getExpiryDate())),
                                ScheduledNotifType.UPDATE
                        );
                    }

                    if (isExpired(doc.getExpiryDate())) {
                        createNotificationForAdmins(
                                admins,
                                String.format("⚠️ EXPIRED: Instructor %s %s - %s expired on %s",
                                        instructor.getName(),
                                        instructor.getLastname(),
                                        formatDocumentType(doc.getDocumentType()),
                                        doc.getExpiryDate().toString()),
                                ScheduledNotifType.UPDATE
                        );
                    }
                }
            }
        }
    }

    @Scheduled(cron = "0 30 7 * * *")
    @Transactional
    public void checkVehicleRegistrations() {
        List<Vehicle> vehicles = vehicleRepository.findAll();
        List<User> admins = userRepository.findByRoleName("ROLE_ADMIN");

        for (Vehicle vehicle : vehicles) {
            if (isExpiringSoon(vehicle.getRegistrationExpiryDate(), 30)) {
                createNotificationForAdmins(
                        admins,
                        String.format("Vehicle %s - registration expires in %d days",
                                vehicle.getRegistrationNumber(),
                                daysUntil(vehicle.getRegistrationExpiryDate())),
                        ScheduledNotifType.UPDATE
                );
            }

            if (isExpired(vehicle.getRegistrationExpiryDate())) {
                createNotificationForAdmins(
                        admins,
                        String.format("⚠️ EXPIRED: Vehicle %s - registration expired on %s",
                                vehicle.getRegistrationNumber(),
                                vehicle.getRegistrationExpiryDate().toString()),
                        ScheduledNotifType.UPDATE
                );
            }
        }
    }

    private void createNotificationForAdmins(List<User> admins, String text, ScheduledNotifType type) {
        LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);

        for (User admin : admins) {
            boolean exists = scheduledNotificationRepository
                    .existsByTextAndTypeAndCreatedAtAfter(text, type, oneDayAgo);

            if (!exists) {
                ScheduledNotification notification = new ScheduledNotification();
                notification.setText(text);
                notification.setUser(admin);
                notification.setType(type);
                notification.setCreatedAt(LocalDateTime.now());

                scheduledNotificationRepository.save(notification);
            }
        }
    }

    public List<AdminNotificationDTO> getNotificationsForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return scheduledNotificationRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private boolean isExpiringSoon(LocalDate expiryDate, int days) {
        if (expiryDate == null) return false;
        LocalDate today = LocalDate.now();
        long daysUntil = ChronoUnit.DAYS.between(today, expiryDate);
        return daysUntil > 0 && daysUntil <= days;
    }

    private boolean isExpired(LocalDate expiryDate) {
        if (expiryDate == null) return false;
        return expiryDate.isBefore(LocalDate.now());
    }

    private long daysUntil(LocalDate expiryDate) {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    private String formatDocumentType(DocumentType type) {
        switch (type) {
            case DRIVING_LICENSE: return "Driving License";
            case INSTRUCTOR_LICENSE: return "Instructor License";
            case MEDICAL_CERTIFICATE: return "Medical Certificate";
            default: return "error";
        }
    }

    private AdminNotificationDTO mapToDTO(ScheduledNotification notification) {
        AdminNotificationDTO dto = new AdminNotificationDTO();
        dto.setId(notification.getId());
        dto.setText(notification.getText());
        dto.setUserId(notification.getUser().getId());
        dto.setUserName(notification.getUser().getName());
        dto.setUserLastName(notification.getUser().getLastname());
        dto.setType(notification.getType());
        dto.setCreatedAt(notification.getCreatedAt());
        return dto;
    }

    public void deleteById(Long id) {
        scheduledNotificationRepository.deleteById(id);
    }


    @Scheduled(cron = "0 0 20 * * WED", zone = "Europe/Belgrade")
    @Transactional
    public void sendTimePreferenceNotification(){
        List<Candidate> candidates = candidateService.getAllForPreferenceNotification();
        for(Candidate candidate : candidates){
            sendNotification("Update your time preference for next weeks classes!",candidate,ScheduledNotifType.TIME_PREFERENCE);
        }
    }

    public void sendTheoryExamNotification(List<Candidate> candidates, LocalDate date){
        for(Candidate candidate : candidates){
            sendNotification("Your theory exam has been scheduled for " + date.toString() ,candidate,ScheduledNotifType.THEORY_EXAM);
        }
    }


}
