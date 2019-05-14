package ppztw.AdvertBoard.Repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ppztw.AdvertBoard.Model.User.AuthProvider;
import ppztw.AdvertBoard.Model.User.Profile;
import ppztw.AdvertBoard.Model.User.User;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() {
        User user = new User();
        Profile profile = new Profile();
        user.setName("user1");
        user.setEmail("mail@test.com");
        profile.setVisibleName("user1");
        profile.setFirstName("firstUser1");
        user.setProfile(profile);
        user.setProvider(AuthProvider.local);

        User user1 = new User();
        Profile profile1 = new Profile();
        user1.setName("user2");
        user1.setEmail("mail2@test.com");
        profile1.setVisibleName("user2");
        user1.setProfile(profile1);
        user1.setProvider(AuthProvider.local);

        entityManager.persistAndFlush(user);

        entityManager.persistAndFlush(user1);
    }

    @Test
    public void findByEmail_ExistingEmailGiven_ShouldFind() {
        assertThat(userRepository.findByEmail("mail@test.com")).isNotEmpty();
    }

    @Test
    public void existsByEmail_ExistingEmailGiven_ShouldReturnTrue() {
        assertThat(userRepository.existsByEmail("mail@test.com")).isTrue();
    }

    @Test
    public void findAllByProfileVisibleNameLike_MatchingNameGiven_ShouldFind() {
        assertThat(userRepository.findAllByProfileVisibleNameLike("user", PageRequest.of(0, 10))).hasSize(2);
    }
    

}
