package com.billboarding.Controller.Payment;

import com.billboarding.Entity.User;
import com.billboarding.Services.Payment.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentInvoiceController {

    private final InvoiceService invoiceService;

    /**
     * 4️⃣ Download invoice PDF for a booking
     *    - Advertiser can download own invoice
     *    - Owner/Admin rules you can add later
     */
    @GetMapping(value = "/invoice/{bookingId}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> downloadInvoice(
            @PathVariable Long bookingId,
            Authentication auth
    ) {
        // You can add ownership checks if needed. For now, just generate if exists.
        User user = (User) auth.getPrincipal();

        byte[] pdfBytes = invoiceService.generateInvoice(bookingId);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=invoice_" + bookingId + ".pdf")
                .body(pdfBytes);
    }
}
