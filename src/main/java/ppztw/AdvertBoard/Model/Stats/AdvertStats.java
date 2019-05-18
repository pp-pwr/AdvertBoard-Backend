package ppztw.AdvertBoard.Model.Stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ppztw.AdvertBoard.Model.Advert.Advert;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class AdvertStats {
    @Id
    private Long id;

    @MapsId
    @OneToOne
    private Advert advert;

    private Integer entryCount;

    public AdvertStats() {
        entryCount = 0;
    }


}

