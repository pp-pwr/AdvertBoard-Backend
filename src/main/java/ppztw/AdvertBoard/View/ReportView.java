package ppztw.AdvertBoard.View;

import lombok.Getter;
import lombok.Setter;
import ppztw.AdvertBoard.Model.Report.AdvertReport;
import ppztw.AdvertBoard.Model.Report.CaseStatus;

@Getter
@Setter
public class ReportView {
    private Long id;

    private Long reportingUserId;

    private String reportingUserName;

    private Long reportedAdvertId;

    private String comment;

    private CaseStatus caseStatus;

    public ReportView(AdvertReport report) {
        this.id = report.getId();
        this.reportingUserId = report.getReportingUser().getId();
        this.reportingUserName = report.getReportingUser().getName();
        this.reportedAdvertId = report.getReportedAdvert().getId();
        this.comment = report.getComment();
        this.caseStatus = report.getCaseStatus();
    }

}
