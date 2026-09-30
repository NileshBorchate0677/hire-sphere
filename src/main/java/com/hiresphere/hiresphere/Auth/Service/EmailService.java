package com.hiresphere.hiresphere.Auth.Service;

public interface EmailService {

    /**
     * Sends a 6-digit verification OTP email for password reset via SMTP.
     *
     * @param toEmail The recipient's registered email address
     * @param otp The 6-digit numeric verification OTP code
     * @param recipientName The recipient's display name or username
     * @return true if the email was successfully dispatched, false otherwise
     */
    boolean sendPasswordResetOtp(String toEmail, String otp, String recipientName);
}
