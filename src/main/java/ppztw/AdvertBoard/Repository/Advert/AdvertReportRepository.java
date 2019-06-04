package ppztw.AdvertBoard.Repository.Advert;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ppztw.AdvertBoard.Model.Report.AdvertReport;
import ppztw.AdvertBoard.Model.Report.CaseStatus;

public interface AdvertReportRepository extends JpaRepository<AdvertReport, Long> {

    Page<AdvertReport> findAll(Pageable pageable);

    Page<AdvertReport> findAllByCaseStatus(Pageable pageable, CaseStatus caseStatus);

}
