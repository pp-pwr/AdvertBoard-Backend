package ppztw.AdvertBoard.View.Advert;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.io.Resource;
import ppztw.AdvertBoard.Model.Advert.Advert;

import java.time.LocalDate;

@Getter
@Setter
public class AdvertSummaryView {
    Long id;
    String title;
    String pic;
    Long categoryId;
    LocalDate date;
    Boolean recommended;
    Long categoryId;

    public AdvertSummaryView(Advert advert) {
        this.id = advert.getId();
        this.title = advert.getTitle();
        this.pic = advert.getImagePath();
        this.date = advert.getDate();
        this.categoryId = advert.getCategory().getId();
        recommended = false;
    }
}
