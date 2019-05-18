package ppztw.AdvertBoard.Repository.Advert;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ppztw.AdvertBoard.Model.Advert.Advert;

import java.util.List;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {

    @Query("SELECT a FROM #{#entityName} a WHERE a.category.id IN :ids AND a.status IN (0, 1)")
    Page<Advert> findAllByCategoryIdIn(@Param("ids") List<Long> categoryIdList, Pageable pageable);

    @Query("SELECT a FROM #{#entityName} a " +
            "WHERE a.category.id IN :ids AND a.status IN (0, 1) AND a.title LIKE CONCAT('%',:substr,'%')")
    Page<Advert> findAllByCategoryIdInAndTitleLike(@Param("ids") List<Long> categoryIdList,
                                                   @Param("substr") String titleContains,
                                                   Pageable pageable);

    @Query(value = "SELECT * FROM adverts a WHERE a.category_category_id = :id AND a.status IN (0, 1)" +
            " ORDER BY random() LIMIT :adcount", nativeQuery = true)
    List<Advert> findRandomByCategoryId(@Param("id") Long categoryId, @Param("adcount") Integer advertCount);

    Page<Advert> findAllByStatus(Pageable pageable, Advert.Status status);

    @Query(value = "SELECT * FROM adverts a WHERE a.user_id = :id AND a.status IN (0, 1)" +
            " ORDER BY a.date LIMIT :adcount", nativeQuery = true)
    List<Advert> findByUserId(@Param("id") Long userId, @Param("adcount") Integer advertCount);
}
