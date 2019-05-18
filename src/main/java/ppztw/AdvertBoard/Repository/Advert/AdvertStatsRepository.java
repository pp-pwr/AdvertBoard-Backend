package ppztw.AdvertBoard.Repository.Advert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppztw.AdvertBoard.Model.Stats.AdvertStats;

@Repository
public interface AdvertStatsRepository extends JpaRepository<AdvertStats, Long> {

}
