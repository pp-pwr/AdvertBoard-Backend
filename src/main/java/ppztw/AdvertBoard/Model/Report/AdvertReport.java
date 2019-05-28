package ppztw.AdvertBoard.Model.Report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.User.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AdvertReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    User reportingUser;

    @OneToOne
    Advert reportedAdvert;

    String comment;

    @Enumerated(EnumType.STRING)
    CaseStatus caseStatus = CaseStatus.unsolved;

    public AdvertReport(User reportingUser, Advert reportedAdvert, String comment) {
        this.reportingUser = reportingUser;
        this.reportedAdvert = reportedAdvert;
        this.comment = comment;
    }


}
