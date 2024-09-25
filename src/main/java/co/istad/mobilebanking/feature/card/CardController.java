package co.istad.mobilebanking.feature.card;

import co.istad.mobilebanking.feature.card.dto.CardResponse;
import co.istad.mobilebanking.feature.card.dto.CreateCardRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/cards")
public class CardController {

    private final CardService cardService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    CardResponse createCard(@Valid @RequestBody CreateCardRequest createCardRequest){
        return cardService.createCard(createCardRequest);
    }

    @GetMapping
    List<CardResponse> findAllCard(){
        return cardService.findAllCard();
    }

    @GetMapping("/cardNumber/{cardNumber}")
    CardResponse findCardByCardNumber(@PathVariable String cardNumber){
        return cardService.findCardByCardNumber(cardNumber);
    }

    @PutMapping("/freeze-card/{cardNumber}")
    CardResponse freezeCard(@PathVariable String cardNumber){
        return cardService.freezeCard(cardNumber);
    }

    @PutMapping("/unfreeze-card/{cardNumber}")
    CardResponse unfreezeCard(@PathVariable String cardNumber){
        return cardService.unfreezeCard(cardNumber);
    }

    @DeleteMapping("/delete-card/{cardNumber}")
    CardResponse deleteCard(@PathVariable String cardNumber){
        return cardService.deleteCard(cardNumber);
    }

}
