package ppztw.AdvertBoard.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ppztw.AdvertBoard.Admin.AdminService;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.User.CaseStatus;
import ppztw.AdvertBoard.Payload.ApiResponse;
import ppztw.AdvertBoard.Security.CurrentUser;
import ppztw.AdvertBoard.Security.UserPrincipal;
import ppztw.AdvertBoard.View.ReportView;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @GetMapping("/report/all")
    @PreAuthorize("hasRole('ADMIN')")
    Page<ReportView> getAllReports(@CurrentUser UserPrincipal userPrincipal, Pageable pageable) {

        return adminService.getAllReports(pageable);
    }

    @GetMapping("/report")
    @PreAuthorize("hasRole('ADMIN')")
    Page<ReportView> getAllReportsByCaseStatus(@CurrentUser UserPrincipal userPrincipal, Pageable pageable,
                                               @RequestParam CaseStatus caseStatus) {

        return adminService.getAllReportsByCaseStatus(caseStatus, pageable);
    }

    @PostMapping("/report/status")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<?> setReportCaseStatus(@CurrentUser UserPrincipal userPrincipal,
                                          @RequestParam Long reportId,
                                          @RequestParam CaseStatus status) {

        adminService.setReportCaseStatus(reportId, status);
        return ResponseEntity.ok(new ApiResponse(true, "Status changed"));
    }

    @PostMapping("/advert/status")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<?> setAdvertStatus(@CurrentUser UserPrincipal userPrincipal, @RequestParam Long advertId,
                                      @RequestParam Advert.Status status) {

        adminService.setAdvertStatus(advertId, status);
        return ResponseEntity.ok(new ApiResponse(true, "Status changed"));
    }


}
