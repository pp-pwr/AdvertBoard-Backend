package ppztw.AdvertBoard.Repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.Report.AdvertReport;
import ppztw.AdvertBoard.Model.Report.CaseStatus;
import ppztw.AdvertBoard.Model.User.AuthProvider;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.Repository.Advert.AdvertReportRepository;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class AdvertReportRepositoryTests {

    private static final int REPORTNUM = 10;
    @Autowired
    private AdvertReportRepository advertReportRepository;

    @Autowired
    private TestEntityManager entityManager;


    @Before
    public void setUp() {
        for (int i = 0; i < REPORTNUM; i++) {
            User user = new User();
            user.setName("username" + i);
            user.setEmail("mail" + i + "@mail.com");
            user.setProvider(AuthProvider.local);

            Advert advert = new Advert();
            advert.setUser(user);
            advert.setTitle("title");
            advert.setDescription("advertDesc");

            AdvertReport report = new AdvertReport(user, advert, "comment");
            if (i >= REPORTNUM / 2)
                report.setCaseStatus(CaseStatus.solved);
            else
                report.setCaseStatus(CaseStatus.unsolved);
            entityManager.persistAndFlush(user);
            entityManager.persistAndFlush(advert);
            entityManager.persistAndFlush(report);

        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAll_PageSizeZeroGiven_ShouldThrowIllegalArgumentException() {
        Pageable pageable = PageRequest.of(0, 0);
        advertReportRepository.findAll(pageable).getNumberOfElements();
    }

    @Test
    public void findAll_PageSizeOneGiven_ShouldReturnOneAdvertReport() {
        Pageable pageable = PageRequest.of(0, 1);
        assertThat(advertReportRepository.findAll(pageable).getNumberOfElements()).isEqualTo(1);
    }

    @Test
    public void findAll_PageSizeManyGiven_ShouldReturnManyAdvertReports() {
        for (int i = 2; i < REPORTNUM; i++) {
            Pageable pageable = PageRequest.of(0, i);
            assertThat(advertReportRepository.findAll(pageable).getNumberOfElements()).isEqualTo(i);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void findAllByCaseStatus_PageSizeZeroGiven_ShouldThrowIllegalArgumentException() {
        Pageable pageable = PageRequest.of(0, 0);
        for (CaseStatus caseStatus : CaseStatus.values())
            advertReportRepository.findAllByCaseStatus(pageable, caseStatus).getNumberOfElements();
    }

    @Test
    public void findAllByCaseStatus_PageSizeOneGiven_ShouldReturnOneAdvertReportWithCorrectStatus() {
        Pageable pageable = PageRequest.of(0, 1);
        for (CaseStatus caseStatus : CaseStatus.values()) {
            Page<AdvertReport> reportPage = advertReportRepository.findAllByCaseStatus(pageable, caseStatus);

            assertThat(reportPage.getNumberOfElements()).isEqualTo(1);
            for (AdvertReport report : reportPage.getContent()) {
                assertThat(report.getCaseStatus()).isEqualByComparingTo(caseStatus);
            }
        }
    }

    @Test
    public void findAllByCaseStatus_PageSizeManyGiven_ShouldReturnManyAdvertReportsWithCorrectStatus() {
        for (int i = 2; i < REPORTNUM / 2; i++) {
            Pageable pageable = PageRequest.of(0, 1);
            for (CaseStatus caseStatus : CaseStatus.values()) {
                Page<AdvertReport> reportPage = advertReportRepository.findAllByCaseStatus(pageable, caseStatus);

                assertThat(reportPage.getNumberOfElements()).isEqualTo(1);
                for (AdvertReport report : reportPage.getContent()) {
                    assertThat(report.getCaseStatus()).isEqualByComparingTo(caseStatus);
                }
            }
        }
    }
}
