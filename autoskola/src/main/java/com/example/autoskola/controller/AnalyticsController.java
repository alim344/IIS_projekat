package com.example.autoskola.controller;

import com.example.autoskola.dto.AnalyticsDTO;
import com.example.autoskola.service.AnalyticsService;
import com.example.autoskola.service.PdfExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private PdfExportService pdfExportService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_PROFESSOR', 'ROLE_ADMIN', 'ROLE_INSTRUCTOR')")
    public ResponseEntity<AnalyticsDTO> getAnalytics(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null) {
            startDate = LocalDate.now().minusMonths(3);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        AnalyticsDTO analytics = analyticsService.getAnalytics(startDate, endDate);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/export-pdf")
    public ResponseEntity<byte[]> exportPdf(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        if (startDate == null) startDate = LocalDate.now().minusMonths(3);
        if (endDate == null) endDate = LocalDate.now();

        AnalyticsDTO analytics = analyticsService.getAnalytics(startDate, endDate);
        byte[] pdfBytes = pdfExportService.generateAnalyticsReport(analytics, startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(
                ContentDisposition.builder("attachment")
                        .filename("analytics-report-" + LocalDate.now() + ".pdf")
                        .build()
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
