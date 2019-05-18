package ppztw.AdvertBoard.Repository.Advert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ppztw.AdvertBoard.Model.Stats.AdvertStats;

@Repository
public interface AdvertStatsRepository extends JpaRepository<AdvertStats, Long> {

    @Query("SELECT COUNT(a) FROM #{#entityName} a WHERE a.isReported = TRUE")
    Integer countReportedAdverts();

}
