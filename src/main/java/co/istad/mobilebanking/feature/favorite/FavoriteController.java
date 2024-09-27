package co.istad.mobilebanking.feature.favorite;

import co.istad.mobilebanking.feature.favorite.dto.FavoriteResponse;
import co.istad.mobilebanking.feature.favorite.dto.UpdateName;
import co.istad.mobilebanking.feature.favorite.dto.UserFavoriteRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @DeleteMapping("/{actNo}")
    public void delete(@PathVariable String actNo) {
        favoriteService.unFavoriteAccount(actNo);
    }

    @GetMapping("/user")
    public List<FavoriteResponse> findAllByUser(@Valid @RequestBody UserFavoriteRequest userFavoriteRequest){
        return favoriteService.findAllByUser(userFavoriteRequest);
    }

    @PutMapping("/update-name")
    public FavoriteResponse updateName(@Valid @RequestBody UpdateName updateName){
        return favoriteService.updateNameFavorite(updateName);
    }


}
