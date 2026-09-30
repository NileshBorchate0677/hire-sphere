package com.hiresphere.hiresphere.Auth.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Service implementation for sending transactional emails (such as OTP codes)
 * via SMTP (e.g. Gmail SMTP).
 */
@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${spring.mail.from:HireSphere Security <no-reply@hiresphere.com>}")
    private String fromAddress;

    @Override
    public boolean sendPasswordResetOtp(String toEmail, String otp, String recipientName) {
        if (mailSender == null || mailUsername == null || mailUsername.trim().isBlank()) {
            log.info("[EMAIL] Gmail SMTP credentials not configured (SPRING_MAIL_USERNAME is empty). " +
                     "Skipping real email dispatch. Simulated OTP for {}: {}", toEmail, otp);
            return false;
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            String sender = (mailUsername != null && mailUsername.contains("@")) ? mailUsername : fromAddress;
            helper.setFrom(sender, "HireSphere");
            helper.setTo(toEmail);
            helper.setSubject("HireSphere Password Reset Code: " + otp);

            String displayName = (recipientName != null && !recipientName.isBlank()) ? recipientName.trim() : "Valued Member";
            String htmlContent = buildOtpHtmlEmail(displayName, otp);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("[EMAIL] Password reset OTP email dispatched successfully to: {}", toEmail);
            return true;

        } catch (Exception e) {
            log.error("[EMAIL] Failed to send password reset OTP to {}: {}", toEmail, e.getMessage());
            return false;
        }
    }

    private String buildOtpHtmlEmail(String name, String otp) {
        return "<!DOCTYPE html>"
                + "<html lang=\"en\">"
                + "<head><meta charset=\"UTF-8\"><title>HireSphere Password Reset</title></head>"
                + "<body style=\"margin: 0; padding: 0; background-color: #f8fafc; font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Helvetica, Arial, sans-serif;\">"
                + "  <table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" style=\"background-color: #f8fafc; padding: 40px 15px;\">"
                + "    <tr>"
                + "      <td align=\"center\">"
                + "        <table width=\"100%\" max-width=\"540px\" cellpadding=\"0\" cellspacing=\"0\" style=\"max-width: 540px; background-color: #ffffff; border-radius: 20px; box-shadow: 0 10px 25px rgba(0,0,0,0.05); overflow: hidden; border: 1px solid #e2e8f0;\">"
                + "          <!-- Header Banner -->"
                + "          <tr>"
                + "            <td style=\"background: linear-gradient(135deg, #4f46e5 0%, #7c3aed 100%); padding: 32px 30px; text-align: center;\">"
                + "              <div style=\"font-size: 26px; font-weight: 900; color: #ffffff; letter-spacing: -0.5px;\">Hire<span style=\"color: #c7d2fe;\">Sphere</span></div>"
                + "              <div style=\"font-size: 11px; text-transform: uppercase; color: #e0e7ff; letter-spacing: 1.5px; margin-top: 4px; font-weight: 600;\">Career Ecosystem & Verification</div>"
                + "            </td>"
                + "          </tr>"
                + "          <!-- Content Body -->"
                + "          <tr>"
                + "            <td style=\"padding: 35px 30px;\">"
                + "              <h2 style=\"margin: 0 0 12px 0; font-size: 20px; font-weight: 800; color: #0f172a;\">Password Reset Request</h2>"
                + "              <p style=\"margin: 0 0 20px 0; font-size: 14px; line-height: 22px; color: #475569;\">Hello <strong>" + name + "</strong>,</p>"
                + "              <p style=\"margin: 0 0 24px 0; font-size: 14px; line-height: 22px; color: #475569;\">We received a request to reset your password for your HireSphere account. Please use the following 6-digit verification code to proceed:</p>"
                + "              <!-- OTP Box -->"
                + "              <div style=\"background-color: #f1f5f9; border: 2px dashed #cbd5e1; border-radius: 14px; padding: 22px; text-align: center; margin-bottom: 24px;\">"
                + "                <span style=\"font-family: 'Courier New', Courier, monospace; font-size: 34px; font-weight: 900; letter-spacing: 8px; color: #4f46e5;\">" + otp + "</span>"
                + "              </div>"
                + "              <p style=\"margin: 0 0 8px 0; font-size: 13px; line-height: 20px; color: #64748b;\">⏳ This verification code is <strong>valid for 30 minutes</strong> and can only be used once.</p>"
                + "              <p style=\"margin: 0; font-size: 12px; line-height: 18px; color: #94a3b8;\">🔒 If you did not make this request, you can safely ignore this email. Your current password remains secure.</p>"
                + "            </td>"
                + "          </tr>"
                + "          <!-- Footer -->"
                + "          <tr>"
                + "            <td style=\"background-color: #f8fafc; border-top: 1px solid #f1f5f9; padding: 20px 30px; text-align: center;\">"
                + "              <p style=\"margin: 0; font-size: 11px; color: #94a3b8;\">© 2026 HireSphere. Empowering Talents & Modern Organizations.</p>"
                + "            </td>"
                + "          </tr>"
                + "        </table>"
                + "      </td>"
                + "    </tr>"
                + "  </table>"
                + "</body>"
                + "</html>";
    }
}
