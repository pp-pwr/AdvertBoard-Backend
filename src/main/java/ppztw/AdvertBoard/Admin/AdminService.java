package ppztw.AdvertBoard.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.User.CaseStatus;
import ppztw.AdvertBoard.Model.User.Report;
import ppztw.AdvertBoard.Repository.Advert.AdvertRepository;
import ppztw.AdvertBoard.Repository.ReportRepository;
import ppztw.AdvertBoard.View.Advert.AdvertSummaryView;
import ppztw.AdvertBoard.View.ReportView;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private AdvertRepository advertRepository;

    public Page<ReportView> getAllReports(Pageable pageable) {
        List<ReportView> reportViewList = new ArrayList<>();
        Page<Report> reports = reportRepository.findAll(pageable);
        for (Report report : reports)
            reportViewList.add(new ReportView(report));
        return new PageImpl<>(reportViewList, pageable, reports.getTotalElements());
    }

    public Page<ReportView> getAllReportsByCaseStatus(CaseStatus caseStatus, Pageable pageable) {
        List<ReportView> reportViewList = new ArrayList<>();
        Page<Report> reports = reportRepository.findAllByCaseStatus(pageable, caseStatus);
        for (Report report : reports)
            reportViewList.add(new ReportView(report));
        return new PageImpl<>(reportViewList, pageable, reports.getTotalElements());
    }

    public void setReportCaseStatus(Long reportId, CaseStatus caseStatus) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("Report", "id", reportId));
        report.setCaseStatus(caseStatus);

        reportRepository.save(report);
    }

    public void setAdvertStatus(Long advertId, Advert.Status status) {
        Advert advert = advertRepository.findById(advertId)
                .orElseThrow(() -> new ResourceNotFoundException("Advert", "id", advertId));

        advert.setStatus(status);
        advertRepository.save(advert);
    }

    public Page<AdvertSummaryView> getAdvertsByStatus(Advert.Status status, Pageable pageable) {
        List<AdvertSummaryView> advertSummaryViewList = new ArrayList<>();
        Page<Advert> adverts = advertRepository.findAllByStatus(pageable, status);

        for (Advert advert : adverts)
            advertSummaryViewList.add(new AdvertSummaryView(advert));

        return new PageImpl<>(advertSummaryViewList, pageable, adverts.getTotalElements());
    }

}
