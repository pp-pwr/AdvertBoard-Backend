package ppztw.AdvertBoard.Repository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import ppztw.AdvertBoard.Model.Advert.Tag;
import ppztw.AdvertBoard.Repository.Advert.TagRepository;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
public class TagRepositoryTests {
    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private
    TestEntityManager entityManager;

    @Before
    public void setUp() {
        Tag tag = new Tag("Name1");
        tag = entityManager.persistAndFlush(tag);
    }

    @Test
    public void findByName_ExistingNameGiven_ShouldFind() {
        assertThat(tagRepository.findByName("Name1")).isNotEmpty();
    }

}
