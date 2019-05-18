package ppztw.AdvertBoard.Model.Stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ppztw.AdvertBoard.Model.Advert.Advert;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    @ElementCollection
    private List<LocalDate> entryDates;

    @ElementCollection
    private List<LocalDate> reportDates;

    private Integer reportCount;

    public AdvertStats() {
        entryCount = 0;
        reportCount = 0;
        entryDates = new ArrayList<>();
        reportDates = new ArrayList<>();
    }

    public boolean isReported() {
        return reportCount > 0;
    }
}

