package ppztw.AdvertBoard.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ppztw.AdvertBoard.Model.User.CaseStatus;
import ppztw.AdvertBoard.Model.User.Report;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Page<Report> findAll(Pageable pageable);

    Page<Report> findAllByCaseStatus(Pageable pageable, CaseStatus caseStatus);

}
