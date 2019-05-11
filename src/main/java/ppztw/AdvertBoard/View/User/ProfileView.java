package ppztw.AdvertBoard.View.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.View.Advert.AdvertSummaryView;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProfileView extends MyProfileView {


    List<AdvertSummaryView> advertSummaryViews;

    public ProfileView(User user) {
        super(user);
        this.advertSummaryViews = new ArrayList<>();
        List<Advert> advertList = user.getAdverts();
        for (Advert advert : advertList)
            if (advert.getStatus() != Advert.Status.ARCHIVED &&
                    advert.getStatus() != Advert.Status.BANNED)
                this.advertSummaryViews.add(new AdvertSummaryView(advert));

    }
}
