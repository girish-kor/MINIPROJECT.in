package miniproject.in.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import miniproject.in.model.Order;
import miniproject.in.model.Product;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendOtp(String email, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(email);
            helper.setSubject("Your OTP for miniproject.in");
            helper.setText(
                    String.format("Your OTP is: %s\n\nThis OTP will expire in 5 minutes.", otp));

            mailSender.send(message);
            log.info("OTP sent to {}", email);
        } catch (MessagingException e) {
            log.error("Failed to send OTP to {}", email, e);
            throw new RuntimeException("Failed to send OTP");
        }
    }

    public void sendProductDelivery(Order order, Product product) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(order.getUserEmail());
            helper.setSubject("Your Software Purchase from miniproject.in");

            String emailBody = String.format(
                    "Thank you for your purchase!\n\n" + "Product: %s\n" + "Order ID: %s\n"
                            + "Amount: $%.2f\n\n" + "Your software is attached to this email.\n\n"
                            + "Best regards,\n" + "miniproject.in Team",
                    product.getName(), order.getId(), order.getAmount());

            helper.setText(emailBody);

            // Attach the file if it exists
            if (product.getFilePath() != null) {
                File file = new File(product.getFilePath());
                if (file.exists()) {
                    FileSystemResource attachment = new FileSystemResource(file);
                    helper.addAttachment(product.getFileName(), attachment);
                }
            }

            mailSender.send(message);
            log.info("Product delivery email sent to {} for order {}", order.getUserEmail(),
                    order.getId());
        } catch (MessagingException e) {
            log.error("Failed to send delivery email for order {}", order.getId(), e);
            throw new RuntimeException("Failed to send delivery email");
        }
    }
}
