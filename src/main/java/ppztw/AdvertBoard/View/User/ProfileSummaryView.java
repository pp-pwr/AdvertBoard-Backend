package ppztw.AdvertBoard.View.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ppztw.AdvertBoard.Model.User.User;

@Getter
@Setter
@NoArgsConstructor
public class ProfileSummaryView {
    Long id;

    String visibleName;

    public ProfileSummaryView(User user) {
        this.id = user.getProfile().getId();
        this.visibleName = user.getProfile().getVisibleName();
    }
}
