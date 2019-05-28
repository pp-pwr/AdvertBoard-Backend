package ppztw.AdvertBoard.Stats;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.Stats.AdvertStats;
import ppztw.AdvertBoard.Model.Stats.ProfileStats;
import ppztw.AdvertBoard.Model.User.Profile;
import ppztw.AdvertBoard.Repository.Advert.AdvertStatsRepository;
import ppztw.AdvertBoard.Repository.ProfileStatsRepository;
import ppztw.AdvertBoard.Repository.UserRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class StatsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertStatsRepository advertStatsRepository;

    @Autowired
    private ProfileStatsRepository profileStatsRepository;

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

    public void setUserReported(Long profileId) {
        ProfileStats profileStats = findProfileStatsById(profileId);
        profileStats.setReportCount(profileStats.getReportCount() + 1);
        List<LocalDate> reportDates = profileStats.getReportDates();
        reportDates.add(LocalDate.now());
        profileStats.setReportDates(reportDates);
        profileStatsRepository.save(profileStats);
    }

    public Integer getReportedAdvertsCount() {
        return advertStatsRepository.countReportedAdverts();
    }

    public Integer getReportedProfilesCount() {
        return profileStatsRepository.countReportedProfiles();
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

    public Map<Integer, Integer> getReportedProfilesCountInYearBetweenMonths(Integer year,
                                                                             Integer beginMonth,
                                                                             Integer endMonth) {
        Map<Integer, Integer> months = new HashMap<>();
        for (int i = beginMonth; i <= endMonth; i++) {
            Integer monthCount = getReportedProfilesCountInYearAndMonth(year, i);
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

    public Map<Integer, Integer> getProfileReportsCountInYearBetweenMonths(Integer year,
                                                                           Integer beginMonth,
                                                                           Integer endMonth) {
        Map<Integer, Integer> months = new HashMap<>();
        for (int i = beginMonth; i <= endMonth; i++) {
            Integer monthCount = getProfileReportsCountInYearAndMonth(year, i);
            months.put(i, monthCount);
        }
        return months;
    }



    public Integer getReportedAdvertsCountByDate(LocalDate date) {
        return advertStatsRepository.countReportedAdvertsByDate(date);
    }

    public Integer getReportedProfilesCountByDate(LocalDate date) {
        return profileStatsRepository.countReportedProfilesByDate(date);
    }

    public Integer getAdvertReportsCountByDate(LocalDate date) {
        return advertStatsRepository.countAdvertReportsByDate(date);
    }

    public Integer getProfileReportsCountByDate(LocalDate date) {
        return profileStatsRepository.countProfileReportsByDate(date);
    }

    private Integer getProfileReportsCountInYearAndMonth(Integer year, Integer monthNum) {
        return profileStatsRepository.countProfileReportsByDateBetween(
                YearMonth.of(year, monthNum).atDay(1),
                YearMonth.of(year, monthNum).atEndOfMonth());
    }

    private Integer getAdvertReportsCountInYearAndMonth(Integer year, Integer monthNum) {
        return advertStatsRepository.countAdvertReportsByDateBetween(
                YearMonth.of(year, monthNum).atDay(1),
                YearMonth.of(year, monthNum).atEndOfMonth());
    }

    private Integer getReportedProfilesCountInYearAndMonth(Integer year, Integer monthNum) {
        return profileStatsRepository.countReportedProfilesByDateBetween(
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

    private ProfileStats findProfileStatsById(Long profileId) {
        Optional<ProfileStats> profileStatsOptional = profileStatsRepository.findById(profileId);
        ProfileStats profileStats;
        if (profileStatsOptional.isPresent())
            profileStats = profileStatsOptional.get();
        else {
            Profile profile = userRepository.findByProfileId(
                    profileId).orElseThrow(() -> new ResourceNotFoundException(
                    "Profile", "id", "profileId")).getProfile();
            profileStats = new ProfileStats();
            profileStats.setProfile(profile);
            profileStats = profileStatsRepository.save(profileStats);
        }
        return profileStats;
    }
}
