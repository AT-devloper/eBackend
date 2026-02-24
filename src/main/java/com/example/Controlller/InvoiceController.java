package com.example.Controlller;

import com.example.Service.InvoiceService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("auth/orders")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping("/{orderId}/invoice")
    public void downloadInvoice(
            @PathVariable Long orderId,
            HttpServletResponse response
    ) throws IOException {

        byte[] pdf = invoiceService.createInvoice(orderId);

        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=invoice_" + orderId + ".pdf"
        );
        response.setContentLength(pdf.length);

        response.getOutputStream().write(pdf);
        response.getOutputStream().flush();
    }
}