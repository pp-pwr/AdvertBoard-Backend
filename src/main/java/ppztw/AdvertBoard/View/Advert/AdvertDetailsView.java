package ppztw.AdvertBoard.View.Advert;

import lombok.Getter;
import lombok.Setter;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.Advert.AdvertInfo;
import ppztw.AdvertBoard.Model.Advert.Tag;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AdvertDetailsView extends AdvertSummaryView {

    Long profileId;
    String profileName;
    String description;
    Long categoryId;
    String categoryName;
    List<String> tags;
    Map<String, Object> additionalInfo;
    Advert.Status status;
    Integer entryCount;


    public AdvertDetailsView(Advert advert) {
        super(advert);
        this.profileId = advert.getUser().getProfile() != null ?
                advert.getUser().getProfile().getId() : null;
        this.profileName = advert.getUser().getProfile() != null ?
                advert.getUser().getProfile().getVisibleName() : null;
        this.description = advert.getDescription();
        this.categoryId = advert.getCategory().getId();
        this.categoryName = advert.getCategory().getCategoryName();
        this.tags = new ArrayList<>();
        this.additionalInfo = new HashMap<>();
        this.status = advert.getStatus();
        this.entryCount = advert.getAdvertStats() != null ?
                advert.getAdvertStats().getEntryCount() : 0;
        for (Tag tag : advert.getTags())
            this.tags.add(tag.getName());
        for (AdvertInfo info : advert.getAdditionalInfo()) {
            Object value = new Object();
            switch (info.getCategoryInfo().getType()) {
                case word:
                    value = info.getValue();
                    break;
                case money:
                    value = BigDecimal.valueOf(Double.valueOf(info.getValue()
                            .replace(',', '.')))
                            .setScale(2, RoundingMode.UP)
                            .doubleValue();
                    break;
                case intNum:
                    value = Long.valueOf(info.getValue());
                    break;
                case floatNum:
                    value = Double.valueOf(info.getValue().replace(',', '.'));
                    break;
            }
            additionalInfo.put(info.getCategoryInfo().getName(), value);
        }
    }
}
