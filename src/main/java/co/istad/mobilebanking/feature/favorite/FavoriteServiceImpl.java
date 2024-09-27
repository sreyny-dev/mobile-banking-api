package co.istad.mobilebanking.feature.favorite;

import co.istad.mobilebanking.domain.Favorite;
import co.istad.mobilebanking.feature.favorite.dto.FavoriteResponse;
import co.istad.mobilebanking.feature.favorite.dto.UpdateName;
import co.istad.mobilebanking.feature.favorite.dto.UserFavoriteRequest;
import co.istad.mobilebanking.feature.user.UserRepository;
import co.istad.mobilebanking.mapper.FavoriteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final FavoriteMapper favoriteMapper;

    @Override
    public void unFavoriteAccount(String actNo) {

        if(!favoriteRepository.existsByActNo(actNo)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account does not exist");
        }

        Favorite fav=favoriteRepository
                .findByActNo(actNo)
                .orElseThrow();

        fav.setIsDeleted(true);
        favoriteRepository.save(fav);

    }

    @Override
    public List<FavoriteResponse> findAllByUser(UserFavoriteRequest userFavoriteRequest) {

        //find user
        if(!userRepository.existsByPhoneNumber(userFavoriteRequest.userPhoneNumber())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Phone number does not exist");
        }

        List<Favorite> favorites=favoriteRepository.findAllByUserPhoneNumber(userFavoriteRequest.userPhoneNumber());


        List<Favorite> activeFavorites = favorites
                .stream()
                .filter(favorite -> !favorite.getIsDeleted())
                .toList();

        return favoriteMapper.toFavoriteResponseList(activeFavorites);
    }

    @Override
    public FavoriteResponse updateNameFavorite(UpdateName updateName) {

        Favorite fav=favoriteRepository
                .findByActNo(updateName.actNo())
                .orElseThrow();

        fav.setName(updateName.name());

        favoriteRepository.save(fav);

        return favoriteMapper.toFavoriteResponse(fav);
    }
}
