package co.istad.mobilebanking.feature.favorite;

import co.istad.mobilebanking.feature.favorite.dto.FavoriteResponse;
import co.istad.mobilebanking.feature.favorite.dto.UpdateName;
import co.istad.mobilebanking.feature.favorite.dto.UserFavoriteRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/{actNo}")
    public void delete(@PathVariable String actNo) {
        favoriteService.unFavoriteAccount(actNo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/user")
    public List<FavoriteResponse> findAllByUser(@Valid @RequestBody UserFavoriteRequest userFavoriteRequest){
        return favoriteService.findAllByUser(userFavoriteRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/update-name")
    public FavoriteResponse updateName(@Valid @RequestBody UpdateName updateName){
        return favoriteService.updateNameFavorite(updateName);
    }


}
