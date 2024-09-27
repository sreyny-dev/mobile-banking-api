package co.istad.mobilebanking.feature.favorite.dto;

public record FavoriteResponse(
        String name,
        String actNo,
        String favoriteType,
        Boolean isDeleted
) {
}
