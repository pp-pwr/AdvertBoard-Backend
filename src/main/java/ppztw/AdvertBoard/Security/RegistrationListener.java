package ppztw.AdvertBoard.Security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.User.UserService;

import java.util.UUID;


@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);
        String recipientAddress = user.getEmail();

        String subject = "Registration Confirmation";
        String confirmationUrl = "http://pwr-advert-board-frontend.herokuapp.com" + "/signupConfirm?token=" + token;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(confirmationUrl);
        javaMailSender.send(mailMessage);
    }

}

