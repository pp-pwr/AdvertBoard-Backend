package ppztw.AdvertBoard.Controller;

import org.hibernate.validator.constraints.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.Payload.ApiResponse;
import ppztw.AdvertBoard.Payload.ProfileInfo;
import ppztw.AdvertBoard.Payload.ReportProfileRequest;
import ppztw.AdvertBoard.Report.ReportService;
import ppztw.AdvertBoard.Repository.UserRepository;
import ppztw.AdvertBoard.Security.CurrentUser;
import ppztw.AdvertBoard.Security.OnRegistrationCompleteEvent;
import ppztw.AdvertBoard.Security.UserPrincipal;
import ppztw.AdvertBoard.Stats.StatsService;
import ppztw.AdvertBoard.User.UserService;
import ppztw.AdvertBoard.View.User.ProfileSummaryView;
import ppztw.AdvertBoard.View.User.ProfileView;
import ppztw.AdvertBoard.View.User.UserView;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    ApplicationEventPublisher eventPublisher;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private StatsService statsService;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserView getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());
        Double rating = userService.getProfileRating(user.getProfile().getId());
        Integer ratingCount = userService.getProfileRatingCount(user.getProfile().getId());
        return new UserView(user, rating, ratingCount);

    }

    @PostMapping("user/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> setProfile(@CurrentUser UserPrincipal userPrincipal,
                                        @RequestBody ProfileInfo profileInfo) {
        userService.processProfile(userPrincipal, profileInfo);
        return ResponseEntity.ok(new ApiResponse(true, "Profile set successfully!"));
    }

    @GetMapping("/user/all")
    @PreAuthorize("permitAll()")
    public Page<ProfileSummaryView> getAllUsers(Pageable pageable,
                                                @RequestParam(required = false) String nameContains) {
        return userService.getAllProfileSummaryViews(pageable, nameContains);
    }

    @GetMapping("/user/get")
    @PreAuthorize("permitAll()")
    public ProfileView getProfile(@RequestParam Long profileId) {
        return userService.getProfileView(profileId);
    }

    @PostMapping("/user/rate")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> rateProfile(@CurrentUser UserPrincipal userPrincipal,
                                         @RequestParam @Valid Long profileId,
                                         @RequestParam @Range(min = 1, max = 5) Integer rating) {
        userService.rateProfile(userPrincipal.getId(), profileId, rating);
        return ResponseEntity.ok(new ApiResponse(true, "User rated"));
    }

    @PostMapping("/user/refreshToken")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> refreshToken(@CurrentUser UserPrincipal userPrincipal) {
        User user = userService.findById(userPrincipal.getId());
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user));

        return ResponseEntity.ok(new ApiResponse(true, "New token generated"));
    }

    @PostMapping("/user/report")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> reportProfile(@CurrentUser UserPrincipal userPrincipal,
                                           @Valid @RequestBody ReportProfileRequest request) {
        reportService.addProfileReport(userPrincipal.getId(), request.getProfileId(), request.getComment());
        statsService.setUserReported(request.getProfileId());
        return ResponseEntity.ok(new ApiResponse(true, "User report added"));
    }
}