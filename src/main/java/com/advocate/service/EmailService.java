package com.advocate.service;

import java.util.Properties;
import java.util.Random;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final String fromEmail = "advocate.business.official@gmail.com"; // Sender's email
    private final String appPassword = "qzrs vkxf ipgb fweq"; // App password

    // Send Invitation message to the provided email address
    public String sendSignUpOtp(String recipientEmail) {

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Set up the session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });

        String otp = generateOtp();
        try {
         
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("");
            message.setSubject(" Your 6-digit OTP(One time Password) to Sign Up to Your Account is :" + otp);

        
            String emailBody = """
                    Hi,

                    Your One-Time Password (OTP) for Sign-up in to Advocate Account is: %s

                    Use this OTP to complete your signup process.

                    Cheers,
                    %s
                    """;

            String formattedMessage = String.format(emailBody, otp, "Advocate");
            message.setText(formattedMessage);

            // Send the email
            Transport.send(message);

            // Log success
            System.out.println("OTP sent to: " + recipientEmail);

            // Return the generated OTP
            return otp;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send OTP to: " + recipientEmail);
            return null;
        }
    }

    public String sendOtpNotification(String recipientEmail) {
        // SMTP server configuration


        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        // Generate OTP
        String otp = generateOtp();

        // Set up the session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, appPassword);
            }
        });
        // Email For Login
        try {
            // Create a message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(" Your 6-digit OTP(One time Password) to Login to Your Account is :" + otp);

            // Create the email body
            String emailBody = """
                    Hi,

                    Your One-Time Password (OTP) for Logging-in Advocate Account is: %s

                    Use this OTP to complete your Login process.

                    Cheers,
                    %s
                    """;

            String formattedMessage = String.format(emailBody, otp, "Advocate");
            message.setText(formattedMessage);

            // Send the email
            Transport.send(message);

            // Log success
            System.out.println("OTP sent to: " + recipientEmail);

            // Return the generated OTP
            return otp;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send OTP to: " + recipientEmail);
            return null;
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); 
        return String.valueOf(otp);
    }

    
}

// App Password Steps :-
// App password will created from manage your google account->security->2-Step
// Verification->App Password-> Create Specific App-> Name="JavaMail
// App"->Create->Copy the 16-character password-> Paste in final String
// appPassword.
// you are good to go then.
