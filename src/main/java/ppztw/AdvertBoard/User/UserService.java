package ppztw.AdvertBoard.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.User.Profile;
import ppztw.AdvertBoard.Model.User.ProfileRating;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.Model.User.VerificationToken;
import ppztw.AdvertBoard.Payload.ProfileInfo;
import ppztw.AdvertBoard.Repository.ProfileRatingRepository;
import ppztw.AdvertBoard.Repository.UserRepository;
import ppztw.AdvertBoard.Repository.VerificationTokenRepository;
import ppztw.AdvertBoard.Security.UserPrincipal;
import ppztw.AdvertBoard.Util.CategoryEntryUtils;
import ppztw.AdvertBoard.View.User.ProfileSummaryView;
import ppztw.AdvertBoard.View.User.ProfileView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfileRatingRepository profileRatingRepository;

    @Autowired
    private VerificationTokenRepository tokenRepository;

    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId));
    }

    @Transactional
    public void processProfile(UserPrincipal userPrincipal, ProfileInfo profileInfo) {
        User user = findById(userPrincipal.getId());
        Profile profile = user.getProfile() == null ? new Profile() : user.getProfile();
        profile.setContactMail(profileInfo.getContactMail());
        profile.setFirstName(profileInfo.getFirstName());
        profile.setLastName(profileInfo.getLastName());
        profile.setTelephoneNumber(profileInfo.getTelephoneNumber());
        profile.setVisibleName(profileInfo.getVisibleName());
        user.setProfile(profile);
        if (profileInfo.getCategoryEntries() != null)
            user.setCategoryEntries(profileInfo.getCategoryEntries());

        userRepository.save(user);
    }

    public void addCategoryEntry(Long categoryId, Long userId, Double val) {
        if (val > 0) {
            User user = findById(userId);
            user.setCategoryEntries(CategoryEntryUtils.addEntryValue(categoryId, user, val));
            userRepository.save(user);
        }
    }

    public Page<ProfileSummaryView> getAllProfileSummaryViews(Pageable pageable,
                                                              @RequestParam(required = false) String nameContains) {

        Page<User> users;
        if (nameContains != null && !nameContains.isEmpty())
            users = userRepository.findAllByProfileVisibleNameLike(nameContains, pageable);
        else
            users = userRepository.findAll(pageable);

        List<ProfileSummaryView> profileSummaryViewList = new ArrayList<>();
        for (User user : users)
            profileSummaryViewList.add(new ProfileSummaryView(user));
        return new PageImpl<>(profileSummaryViewList, pageable, users.getTotalElements());
    }


    public ProfileView getProfileView(Long profileId) {
        User user = userRepository.findByProfileId(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile", "id", profileId));

        Double rating = getProfileRating(user.getProfile().getId());
        Integer ratingCount = getProfileRatingCount(user.getProfile().getId());


        return new ProfileView(user, rating, ratingCount);
    }


    public void rateProfile(Long userId, Long ratedProfileId, Integer rating) {
        User user = findById(userId);
        ProfileRating profileRating = profileRatingRepository.
                findByRatingIdAndRatedId(user.getProfile().getId(), ratedProfileId).orElse(new ProfileRating());

        profileRating.setRating(rating.doubleValue());
        profileRating.setRatingId(user.getProfile().getId());
        profileRating.setRatedId(ratedProfileId);
        profileRatingRepository.save(profileRating);

    }

    public Double getProfileRating(Long profileId) {
        return profileRatingRepository.getProfileRating(profileId);
    }

    public Integer getProfileRatingCount(Long profileId) {
        return profileRatingRepository.getProfileRatingCount(profileId);
    }

    public void createVerificationToken(User user, String tokenString) {
        VerificationToken token = new VerificationToken(tokenString, user);
        tokenRepository.save(token);
    }

    public void confirmToken(String tokenString) {
        Calendar calendar = Calendar.getInstance();
        VerificationToken token = tokenRepository.findByToken(tokenString).orElseThrow(() ->
                new ResourceNotFoundException("VerificationToken", "token", tokenString));

        User user = token.getUser();
        if (token.getExpiryDate().getTime() - calendar.getTime().getTime() > 0) {
            user.setEmailVerified(true);
            userRepository.save(user);
        }
    }
}
