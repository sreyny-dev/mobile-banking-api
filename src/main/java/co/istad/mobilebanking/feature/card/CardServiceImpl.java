package co.istad.mobilebanking.feature.card;

import co.istad.mobilebanking.domain.Account;
import co.istad.mobilebanking.domain.Card;
import co.istad.mobilebanking.domain.CardType;
import co.istad.mobilebanking.feature.account.AccountRepository;
import co.istad.mobilebanking.feature.card.dto.CardResponse;
import co.istad.mobilebanking.feature.card.dto.CreateCardRequest;
import co.istad.mobilebanking.mapper.CardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final CardTypeRepository cardTypeRepository;
    private final CardMapper cardMapper;
    private final AccountRepository accountRepository;

    @Override
    public CardResponse createCard(CreateCardRequest createCardRequest) {

        //Validate actNo if it existed
        if(!accountRepository.existsByActNo(createCardRequest.actNo())){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "ActNo does not exist"
            );
        }
        //validate if actNo is already link ot another card
        if(cardRepository.existsByAccount_actNo(createCardRequest.actNo())){
            throw new ResponseStatusException(
              HttpStatus.CONFLICT, "ActNo is already linked to another card."
            );
        }

        //validate card number
        if(cardRepository.existsByCardNumber(createCardRequest.cardNumber())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Card Number is already existed"
            );
        }




        Account account=accountRepository.findByActNo(createCardRequest.actNo())
                .orElseThrow();

        Card card=cardMapper.fromCreateCardRequest(createCardRequest);

        CardType cardType=cardTypeRepository.findByAlias(createCardRequest.cardTypeAlias()).orElseThrow();

        card.setCardType(cardType);
        card.setAccount(account);
        card.setIssuedDate(LocalDate.now());
        card.setExpiryDate(LocalDate.now().plusYears(2));
        card.setIsDeleted(false);
        card.setIsFrozen(false);

        cardRepository.save(card);

        return cardMapper.toCardResponse(card);
    }

    @Override
    public List<CardResponse> findAllCard() {

        List<Card> cards=cardRepository.findAll();

        return cardMapper.toCardResponseList(cards);
    }

    @Override
    public CardResponse findCardByCardNumber(String cardNumber) {

        Card card=cardRepository
                .findByCardNumber(cardNumber)
                .orElseThrow();

        return cardMapper.toCardResponse(card);
    }

    @Override
    public CardResponse freezeCard(String cardNumber) {

        Card card=cardRepository
                .findByCardNumber(cardNumber)
                .orElseThrow();

        card.setIsFrozen(true);
        cardRepository.save(card);

        return cardMapper.toCardResponse(card);
    }

    @Override
    public CardResponse unfreezeCard(String cardNumber) {
        Card card=cardRepository
                .findByCardNumber(cardNumber)
                .orElseThrow();

        card.setIsFrozen(false);
        cardRepository.save(card);

        return cardMapper.toCardResponse(card);
    }

    @Override
    public CardResponse deleteCard(String cardNumber) {

        Card card=cardRepository
                .findByCardNumber(cardNumber)
                .orElseThrow();
        card.setIsDeleted(true);
        cardRepository.save(card);
        return cardMapper.toCardResponse(card);
    }


}
