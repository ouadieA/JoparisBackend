package com.ecommerce.service.mail;

import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public byte[] generateQRCode(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }

    public void sendEmail(User user, Product product) {
        try{
            String urlWithUserId = "User Name = " + user.getName() + " " + "Email = " + user.getEmail() + " Product Name = " + product.getName() + " Product ID = " + product.getId();
            byte[] qrCodeImage = generateQRCode(urlWithUserId, 200, 200);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("spring.email.from@gmail.com");
            helper.setTo(user.getEmail());
            helper.setSubject("Thank you for your purchase!");
            helper.setText("<html><body>" +
                    "<p>Hello 👋,</p>" +
                    "<p>Thank you for your purchase! Here is the QR code you can scan:</p>" +
                    "<img src='cid:qrCode'>" +
                    "</body></html>", true);

            helper.addInline("qrCode", new ByteArrayResource(qrCodeImage), "image/png");

            mailSender.send(message);
        } catch (Exception e){
            System.out.println("Something Went Wrong...");
        }
    }
}
