package com.example.autoskola.service;

import com.example.autoskola.dto.FuelRecordDTO;
import com.example.autoskola.dto.VehicleStatsDTO;
import com.example.autoskola.model.Vehicle;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class PdfReportService {

    public byte[] generateVehicleReport(VehicleStatsDTO stats, Vehicle vehicle) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        document.add(new Paragraph("Vehicle Report")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(20)
                .setBold());

        document.add(new Paragraph("Vehicle: " + vehicle.getRegistrationNumber())
                .setFontSize(14));
        document.add(new Paragraph("Period: " + stats.getMonth().toString())
                .setFontSize(12));

        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Basic Statistics:").setBold().setFontSize(14));
        document.add(new Paragraph("Total distance: " + stats.getDistanceTraveled() + " km"));
        document.add(new Paragraph("Total fuel: " + String.format("%.2f", stats.getTotalLiters()) + " L"));
        document.add(new Paragraph("Total cost: " + String.format("%.2f", stats.getTotalCost()) + " RSD"));
        document.add(new Paragraph("Average consumption: " + String.format("%.2f", stats.getAvgConsumption()) + " L/100km"));
        document.add(new Paragraph("Average cost per liter: " + String.format("%.2f", stats.getAvgCostPerLiter()) + " RSD/L"));

        document.add(new Paragraph("\n"));

        document.add(new Paragraph("Instructor Statistics:").setBold().setFontSize(14));

        if (stats.getMostFrequentInstructorName() != null) {
            document.add(new Paragraph("Most frequent instructor: " + stats.getMostFrequentInstructorName() +
                    " (" + stats.getMostFrequentInstructorCount() + " refuels)"));
        }

        document.add(new Paragraph("\n"));

        if (stats.getPracticalClassesCount() != null && stats.getPracticalClassesCount() > 0) {
            document.add(new Paragraph("Practical Classes:").setBold().setFontSize(14));
            document.add(new Paragraph("Number of classes: " + stats.getPracticalClassesCount()));
            document.add(new Paragraph("Fuel per class: " + String.format("%.2f", stats.getFuelPerClass()) + " L"));
            document.add(new Paragraph("Cost per class: " + String.format("%.2f", stats.getCostPerClass()) + " RSD"));
            document.add(new Paragraph("\n"));
        }

        document.add(new Paragraph("Refuel History:").setBold().setFontSize(14));

        Table table = new Table(new float[]{2, 2, 2, 2});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addCell("Date");
        table.addCell("Liters");
        table.addCell("Cost");
        table.addCell("Mileage");

        if (stats.getFuelRecords() != null && !stats.getFuelRecords().isEmpty()) {
            for (FuelRecordDTO record : stats.getFuelRecords()) {
                table.addCell(record.getRefuelDate().toString());
                table.addCell(String.format("%.2f", record.getLiters()));
                table.addCell(String.format("%.2f", record.getTotalCost()));
                table.addCell(record.getMileageAtRefuel().toString());
            }
        } else {
            table.addCell("No data");
            table.addCell("No data");
            table.addCell("No data");
            table.addCell("No data");
        }

        document.add(table);
        document.close();

        return baos.toByteArray();
    }
}