package ppztw.AdvertBoard.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppztw.AdvertBoard.Model.Report.CaseStatus;
import ppztw.AdvertBoard.Model.Report.ProfileReport;

@Repository
public interface ProfileReportRepository extends JpaRepository<ProfileReport, Long> {

    Page<ProfileReport> findAll(Pageable pageable);

    Page<ProfileReport> findAllByCaseStatus(Pageable pageable, CaseStatus caseStatus);
}
