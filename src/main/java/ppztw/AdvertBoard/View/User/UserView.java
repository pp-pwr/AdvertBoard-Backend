package ppztw.AdvertBoard.View.User;

import lombok.Getter;
import lombok.Setter;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.User.AuthProvider;
import ppztw.AdvertBoard.Model.User.Profile;
import ppztw.AdvertBoard.Model.User.Role;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.View.Advert.AdvertSummaryView;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserView {
    private String name;
    private String email;
    private Boolean emailVerified;
    private List<AdvertSummaryView> adverts;
    private AuthProvider provider;
    private MyProfileView profileView;
    private Role role;

    public UserView(User user, Double rating) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.emailVerified = user.getEmailVerified();
        this.adverts = new ArrayList<>();
        for (Advert advert : user.getAdverts())
            adverts.add(new AdvertSummaryView(advert));
        this.provider = user.getProvider();
        Profile profile = user.getProfile();
        this.profileView = profile == null ? null : new MyProfileView(user, rating);
        this.role = user.getRole();
    }
}
