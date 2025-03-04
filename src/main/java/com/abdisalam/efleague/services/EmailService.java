package com.abdisalam.efleague.services;


import com.abdisalam.efleague.modal.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;
    private final String fromEmail;
    private static final String COMMISSIONER_EMAIL = "abdisalamfresh@gmail.com"; // Replace with actual commissioner email

    @Autowired
    public EmailService(JavaMailSender mailSender, Environment env) {
        this.mailSender = mailSender;
        this.fromEmail = env.getProperty("spring.mail.username"); // Get from application.properties
    }

    public void sendCommissionerNotification(String subject, String userEmail, String username, String role, String teamPreference) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("abdik171@gmail.com");  // Change to commissioner email
        message.setSubject(subject);

        String emailBody = String.format(
                """
                🏀 New User Inquiry to Join a Team 🏀
        
                📌 **User Information**
                - **Username:** %s
                - **Email:** %s
                - **Role Selected:** %s
                - **Team Preference:** %s
        
                📨 **Action Required**
                Please review the user's request and assign them to a team if appropriate.
        
                ⚠️ **This is an automated email. Please do not reply directly.**
                """,
                username, userEmail, role, teamPreference
        );

        message.setText(emailBody);
        message.setFrom("abdisalamfresh@gmail.com");  // Ensure this is your configured email

        try {
            mailSender.send(message);
            System.out.println("📧 Commissioner notification sent successfully!");
        } catch (Exception e) {
            System.out.println("❌ Error Sending Inquiry Email: " + e.getMessage());
        }
    }

}