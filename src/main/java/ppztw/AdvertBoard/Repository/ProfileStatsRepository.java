package ppztw.AdvertBoard.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ppztw.AdvertBoard.Model.Stats.ProfileStats;

import java.time.LocalDate;

@Repository
public interface ProfileStatsRepository extends JpaRepository<ProfileStats, Long> {

    @Query("SELECT COUNT(a) FROM #{#entityName} a WHERE a.reportCount > 0")
    Integer countReportedProfiles();

    @Query("SELECT COUNT(DISTINCT a) FROM #{#entityName} a JOIN a.reportDates rd WHERE rd = :date")
    Integer countReportedProfilesByDate(@Param("date") LocalDate date);


    @Query("SELECT COUNT(a) FROM #{#entityName} a JOIN a.reportDates rd WHERE rd = :date")
    Integer countProfileReportsByDate(@Param("date") LocalDate date);


    @Query("SELECT COUNT(DISTINCT a) FROM #{#entityName} a JOIN a.reportDates rd WHERE rd BETWEEN :dateFrom AND :dateTo")
    Integer countReportedProfilesByDateBetween(@Param("dateFrom") LocalDate dateFrom,
                                               @Param("dateTo") LocalDate dateTo);

    @Query("SELECT COUNT(a) FROM #{#entityName} a JOIN a.reportDates rd WHERE rd BETWEEN :dateFrom AND :dateTo")
    Integer countProfileReportsByDateBetween(@Param("dateFrom") LocalDate dateFrom,
                                             @Param("dateTo") LocalDate dateTo);


}
