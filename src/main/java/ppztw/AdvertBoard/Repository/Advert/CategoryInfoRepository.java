package ppztw.AdvertBoard.Repository.Advert;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ppztw.AdvertBoard.Model.Advert.CategoryInfo;

@Repository
public interface CategoryInfoRepository extends JpaRepository<CategoryInfo, Long> {
}
