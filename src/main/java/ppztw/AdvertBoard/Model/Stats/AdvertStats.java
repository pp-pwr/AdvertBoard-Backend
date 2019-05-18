package ppztw.AdvertBoard.Model.Stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ppztw.AdvertBoard.Model.Advert.Advert;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class AdvertStats implements Serializable {
    @Id
    private Long id;

    @JoinColumn(name = "id")
    @OneToOne
    @MapsId
    private Advert advert;

    private Integer entryCount;

    private boolean isReported;

    public AdvertStats() {
        entryCount = 0;
        isReported = false;
    }


}

