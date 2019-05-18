package ppztw.AdvertBoard.Repository.Advert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppztw.AdvertBoard.Model.Advert.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

}