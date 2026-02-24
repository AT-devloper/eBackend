package com.example.Service;

import com.example.Dto.InvoiceItem;
import com.example.Model.Order;
import com.example.Model.OrderItem;
import com.example.Repository.InvoiceRepository;
import com.example.Repository.OrderItemRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final OrderItemRepository orderItemRepository;

    public byte[] createInvoice(Long orderId) {
        // --- 1. FETCH ORDER ---
        Order order = invoiceRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // --- 2. PREPARE ITEMS LIST (Your Original Logic) ---
        List<InvoiceItem> items = new ArrayList<>();

        // 2a. Try itemsJson first
        if (order.getItemsJson() != null && !order.getItemsJson().isEmpty()) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                items = mapper.readValue(order.getItemsJson(), new TypeReference<List<InvoiceItem>>() {});
            } catch (Exception e) {
                // Log error but continue to fallback
                System.err.println("JSON Parse Error: " + e.getMessage());
            }
        }

        // 2b. Fallback to DB
        if (items.isEmpty()) {
            List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
            for (OrderItem oi : orderItems) {
                InvoiceItem invItem = new InvoiceItem();
                invItem.setProductId(oi.getProductId());
                invItem.setProductName(oi.getProductName() != null ? oi.getProductName() : "Product-" + oi.getProductId());
                invItem.setQuantity(oi.getQuantity() != null ? oi.getQuantity() : 1);
                invItem.setPrice(oi.getPrice() != null ? oi.getPrice() : 0.0);
                items.add(invItem);
            }
        }

        // --- 3. GENERATE PROFESSIONAL PDF (New Logic) ---
        try {
            // A. Load Templates
            String htmlTemplate = loadResourceFile("templates/invoice_template.html");
            String cssContent = loadResourceFile("templates/invoice_style.css");

            // B. Build Items HTML Rows
            StringBuilder itemsRowsHtml = new StringBuilder();
            double totalAmount = 0.0;

            if (items.isEmpty()) {
                itemsRowsHtml.append("<tr><td colspan='4' class='text-center'>No items found.</td></tr>");
            } else {
                for (InvoiceItem item : items) {
                    double lineTotal = item.getPrice() * item.getQuantity();
                    totalAmount += lineTotal;

                    // Building HTML rows
                    itemsRowsHtml.append(String.format(
                    	    "<tr>" +
                    	        "<td>%s</td>" +
                    	        "<td align='center'>%d</td>" +  
                    	        "<td align='right'>&#8377; %.2f</td>" + 
                    	        "<td align='right'>&#8377; %.2f</td>" + 
                    	    "</tr>",
                    	    escapeHtml(item.getProductName()), 
                    	    item.getQuantity(), 
                    	    item.getPrice(), 
                    	    lineTotal
                    	));
                }
            }

            // C. Inject Data & CSS into HTML
            String finalHtml = htmlTemplate
                    .replace("[[CSS_STYLES]]", cssContent) // Injects CSS
                    .replace("[[ORDER_NO]]", escapeHtml(order.getOrderNumber()))
                    .replace("[[ORDER_DATE]]", formatOrderDate(order))
                    .replace("[[ORDER_STATUS]]", escapeHtml(order.getOrderStatus()))
                    .replace("[[ITEMS_ROWS]]", itemsRowsHtml.toString())
                    .replace("[[TOTAL_AMOUNT]]", String.format("%.2f", totalAmount));

            // D. Render PDF
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                ITextRenderer renderer = new ITextRenderer();
                renderer.setDocumentFromString(finalHtml);
                renderer.layout();
                renderer.createPDF(outputStream);
                return outputStream.toByteArray();
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF invoice", e);
        }
    }

    // --- HELPER METHODS ---

    private String loadResourceFile(String filename) {
        try (InputStream inputStream = new ClassPathResource(filename).getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Could not read template file: " + filename, e);
        }
    }

    private String formatOrderDate(Order order) {
        if (order.getCreatedAt() == null) return "N/A";
        return order.getCreatedAt().format(DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm"));
    }

    // Prevents HTML breakage if product name contains special chars like "&"
    private String escapeHtml(String text) {
        if (text == null) return "";
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;");
    }
}