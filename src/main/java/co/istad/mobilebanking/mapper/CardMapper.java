package co.istad.mobilebanking.mapper;

import co.istad.mobilebanking.domain.Card;
import co.istad.mobilebanking.feature.card.dto.CardResponse;
import co.istad.mobilebanking.feature.card.dto.CreateCardRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CardMapper {

    Card fromCreateCardRequest(CreateCardRequest createCardRequest);
    CardResponse toCardResponse(Card card);
    List<CardResponse> toCardResponseList(List<Card> cardList);
}
