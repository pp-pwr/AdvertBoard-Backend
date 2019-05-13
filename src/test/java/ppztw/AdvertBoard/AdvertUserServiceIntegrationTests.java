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
import ppztw.AdvertBoard.Advert.AdvertUserService;
import ppztw.AdvertBoard.Exception.BadRequestException;
import ppztw.AdvertBoard.Exception.ResourceNotFoundException;
import ppztw.AdvertBoard.Model.Advert.Advert;
import ppztw.AdvertBoard.Model.Advert.Category;
import ppztw.AdvertBoard.Model.User.Profile;
import ppztw.AdvertBoard.Model.User.User;
import ppztw.AdvertBoard.Payload.Advert.CreateAdvertRequest;
import ppztw.AdvertBoard.Repository.Advert.AdvertRepository;
import ppztw.AdvertBoard.Repository.Advert.CategoryInfoRepository;
import ppztw.AdvertBoard.Repository.Advert.CategoryRepository;
import ppztw.AdvertBoard.Repository.Advert.TagRepository;
import ppztw.AdvertBoard.Repository.UserRepository;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class AdvertUserServiceIntegrationTests {
    @MockBean
    private AdvertRepository advertRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private CategoryRepository categoryRepository;
    @MockBean
    private TagRepository tagRepository;
    @MockBean
    private CategoryInfoRepository categoryInfoRepository;
    @Autowired
    private AdvertUserService advertUserService;

    @Before
    public void setUp() {
        User user = new User();
        user.setId(0L);

        Advert advert = new Advert();
        advert.setId(0L);

        Category category = new Category();
        category.setId(0L);

        Profile profile = new Profile();

        advert.setUser(user);
        advert.setCategory(category);
        List<Advert> advertList = new ArrayList<>();
        advertList.add(advert);

        user.setProfile(profile);
        user.setAdverts(advertList);

        Mockito.when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Mockito.when(advertRepository.findById(advert.getId())).thenReturn(Optional.of(advert));
        Mockito.when(categoryRepository.findById(category.getId())).thenReturn(Optional.of(category));

    }

    @Test
    public void findAdvert_ExistingUserAndAdvertIdGiven_ShouldReturnAdvert() {
        Long userId = 0L;
        Long advertId = 0L;

        Optional<Advert> advertOptional = advertUserService.findAdvert(userId, advertId);
        Advert advert = advertOptional.get();
        assertThat(advert).isNotNull();
        assertThat(advert.getId()).isEqualTo(advertId);
        assertThat(advert.getUser().getId()).isEqualTo(userId);
    }

    @Test
    public void findAdvert_NotExistingAdvertIdGiven_ShouldReturnNull() {
        Long userId = 0L;
        Long advertId = 1L;
        assertThat(advertUserService.findAdvert(userId, advertId)).isEmpty();
    }

    @Test(expected = ResourceNotFoundException.class)
    public void findAdvert_NotExistingUserIdGiven_ShouldThrowException() {
        Long userId = 1L;
        Long advertId = 0L;
        advertUserService.findAdvert(userId, advertId);
    }

    @Test
    public void addAdvert_ExistingUserAndValidRequestGiven_ShouldAddAdvert() {
        Long userId = 0L;
        CreateAdvertRequest request = new CreateAdvertRequest();
        request.setTitle("Title");
        request.setDescription("description");
        request.setCategory(0L);
        request.setAdditionalInfo(null);
        request.setTags(null);
        request.setImageFile(null);
        advertUserService.addAdvert(userId, request);
    }

    @Test(expected = BadRequestException.class)
    public void addAdvert_ExistingUserIdAndInvalidAdditionalInfo_ShouldThrowException() {
        Long userId = 0L;

        Long additionalInfoId = 1L;
        Map<String, String> additionalInfos = new HashMap<>();
        additionalInfos.put(additionalInfoId.toString(), "");

        CreateAdvertRequest request = new CreateAdvertRequest();
        request.setTitle("Title");
        request.setDescription("description");
        request.setCategory(0L);
        request.setAdditionalInfo(additionalInfos);
        request.setTags(null);
        request.setImageFile(null);
        advertUserService.addAdvert(userId, request);
    }

    @TestConfiguration
    static class AdvertUserServiceTestContextConfiguration {

        @Bean
        public AdvertUserService userService() {
            return new AdvertUserService();
        }
    }

}
