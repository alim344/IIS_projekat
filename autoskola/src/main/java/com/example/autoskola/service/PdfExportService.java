package com.example.autoskola.service;

import com.example.autoskola.dto.AnalyticsDTO;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Service
public class PdfExportService {
    public byte[] generateAnalyticsReport(AnalyticsDTO analytics, LocalDate startDate, LocalDate endDate) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Paragraph title = new Paragraph("Driving School Analytics Report")
                    .setFontSize(20)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(10);
            document.add(title);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Paragraph dateRange = new Paragraph(
                    "Period: " + startDate.format(formatter) + " - " + endDate.format(formatter))
                    .setFontSize(12)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(dateRange);

            document.add(new Paragraph("Summary Statistics").setFontSize(16).setBold().setMarginBottom(10));
            Table summaryTable = new Table(2);
            summaryTable.setWidth(UnitValue.createPercentValue(100));
            addTableRow(summaryTable, "Total Candidates", String.valueOf(analytics.getTotalCandidates()));
            addTableRow(summaryTable, "Total Theory Classes", String.valueOf(analytics.getTotalTheoryClasses()));
            addTableRow(summaryTable, "Total Practical Classes", String.valueOf(analytics.getTotalPracticalClasses()));
            addTableRow(summaryTable, "Candidates Ready for Exam", String.valueOf(analytics.getCandidatesReadyForExam()));
            addTableRow(summaryTable, "Avg Theory Occupancy", analytics.getAverageTheoryClassOccupancy() + "%");
            addTableRow(summaryTable, "Avg Lessons Completed", String.valueOf(analytics.getAverageLessonsCompleted()));
            document.add(summaryTable);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Candidate Time Preferences").setFontSize(14).setBold().setMarginBottom(10));
            Table prefTable = new Table(2);
            prefTable.setWidth(UnitValue.createPercentValue(100));
            addTableHeader(prefTable, "Time Slot", "Count");
            for (Map.Entry<String, Integer> entry : analytics.getCandidatePreferences().entrySet()) {
                addTableRow(prefTable, entry.getKey(), String.valueOf(entry.getValue()));
            }
            document.add(prefTable);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Theory Classes by Time Slot").setFontSize(14).setBold().setMarginBottom(10));
            Table theorySlotTable = new Table(2);
            theorySlotTable.setWidth(UnitValue.createPercentValue(100));
            addTableHeader(theorySlotTable, "Time Slot", "Number of Classes");
            for (Map.Entry<String, Integer> entry : analytics.getTheoryClassesBySlot().entrySet()) {
                addTableRow(theorySlotTable, entry.getKey(), String.valueOf(entry.getValue()));
            }
            document.add(theorySlotTable);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Theory Class Occupancy by Slot").setFontSize(14).setBold().setMarginBottom(10));
            Table occupancyTable = new Table(2);
            occupancyTable.setWidth(UnitValue.createPercentValue(100));
            addTableHeader(occupancyTable, "Time Slot", "Occupancy %");
            for (Map.Entry<String, Double> entry : analytics.getOccupancyBySlot().entrySet()) {
                addTableRow(occupancyTable, entry.getKey(), entry.getValue() + "%");
            }
            document.add(occupancyTable);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Practical Classes by Instructor").setFontSize(14).setBold().setMarginBottom(10));
            Table instructorTable = new Table(2);
            instructorTable.setWidth(UnitValue.createPercentValue(100));
            addTableHeader(instructorTable, "Instructor", "Total Classes");
            for (Map.Entry<String, Integer> entry : analytics.getPracticalClassesByInstructor().entrySet()) {
                addTableRow(instructorTable, entry.getKey(), String.valueOf(entry.getValue()));
            }
            document.add(instructorTable);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Completed Practical Classes by Instructor").setFontSize(14).setBold().setMarginBottom(10));
            Table completedTable = new Table(2);
            completedTable.setWidth(UnitValue.createPercentValue(100));
            addTableHeader(completedTable, "Instructor", "Completed Classes");
            for (Map.Entry<String, Integer> entry : analytics.getCompletedPracticalByInstructor().entrySet()) {
                addTableRow(completedTable, entry.getKey(), String.valueOf(entry.getValue()));
            }
            document.add(completedTable);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Candidates by Category").setFontSize(14).setBold().setMarginBottom(10));
            Table categoryTable = new Table(2);
            categoryTable.setWidth(UnitValue.createPercentValue(100));
            addTableHeader(categoryTable, "Category", "Count");
            for (Map.Entry<String, Integer> entry : analytics.getCandidatesByCategory().entrySet()) {
                addTableRow(categoryTable, entry.getKey(), String.valueOf(entry.getValue()));
            }
            document.add(categoryTable);
            document.add(new Paragraph("\n"));

            document.add(new Paragraph("Candidates by Status").setFontSize(14).setBold().setMarginBottom(10));
            Table statusTable = new Table(2);
            statusTable.setWidth(UnitValue.createPercentValue(100));
            addTableHeader(statusTable, "Status", "Count");
            for (Map.Entry<String, Integer> entry : analytics.getCandidatesByStatus().entrySet()) {
                addTableRow(statusTable, entry.getKey(), String.valueOf(entry.getValue()));
            }
            document.add(statusTable);

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF: " + e.getMessage());
        }
    }

    private void addTableHeader(Table table, String header1, String header2) {
        Cell cell1 = new Cell().add(new Paragraph(header1).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        Cell cell2 = new Cell().add(new Paragraph(header2).setBold())
                .setBackgroundColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER);
        table.addCell(cell1);
        table.addCell(cell2);
    }

    private void addTableRow(Table table, String col1, String col2) {
        table.addCell(new Cell().add(new Paragraph(col1)));
        table.addCell(new Cell().add(new Paragraph(col2)).setTextAlignment(TextAlignment.RIGHT));
    }
}
