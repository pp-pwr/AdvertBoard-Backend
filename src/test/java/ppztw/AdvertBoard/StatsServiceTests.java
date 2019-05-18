package ppztw.AdvertBoard;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ppztw.AdvertBoard.Model.Stats.AdvertStats;
import ppztw.AdvertBoard.Repository.Advert.AdvertStatsRepository;
import ppztw.AdvertBoard.Stats.StatsService;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class StatsServiceTests {

    @Autowired
    private StatsService statsService;

    @MockBean
    private AdvertStatsRepository advertStatsRepository;

    @Before
    public void setUp() {
        AdvertStats advertStats = new AdvertStats(0L, null, 0);
        Mockito.when(advertStatsRepository.findById(0L)).thenReturn(Optional.of(advertStats));
    }

    @Test
    public void getAdvertEntryCount_ExistingIdGiven_ShouldReturnNonNullValue() {
        Long advertId = 0L;

        assertThat(statsService.getAdvertEntryCount(advertId)).isNotNull();
    }

    @Test
    public void addAdvertEntry_ShouldIncreaseEntryCount() {
        Long advertId = 0L;
        Integer beforeEntryCount = statsService.getAdvertEntryCount(advertId);
        statsService.addAdvertEntry(advertId);
        Integer afterEntryCount = statsService.getAdvertEntryCount(advertId);

        assertThat(afterEntryCount).isEqualTo(beforeEntryCount + 1);
    }

    @TestConfiguration
    static class StatsServiceTestsContextConfiguration {
        @Bean
        public StatsService statsService() {
            return new StatsService();
        }
    }


}
