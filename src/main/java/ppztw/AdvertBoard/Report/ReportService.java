package ppztw.AdvertBoard.Report;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.Report.AdvertReport;
import ppztw.AdvertBoard.Model.Report.ProfileReport;
import ppztw.AdvertBoard.Model.User.Profile;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.Repository.Advert.AdvertReportRepository;
import ppztw.AdvertBoard.Repository.Advert.AdvertRepository;
import ppztw.AdvertBoard.Repository.ProfileReportRepository;
import ppztw.AdvertBoard.Repository.UserRepository;

@Service
public class ReportService {

    @Autowired
    private AdvertReportRepository advertReportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertRepository advertRepository;

    @Autowired
    private ProfileReportRepository profileReportRepository;

    public void addReport(Long userId, Long advertId, String comment) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId));

        Advert advert = advertRepository.findById(advertId).orElseThrow(() ->
                new ResourceNotFoundException("Advert", "id", advertId));

        AdvertReport report = new AdvertReport(user, advert, comment);

        advertReportRepository.save(report);
    }


    public void addProfileReport(Long userId, Long profileId, String comment) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId));

        Profile profile = userRepository.findByProfileId(profileId).orElseThrow(() ->
                new ResourceNotFoundException("Profile", "id", profileId)).getProfile();

        ProfileReport report = new ProfileReport(user, profile, comment);
        profileReportRepository.save(report);
    }
}
