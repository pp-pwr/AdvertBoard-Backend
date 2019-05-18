package ppztw.AdvertBoard.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ppztw.AdvertBoard.Model.User.ProfileRating;

import java.util.Optional;

public interface ProfileRatingRepository extends JpaRepository<ProfileRating, Long> {

    @Query("SELECT AVG(pr.rating) FROM #{#entityName} pr WHERE pr.ratedId = :id")
    Double getProfileRating(@Param("id") Long profileId);

    @Query("SELECT COUNT(pr.rating) FROM #{#entityName} pr WHERE pr.ratedId = :id")
    Integer getProfileRatingCount(@Param("id") Long profileId);

    Optional<ProfileRating> findByRatingIdAndRatedId(Long ratingId, Long ratedId);

}
