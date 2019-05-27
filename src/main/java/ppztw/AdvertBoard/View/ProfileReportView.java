package ppztw.AdvertBoard.View;

import ppztw.AdvertBoard.Model.Report.CaseStatus;
import ppztw.AdvertBoard.Model.Report.ProfileReport;

public class ProfileReportView {
    private Long id;

    private Long reportingUserId;

    private String reportingUserName;

    private Long reportedProfileId;

    private String comment;

    private CaseStatus caseStatus;

    public ProfileReportView(ProfileReport report) {
        this.id = report.getId();
        this.reportingUserId = report.getReportingUser().getId();
        this.reportingUserName = report.getReportingUser().getName();
        this.reportedProfileId = report.getReportedProfile().getId();
        this.comment = report.getComment();
        this.caseStatus = report.getCaseStatus();
    }
}
