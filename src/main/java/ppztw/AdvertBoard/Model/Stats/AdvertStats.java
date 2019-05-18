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

    private Integer reportCount;

    public AdvertStats() {
        entryCount = 0;
        reportCount = 0;
    }

    public boolean isReported() {
        return reportCount > 0;
    }
}

