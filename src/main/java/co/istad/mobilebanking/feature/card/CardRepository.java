package co.istad.mobilebanking.feature.card;

import co.istad.mobilebanking.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {

    Boolean existsByAccount_actNo(String actNo);
    Boolean existsByCardNumber(String cardNumber);

}
