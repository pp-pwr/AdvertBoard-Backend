package ppztw.AdvertBoard.Report;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.User.Report;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.Repository.Advert.AdvertRepository;
import ppztw.AdvertBoard.Repository.ReportRepository;
import ppztw.AdvertBoard.Repository.UserRepository;

@Service
public class ReportService {

    @Autowired
    private ReportRepository reportRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertRepository advertRepository;


    public void addReport(Long userId, Long advertId, String comment) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId));

        Advert advert = advertRepository.findById(advertId).orElseThrow(() ->
                new ResourceNotFoundException("Advert", "id", advertId));

        Report report = new Report(user, advert, comment);

        reportRepository.save(report);
    }


}
