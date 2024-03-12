package fontys.emergencywebapps.configurations.event.listener;

import fontys.emergencywebapps.business.exceptions.CustomException;
import fontys.emergencywebapps.business.impl.UserServiceImpl;
import fontys.emergencywebapps.persistence.entities.User;
import fontys.emergencywebapps.persistence.repos.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import fontys.emergencywebapps.configurations.event.RegistrationCompleteEvent;
import org.springframework.context.ApplicationListener;

import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegisterCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent>{

    private final UserServiceImpl userService;

    private final UserRepository userRepository;

    private final JavaMailSender mailSender;
    private User user;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        Optional<User> userOptional = userRepository.findByUsername(event.getUser().getUsername());
        if (userOptional.isEmpty()) {
            throw new CustomException("User not found");
        }
        user = userOptional.get();

        String verificationToken = UUID.randomUUID().toString();

        userService.createVerificationToken(user, verificationToken);
        String url = event.getAppUrl() + "/api/auth/registrationConfirm?token=" + verificationToken;

        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Error sending email: {}" , e.getMessage());
        }
        log.info("Click the link to verify your registration: {}" , url);

    }
    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Email Verification";
        String senderName = "Emergency Web Apps";
        String emailContent = "<p> Hi, " +user.getLastName() + "</p>"
                + "<p> Thank you for registering with us </p>"
                + "<p> Please click the link below to verify your email address: </p>"
                + "<p><a href=\"" + url + "\">Verify your email to activate your account</a></p>"
                + "<br>"
                + "<p> Thank you, </p>"
                + "<p> Emergency Web Apps </p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom("emergencywebapp2023@gmail.com", senderName);
        helper.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(emailContent, "utf-8", "html");
        mailSender.send(message);
    }
}
