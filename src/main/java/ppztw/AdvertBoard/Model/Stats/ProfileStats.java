package ppztw.AdvertBoard.Model.Stats;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ppztw.AdvertBoard.Model.User.Profile;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class ProfileStats implements Serializable {
    @Id
    private Long id;

    @JoinColumn(name = "id")
    @OneToOne
    @MapsId
    private Profile profile;

    private Integer reportCount;

    @ElementCollection
    private List<LocalDate> reportDates;

    public ProfileStats() {
        reportCount = 0;
        reportDates = new ArrayList<>();
    }

    public boolean isReported() {
        return reportCount > 0;
    }
}
