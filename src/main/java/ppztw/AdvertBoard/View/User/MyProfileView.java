package ppztw.AdvertBoard.View.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ppztw.AdvertBoard.Model.User.User;

@Getter
@Setter
@NoArgsConstructor
public class MyProfileView extends ProfileSummaryView {
    String firstName;

    String lastName;

    String telephoneNumber;

    String contactMail;

    Double rating;

    Integer ratingCount;

    Boolean isVerified;

    public MyProfileView(User user, Double rating, Integer ratingCount) {
        super(user);
        this.firstName = user.getProfile().getFirstName();
        this.lastName = user.getProfile().getLastName();
        this.telephoneNumber = user.getProfile().getTelephoneNumber();
        this.contactMail = user.getProfile().getContactMail();
        this.rating = rating;
        this.ratingCount = ratingCount;
        this.isVerified = user.getEmailVerified();
    }
}
