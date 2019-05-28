package ppztw.AdvertBoard.Admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.Report.AdvertReport;
import ppztw.AdvertBoard.Model.Report.CaseStatus;
import ppztw.AdvertBoard.Model.Report.ProfileReport;
import ppztw.AdvertBoard.Model.User.Role;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.Repository.Advert.AdvertReportRepository;
import ppztw.AdvertBoard.Repository.Advert.AdvertRepository;
import ppztw.AdvertBoard.Repository.ProfileReportRepository;
import ppztw.AdvertBoard.Repository.UserRepository;
import ppztw.AdvertBoard.View.Advert.AdvertSummaryView;
import ppztw.AdvertBoard.View.ProfileReportView;
import ppztw.AdvertBoard.View.ReportView;
import ppztw.AdvertBoard.View.User.ProfileSummaryView;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdvertReportRepository advertReportRepository;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private ProfileReportRepository profileReportRepository;

    @Autowired
    private UserRepository userRepository;

    public Page<ReportView> getAllReports(Pageable pageable) {
        List<ReportView> reportViewList = new ArrayList<>();
        Page<AdvertReport> reports = advertReportRepository.findAll(pageable);
        for (AdvertReport report : reports)
            reportViewList.add(new ReportView(report));
        return new PageImpl<>(reportViewList, pageable, reports.getTotalElements());
    }


    public Page<ProfileReportView> getAllProfileReports(Pageable pageable) {
        List<ProfileReportView> reportViewList = new ArrayList<>();
        Page<ProfileReport> reports = profileReportRepository.findAll(pageable);
        for (ProfileReport report : reports)
            reportViewList.add(new ProfileReportView(report));
        return new PageImpl<>(reportViewList, pageable, reports.getTotalElements());
    }

    public Page<ReportView> getAllReportsByCaseStatus(CaseStatus caseStatus, Pageable pageable) {
        List<ReportView> reportViewList = new ArrayList<>();
        Page<AdvertReport> reports = advertReportRepository.findAllByCaseStatus(pageable, caseStatus);
        for (AdvertReport report : reports)
            reportViewList.add(new ReportView(report));
        return new PageImpl<>(reportViewList, pageable, reports.getTotalElements());
    }


    public Page<ProfileReportView> getAllProfileReportsByCaseStatus(CaseStatus caseStatus, Pageable pageable) {
        List<ProfileReportView> reportViewList = new ArrayList<>();
        Page<ProfileReport> reports = profileReportRepository.findAllByCaseStatus(pageable, caseStatus);
        for (ProfileReport report : reports)
            reportViewList.add((new ProfileReportView(report)));
        return new PageImpl<>(reportViewList, pageable, reports.getTotalElements());
    }

    public void setReportCaseStatus(Long reportId, CaseStatus caseStatus) {
        AdvertReport report = advertReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("AdvertReport", "id", reportId));
        report.setCaseStatus(caseStatus);

        advertReportRepository.save(report);
    }


    public void setProfileReportCaseStatus(Long reportId, CaseStatus caseStatus) {
        ProfileReport report = profileReportRepository.findById(reportId)
                .orElseThrow(() -> new ResourceNotFoundException("ProfileReport", "id", reportId));
        report.setCaseStatus(caseStatus);

        profileReportRepository.save(report);
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

    public void setUserRole(Long profileId, Role role) {
        User user = userRepository.findByProfileId(profileId).orElseThrow(()
                -> new ResourceNotFoundException("Profile", "id", profileId));
        user.setRole(role);
        userRepository.save(user);
    }

    public Page<ProfileSummaryView> getProfilesByUserRole(Pageable pageable, Role role) {
        Page<User> users = userRepository.findAllByRole(pageable, role);
        List<ProfileSummaryView> profileSummaryViewList = new ArrayList<>();
        for (User user : users)
            profileSummaryViewList.add(new ProfileSummaryView(user));
        return new PageImpl<>(profileSummaryViewList, pageable, users.getTotalElements());
    }
}
