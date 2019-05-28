package ppztw.AdvertBoard.Model.Report;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ppztw.AdvertBoard.Model.User.Profile;
import ppztw.AdvertBoard.Model.User.User;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class ProfileReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne
    User reportingUser;

    @OneToOne
    Profile reportedProfile;

    String comment;

    @Enumerated(EnumType.STRING)
    CaseStatus caseStatus = CaseStatus.unsolved;

    public ProfileReport(User reportingUser, Profile reportedProfile, String comment) {
        this.reportingUser = reportingUser;
        this.reportedProfile = reportedProfile;
        this.comment = comment;
    }


}
