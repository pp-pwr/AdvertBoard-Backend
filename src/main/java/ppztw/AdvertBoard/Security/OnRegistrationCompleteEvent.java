package ppztw.AdvertBoard.Security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;
import ppztw.AdvertBoard.Model.User.User;

@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private User user;

    public OnRegistrationCompleteEvent(User user) {
        super(user);
        this.user = user;
    }
}
