package ppztw.AdvertBoard.Stats;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.Stats.AdvertStats;
import ppztw.AdvertBoard.Repository.Advert.AdvertStatsRepository;

@Service
public class StatsService {

    @Autowired
    private AdvertStatsRepository advertStatsRepository;

    public void addAdvertEntry(Long advertId) {
        AdvertStats advertStats = findAdvertStatsById(advertId);
        advertStats.setEntryCount(advertStats.getEntryCount() + 1);
        advertStatsRepository.save(advertStats);
    }

    public Integer getAdvertEntryCount(Long advertId) {
        AdvertStats advertStats = findAdvertStatsById(advertId);
        return advertStats.getEntryCount();
    }

    public void setAdvertReported(Long advertId) {
        AdvertStats advertStats = findAdvertStatsById(advertId);
        advertStats.setReportCount(advertStats.getReportCount() + 1);
        advertStatsRepository.save(advertStats);
    }

    public Integer getReportedAdvertsCount() {
        return advertStatsRepository.countReportedAdverts();
    }

    public Integer getAdvertReportCount(Long advertId) {
        return findAdvertStatsById(advertId).getReportCount();
    }


    private AdvertStats findAdvertStatsById(Long advertId) {
        return advertStatsRepository.findById(advertId).orElseThrow(() ->
                new ResourceNotFoundException("Advert", "id", advertId));
    }
}
