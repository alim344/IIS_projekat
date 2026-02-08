package com.example.autoskola.controller;

import com.example.autoskola.dto.TimePrefInstructorDTO;
import com.example.autoskola.model.TimePreference;
import com.example.autoskola.service.InstructorService;
import com.example.autoskola.service.TimePreferenceService;
import com.example.autoskola.util.TokenUtils;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/time_pref")
public class TimePreferenceController {

    @Autowired
    private TimePreferenceService timePreferenceService;
    @Autowired
    private TokenUtils tokenUtils;
    @Autowired
    private InstructorService instructorService;

    @GetMapping("/get_by_inst_id")
    public ResponseEntity<List<TimePrefInstructorDTO>> getTimePrefByInstructorId(HttpServletRequest request){

       String token = tokenUtils.getToken(request);
       String email = tokenUtils.getEmailFromToken(token);
       long instructorId = instructorService.getIdByEmail(email);
       return ResponseEntity.ok(timePreferenceService.getCandidateTimePreferencesDTO(instructorId));
   }
}
