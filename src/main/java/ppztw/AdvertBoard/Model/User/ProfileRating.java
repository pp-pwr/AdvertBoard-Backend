package ppztw.AdvertBoard.Model.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@IdClass(ProfileRatingKey.class)
public class ProfileRating {

    @Id
    Long ratingId;

    @Id
    long ratedId;

    Double rating;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;


}
