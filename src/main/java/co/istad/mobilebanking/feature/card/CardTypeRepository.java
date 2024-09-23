package co.istad.mobilebanking.feature.card;

import co.istad.mobilebanking.domain.CardType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CardTypeRepository extends JpaRepository<CardType, Integer> {

    Optional<CardType> findByAlias(String alias);


}
