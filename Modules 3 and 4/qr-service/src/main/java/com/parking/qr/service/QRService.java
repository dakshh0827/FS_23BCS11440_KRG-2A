package com.parking.qr.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.parking.qr.model.QRCode;
import com.parking.qr.repository.QRRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class QRService {
    
    @Autowired
    private QRRepository qrRepository;
    
    public Map<String, Object> generateQRCode(Long bookingId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            String code = UUID.randomUUID().toString();
            
            QRCode qrCode = new QRCode();
            qrCode.setBookingId(bookingId);
            qrCode.setCode(code);
            qrCode.setValidity(LocalDateTime.now().plusHours(24));
            qrCode.setIsUsed(false);
            
            qrRepository.save(qrCode);
            
            // Generate QR code image
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, 300, 300);
            
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            String base64Image = Base64.getEncoder().encodeToString(pngData);
            
            response.put("success", true);
            response.put("qrCode", code);
            response.put("qrImage", "data:image/png;base64," + base64Image);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to generate QR code: " + e.getMessage());
        }
        
        return response;
    }
    
    public Map<String, Object> validateQRCode(String code) {
        Map<String, Object> response = new HashMap<>();
        
        QRCode qrCode = qrRepository.findByCode(code).orElse(null);
        
        if (qrCode == null) {
            response.put("valid", false);
            response.put("message", "Invalid QR code");
        } else if (qrCode.getIsUsed()) {
            response.put("valid", false);
            response.put("message", "QR code already used");
        } else if (qrCode.getValidity().isBefore(LocalDateTime.now())) {
            response.put("valid", false);
            response.put("message", "QR code expired");
        } else {
            qrCode.setIsUsed(true);
            qrRepository.save(qrCode);
            response.put("valid", true);
            response.put("bookingId", qrCode.getBookingId());
            response.put("message", "QR code validated successfully");
        }
        
        return response;
    }
}