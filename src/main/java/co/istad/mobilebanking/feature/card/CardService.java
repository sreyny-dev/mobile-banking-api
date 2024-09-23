package co.istad.mobilebanking.feature.card;

import co.istad.mobilebanking.feature.card.dto.CardResponse;
import co.istad.mobilebanking.feature.card.dto.CreateCardRequest;

import java.util.List;

public interface CardService {

    CardResponse createCard(CreateCardRequest createCardRequest);
    List<CardResponse> findAllCard();
}
