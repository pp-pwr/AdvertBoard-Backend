package ppztw.AdvertBoard.Repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.Advert.Category;
import ppztw.AdvertBoard.Model.Stats.AdvertStats;
import ppztw.AdvertBoard.Model.User.AuthProvider;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.Repository.Advert.AdvertStatsRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class AdvertStatsRepositoryTests {

    @Autowired
    private AdvertStatsRepository advertStatsRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() {
        User user = new User();
        user.setEmail("mail@mail.me");
        user.setProvider(AuthProvider.local);
        user.setName("");
        Category category = new Category();
        category.setCategoryName("category");
        category.setDescription("");
        Advert advert = new Advert("", null, "", "", category, user, null);

        AdvertStats stats = new AdvertStats();
        List<LocalDate> reportDates = new ArrayList<>();
        List<LocalDate> entryDates = new ArrayList<>();
        stats.setAdvert(advert);
        stats.setEntryCount(5);
        stats.setReportCount(5);
        advert.setAdvertStats(stats);

        for (int i = 0; i < 5; i++) {
            reportDates.add(LocalDate.of(2019, 12, 1 + (i < 3 ? 0 : i)));
            entryDates.add(LocalDate.of(2019, 12, 12));
        }
        stats.setReportDates(reportDates);
        stats.setEntryDates(entryDates);
        entityManager.persistAndFlush(user);
        entityManager.persistAndFlush(advert);
    }

    @Test
    public void countReportedAdverts_ReportedAdvertExists_ShouldReturnVal() {
        assertThat(advertStatsRepository.countReportedAdverts()).isEqualTo(1);
    }

    @Test
    public void countReportedAdvertsByDate_ExistingDateGiven_ShouldReturnVal() {
        assertThat(advertStatsRepository.countReportedAdvertsByDate(
                LocalDate.of(2019, 12, 1))).isEqualTo(1);
    }

    @Test
    public void countAdvertReportsByDate_ExistingDateGiven_ShouldReturnVal() {
        assertThat(advertStatsRepository.countAdvertReportsByDate(
                LocalDate.of(2019, 12, 1))).isEqualTo(3);
    }

    @Test
    public void countReportedAdvertsByDateBetween_ExistingDatesGiven_ShouldReturnVal() {
        assertThat(advertStatsRepository.countReportedAdvertsByDateBetween(
                LocalDate.of(2019, 12, 1),
                LocalDate.of(2019, 12, 13))).isEqualTo(1);
    }

    @Test
    public void countAdvertReportsByDateBetween_ExistingDatesGiven_ShouldReturnVal() {
        assertThat(advertStatsRepository.countAdvertReportsByDateBetween(
                LocalDate.of(2019, 12, 4),
                LocalDate.of(2019, 12, 5))).isEqualTo(2);
    }


}
