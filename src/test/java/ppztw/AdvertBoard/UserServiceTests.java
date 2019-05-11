package ppztw.AdvertBoard;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.Repository.UserRepository;
import ppztw.AdvertBoard.User.UserService;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserServiceTests {
    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        User user = new User();
        user.setId(0L);
        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User user1 = new User();
        user1.setId(2L);
        Map<Long, Double> entries = new HashMap<>();
        entries.put(0L, 0.01);
        user1.setCategoryEntries(entries);
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
    }

    @Test(expected = ResourceNotFoundException.class)
    public void addCategoryEntry_NotExistingUserIdAndPositiveValGiven_ShouldThrowException() {
        Long userId = 1L;
        userService.addCategoryEntry(0L, userId, 0.1);
    }

    @Test
    public void addCategoryEntry_NotExistingUserIdAndNegativeValGiven_ShouldPass() {
        Long userId = 1L;
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

    @TestConfiguration
    static class AdvertUserServiceTestContextConfiguration {

        @Bean
        public UserService userService() {
            return new UserService();
        }
    }

}
