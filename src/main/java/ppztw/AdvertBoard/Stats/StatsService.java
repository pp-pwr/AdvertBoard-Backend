package ppztw.AdvertBoard.Stats;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.Stats.AdvertStats;
import ppztw.AdvertBoard.Repository.Advert.AdvertStatsRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StatsService {

    @Autowired
    private AdvertStatsRepository advertStatsRepository;

    public void addAdvertEntry(Long advertId) {
        AdvertStats advertStats = findAdvertStatsById(advertId);
        advertStats.setEntryCount(advertStats.getEntryCount() + 1);
        List<LocalDate> entryDates = advertStats.getEntryDates();
        entryDates.add(LocalDate.now());
        advertStats.setEntryDates(entryDates);
        advertStatsRepository.save(advertStats);
    }

    public Integer getAdvertEntryCount(Long advertId) {
        AdvertStats advertStats = findAdvertStatsById(advertId);
        return advertStats.getEntryCount();
    }

    public void setAdvertReported(Long advertId) {
        AdvertStats advertStats = findAdvertStatsById(advertId);
        advertStats.setReportCount(advertStats.getReportCount() + 1);
        List<LocalDate> reportDates = advertStats.getReportDates();
        reportDates.add(LocalDate.now());
        advertStats.setReportDates(reportDates);
        advertStatsRepository.save(advertStats);
    }

    public Integer getReportedAdvertsCount() {
        return advertStatsRepository.countReportedAdverts();
    }

    public Integer getAdvertReportCount(Long advertId) {
        return findAdvertStatsById(advertId).getReportCount();
    }

    public Map<Integer, Integer> getReportedAdvertsCountInYearBetweenMonths(Integer year,
                                                                            Integer beginMonth,
                                                                            Integer endMonth) {
        Map<Integer, Integer> months = new HashMap<>();
        for (int i = beginMonth; i <= endMonth; i++) {
            Integer monthCount = getReportedAdvertsCountInYearAndMonth(year, i);
            months.put(i, monthCount);
        }
        return months;
    }

    public Map<Integer, Integer> getAdvertReportsCountInYearBetweenMonths(Integer year,
                                                                          Integer beginMonth,
                                                                          Integer endMonth) {
        Map<Integer, Integer> months = new HashMap<>();
        for (int i = beginMonth; i <= endMonth; i++) {
            Integer monthCount = getAdvertReportsCountInYearAndMonth(year, i);
            months.put(i, monthCount);
        }
        return months;
    }

    public Integer getReportedAdvertsCountByDate(LocalDate date) {
        return advertStatsRepository.countReportedAdvertsByDate(date);
    }

    public Integer getAdvertReportsCountByDate(LocalDate date) {
        return advertStatsRepository.countAdvertReportsByDate(date);
    }

    private Integer getAdvertReportsCountInYearAndMonth(Integer year, Integer monthNum) {
        return advertStatsRepository.countAdvertReportsByDateBetween(
                YearMonth.of(year, monthNum).atDay(1),
                YearMonth.of(year, monthNum).atEndOfMonth());
    }

    private Integer getReportedAdvertsCountInYearAndMonth(Integer year, Integer monthNum) {
        return advertStatsRepository.countReportedAdvertsByDateBetween(
                YearMonth.of(year, monthNum).atDay(1),
                YearMonth.of(year, monthNum).atEndOfMonth());
    }

    private AdvertStats findAdvertStatsById(Long advertId) {
        return advertStatsRepository.findById(advertId).orElseThrow(() ->
                new ResourceNotFoundException("Advert", "id", advertId));
    }
}
