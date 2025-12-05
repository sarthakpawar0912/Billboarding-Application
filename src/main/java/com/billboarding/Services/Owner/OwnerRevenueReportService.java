package com.billboarding.Services.Owner;

import com.billboarding.DTO.OWNER.BillboardRevenueDTO;
import com.billboarding.DTO.OWNER.OwnerRevenueDashboardResponse;
import com.billboarding.Entity.User;

import com.lowagie.text.*;                 // OpenPDF
import com.lowagie.text.pdf.*;            // OpenPDF

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class OwnerRevenueReportService {

    private final OwnerRevenueService revenueService;

    public byte[] exportRevenueCsv(User owner, Long billboardId, LocalDate start, LocalDate end) {

        OwnerRevenueDashboardResponse data =
                revenueService.getRevenueDashboard(owner, billboardId, start, end);

        StringBuilder sb = new StringBuilder();
        sb.append("Billboard ID,Title,Location,Bookings,Revenue\n");

        for (BillboardRevenueDTO b : data.getBillboards()) {
            sb.append(b.getBillboardId()).append(",")
                    .append(b.getTitle()).append(",")
                    .append(b.getLocation()).append(",")
                    .append(b.getTotalBookings()).append(",")
                    .append(b.getTotalRevenue()).append("\n");
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] exportRevenuePdf(User owner, Long billboardId, LocalDate start, LocalDate end) {

        OwnerRevenueDashboardResponse data =
                revenueService.getRevenueDashboard(owner, billboardId, start, end);

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // Correct OpenPDF fonts
            Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
            Font normalFont = new Font(Font.HELVETICA, 12);

            Paragraph title = new Paragraph("Owner Revenue Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total Earnings: ₹" + data.getTotalEarnings(), normalFont));
            document.add(new Paragraph("Total Billboards: " + data.getTotalBillboards(), normalFont));
            document.add(new Paragraph("Total Bookings: " + data.getTotalBookings(), normalFont));
            document.add(new Paragraph("Pending Approvals: " + data.getPendingRequests(), normalFont));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Billboard Revenue Breakdown", titleFont));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);

            addTableHeader(table, "ID");
            addTableHeader(table, "Title");
            addTableHeader(table, "Location");
            addTableHeader(table, "Bookings");
            addTableHeader(table, "Revenue");

            for (BillboardRevenueDTO b : data.getBillboards()) {
                table.addCell(b.getBillboardId().toString());
                table.addCell(b.getTitle());
                table.addCell(b.getLocation());
                table.addCell(String.valueOf(b.getTotalBookings()));
                table.addCell("₹" + b.getTotalRevenue());
            }

            document.add(table);
            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PDF creation failed: " + e.getMessage());
        }
    }

    private void addTableHeader(PdfPTable table, String title) {
        PdfPCell header = new PdfPCell(new Phrase(title));
        header.setBackgroundColor(Color.LIGHT_GRAY);
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header);
    }
}
