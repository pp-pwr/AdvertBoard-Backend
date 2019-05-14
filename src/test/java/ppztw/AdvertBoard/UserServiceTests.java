package ppztw.AdvertBoard;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.User.Profile;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.Repository.UserRepository;
import ppztw.AdvertBoard.User.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserServiceTests {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        User user = new User();
        user.setId(0L);
        user.setProfile(new Profile());
        user.getProfile().setId(0L);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User user1 = new User();
        user1.setId(2L);
        user1.setProfile(new Profile());
        user1.getProfile().setId(2L);
        Map<Long, Double> entries = new HashMap<>();
        entries.put(0L, 0.01);
        user1.setCategoryEntries(entries);
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        Mockito.when(userRepository.findByProfileId(2L)).thenReturn(Optional.of(user1));

        for (long i = 3L; i < 10L; i++) {
            User usr = new User();
            usr.setId(i);
            Profile profile = new Profile();
            profile.setId(i);
            usr.setProfile(profile);
            Mockito.when(userRepository.findById(i)).thenReturn(Optional.of(usr));
        }
    }

    @Test(expected = ResourceNotFoundException.class)
    public void addCategoryEntry_NotExistingUserIdAndPositiveValGiven_ShouldThrowException() {
        Long userId = -1L;
        userService.addCategoryEntry(0L, userId, 0.1);
    }

    @Test
    public void addCategoryEntry_NotExistingUserIdAndNegativeValGiven_ShouldPass() {
        Long userId = -1L;
        userService.addCategoryEntry(0L, userId, -0.1);
    }

    @Test
    public void addCategoryEntry_NewCategoryGiven_ShouldInsertVal() {
        Long userId = 0L;
        Long catId = 0L;
        Double val = 0.1;
        userService.addCategoryEntry(catId, userId, val);
        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId));
        assertThat(user.getCategoryEntries().get(catId)).isEqualTo(val);
    }

    @Test
    public void addCategoryEntry_ExistingCatGiven_ShouldAddVal() {
        Long userId = 2L;
        Long catId = 0L;
        Double val = 0.1;

        User user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId));

        Double oldVal = user.getCategoryEntries().get(catId);

        userService.addCategoryEntry(catId, userId, val);
        user = userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User", "id", userId));
        assertThat(user.getCategoryEntries().get(catId)).isEqualTo(val + oldVal);
    }

    @Test
    public void rateProfile_FirstRatingGiven_ShouldReturnEqualToRating() {
        Long userId = 0L;
        Long ratedProfileId = 2L;
        Integer rating = 5;
        userService.rateProfile(userId, ratedProfileId, rating);
        assertThat(userService.getProfileRating(ratedProfileId)).isEqualTo(rating.doubleValue());
    }

    @Test
    public void rateProfile_FewRatingsOneUserGiven_ShouldReturnLastOne() {
        Long userId = 0L;
        Long ratedProfileId = 2L;
        ArrayList<Integer> ratings = new ArrayList<>();
        ratings.add(1);
        ratings.add(2);
        ratings.add(3);

        for (Integer rating : ratings)
            userService.rateProfile(userId, ratedProfileId, rating);
        assertThat(userService.getProfileRating(ratedProfileId)).isEqualTo(ratings.get(2).doubleValue());
    }

    @Test
    public void rateProfile_FewRatingsFewUsersGiven_ShouldReturnAverage() {
        ArrayList<Long> userIds = new ArrayList<>();
        Long profileId = 2L;
        Integer ratingSum = 0;
        for (int i = 4; i < 7; i++) {
            Integer rating = i - 3;
            System.out.println(rating);
            ratingSum += rating;
            userService.rateProfile((long) i, profileId, rating);
        }
        Double avg = ratingSum.doubleValue() / 3;
        assertThat(userService.getProfileRating(profileId)).isEqualTo(avg);
    }


    @TestConfiguration
    static class AdvertUserServiceTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserService();
        }
    }

}
