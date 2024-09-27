package co.istad.mobilebanking.feature.favorite;

import co.istad.mobilebanking.domain.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {

    Optional<Favorite> findByActNo(String actNo);
    Boolean existsByActNo(String actNo);

    @Query("SELECT f FROM Favorite f WHERE f.user.phoneNumber = :phoneNumber")
    List<Favorite> findAllByUserPhoneNumber(@Param("phoneNumber") String phoneNumber);

    Boolean existsByPhoneNumber(String phoneNumber);

}
