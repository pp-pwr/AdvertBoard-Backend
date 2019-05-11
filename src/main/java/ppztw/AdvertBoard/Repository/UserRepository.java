package ppztw.AdvertBoard.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ppztw.AdvertBoard.Model.User.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("SELECT u FROM #{#entityName} u WHERE u.profile.visibleName LIKE CONCAT('%',:name,'%')")
    Page<User> findAllByProfileVisibleNameLike(@Param("name") String nameContains, Pageable pageable);

    Optional<User> findByProfileId(Long profileId);
}