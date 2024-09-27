package co.istad.mobilebanking.mapper;

import co.istad.mobilebanking.domain.Favorite;
import co.istad.mobilebanking.feature.favorite.dto.FavoriteResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    List<FavoriteResponse> toFavoriteResponseList(List<Favorite> favorites);
    FavoriteResponse toFavoriteResponse(Favorite favorite);

}
