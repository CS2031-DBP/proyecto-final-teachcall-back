package dbp.techcall.auth.event;

import dbp.techcall.user.domain.Users;
import org.springframework.context.ApplicationEvent;

public class UserRegisteredEvent extends ApplicationEvent{
    private final Users registeredUser;

    public UserRegisteredEvent(Users registeredUser) {
        super(registeredUser);
        this.registeredUser = registeredUser;
    }

    public Users getRegisteredUser() {
        return registeredUser;
    }
}
