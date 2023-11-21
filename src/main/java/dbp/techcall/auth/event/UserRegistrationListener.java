package dbp.techcall.auth.event;

import dbp.techcall.booking.email.EmailService;
import dbp.techcall.user.domain.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.EventListener;

@Component
public class UserRegistrationListener implements ApplicationListener<UserRegisteredEvent> {

    @Autowired
    private EmailService emailService;

    @Override
    public void onApplicationEvent(UserRegisteredEvent event) {
        Users registeredUser = event.getRegisteredUser();
        emailService.sendWelcomeEmail(registeredUser.getEmail(), registeredUser.getFirstName());
    }
}
