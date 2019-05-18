package ppztw.AdvertBoard.Repository.Advert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ppztw.AdvertBoard.Model.Stats.AdvertStats;

import java.time.LocalDate;

@Repository
public interface AdvertStatsRepository extends JpaRepository<AdvertStats, Long> {

    @Query("SELECT COUNT(a) FROM #{#entityName} a WHERE a.reportCount > 0")
    Integer countReportedAdverts();

    @Query("SELECT COUNT(DISTINCT a) FROM #{#entityName} a JOIN a.reportDates rd WHERE rd = :date")
    Integer countReportedAdvertsByDate(@Param("date") LocalDate date);


    @Query("SELECT COUNT(a) FROM #{#entityName} a JOIN a.reportDates rd WHERE rd = :date")
    Integer countAdvertReportsByDate(@Param("date") LocalDate date);


    @Query("SELECT COUNT(DISTINCT a) FROM #{#entityName} a JOIN a.reportDates rd WHERE rd BETWEEN :dateFrom AND :dateTo")
    Integer countReportedAdvertsByDateBetween(@Param("dateFrom") LocalDate dateFrom,
                                              @Param("dateTo") LocalDate dateTo);

    @Query("SELECT COUNT(a) FROM #{#entityName} a JOIN a.reportDates rd WHERE rd BETWEEN :dateFrom AND :dateTo")
    Integer countAdvertReportsByDateBetween(@Param("dateFrom") LocalDate dateFrom,
                                            @Param("dateTo") LocalDate dateTo);

}
