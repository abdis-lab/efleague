package com.abdisalam.efleague.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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

    public void sendCommissionerNotification(String subject, String username, String role) {
        String emailBody = """
        📢 **New User Registration Notification**
        
        A new user has signed up on the E&F Basketball League platform. Here are the details:

        🔹 **Username:** %s
        🔹 **Role:** %s
        🔹 **Signup Time:** %s

        **Next Steps for the Commissioner:**
        - If the user is a *Captain*, review their request and approve/reject their team.
        - If the user is a *Player*, monitor their team assignment or manually assign them.
        - For any necessary follow-ups, you may reach out to the user.

        ✅ Please log in to the **Admin Panel** to manage user requests.

        🔗 [Go to Admin Panel](http://localhost:8080/admin)

        ---
        📌 *This is an automated email. Do not reply.*
        """.formatted(username, role, java.time.LocalDateTime.now());

        // Send Email
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("abdisalamfresh@gamil.com");
        message.setSubject(subject);
        message.setText(emailBody);

        mailSender.send(message);

        System.out.println("📧 Email sent to commissioner successfully!");
    }

}