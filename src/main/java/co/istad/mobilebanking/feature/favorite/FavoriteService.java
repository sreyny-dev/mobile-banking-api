package co.istad.mobilebanking.feature.favorite;

import co.istad.mobilebanking.feature.favorite.dto.FavoriteResponse;
import co.istad.mobilebanking.feature.favorite.dto.UpdateName;
import co.istad.mobilebanking.feature.favorite.dto.UserFavoriteRequest;

import java.util.List;

public interface FavoriteService {
    void unFavoriteAccount(String actNo);
    List<FavoriteResponse> findAllByUser(UserFavoriteRequest userFavoriteRequest);
    FavoriteResponse updateNameFavorite(UpdateName updateName);
}
